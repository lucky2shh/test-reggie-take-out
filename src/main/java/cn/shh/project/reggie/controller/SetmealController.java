package cn.shh.project.reggie.controller;

import cn.shh.project.reggie.dto.SetmealDto;
import cn.shh.project.reggie.pojo.Category;
import cn.shh.project.reggie.pojo.Dish;
import cn.shh.project.reggie.pojo.Setmeal;
import cn.shh.project.reggie.service.CategoryService;
import cn.shh.project.reggie.service.SetmealDishService;
import cn.shh.project.reggie.service.SetmealService;
import cn.shh.project.reggie.util.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache", allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmealCache", allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 启售/停售
     * @param type
     * @param ids
     * @return
     */
    @PostMapping("/status/{type}")
    public R<String> editStatus(@PathVariable int type, String ids){
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.set(Setmeal::getStatus, type);
        updateWrapper.in(Setmeal::getId, ids);
        setmealService.update(updateWrapper);
        return R.success("操作成功！");
    }

    /**
     * 根据Id查询套餐信息及嵌套的菜品信息
     * @param id
     * @return
     */
    @Cacheable(value = "setmealCache", key = "#p0", unless = "#result == null")
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmeal = setmealService.getByIdWithDish(id);
        return R.success(setmeal);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache", allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("操作成功");
    }
}
