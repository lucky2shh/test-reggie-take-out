package cn.shh.project.reggie.service.impl;

import cn.shh.project.reggie.common.exception.CustomException;
import cn.shh.project.reggie.mapper.CategoryMapper;
import cn.shh.project.reggie.pojo.Category;
import cn.shh.project.reggie.pojo.Dish;
import cn.shh.project.reggie.pojo.Setmeal;
import cn.shh.project.reggie.service.CategoryService;
import cn.shh.project.reggie.service.DishService;
import cn.shh.project.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void removeById(Long id) {
        // 1、判断当前分类是否关联的有菜品，若关联则给出提示并返回
        LambdaQueryWrapper<Dish> dishQW = new LambdaQueryWrapper<>();
        dishQW.eq(Dish::getCategoryId, id);
        Long dishCount = dishService.count(dishQW);
        if (dishCount > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }
        // 2、判断当前分类是否关联了套餐，若关联则给出提示并返回
        LambdaQueryWrapper<Setmeal> setmealQW = new LambdaQueryWrapper<>();
        setmealQW.eq(Setmeal::getCategoryId, id);
        Long setmealCount = setmealService.count(setmealQW);
        if (setmealCount > 0){
            throw new CustomException("当前分类下关联了套餐，不能删除！");
        }

        // 3、正常删除
        super.removeById(id);
    }
}
