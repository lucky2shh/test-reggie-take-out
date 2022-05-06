package cn.shh.project.reggie.controller;

import cn.shh.project.reggie.pojo.Employee;
import cn.shh.project.reggie.service.EmployeeService;
import cn.shh.project.reggie.util.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public R<String> login(HttpServletResponse response){
        return R.error("NOLOGIN");
    }

    /**
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // 1、给密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2、根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3、如果没有此用户，返回登录失败
        if (emp == null){
            return R.error("登录失败！");
        }

        // 4、查看密码是否正确
        if (! emp.getPassword().equals(password)){
            return R.error("密码错误，登录失败！");
        }

        // 5、用户是否被禁用
        if (emp.getStatus() == 0){
            return R.error("账号已被禁用！");
        }

        // 6、登录成功，将用户ID放入session，返回登录成功
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 1、清理session
        request.getSession().removeAttribute("employee");
        // 2、返回结果
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        // 1、若员工已存在，直接返回。（也可通过异常来提示，减少数据库压力）
        /*String username = employee.getUsername();
        int count = employeeService.count(new QueryWrapper<Employee>().eq("username", username));
        if (count > 0){
            return R.error("添加失败，员工已存在!");
        }*/

        // 2、新增员工
        // 2.1、封装数据
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Long empId = (Long) request.getSession().getAttribute("employee");
        // 2.2、新增员工到数据库
        employeeService.save(employee);
        return R.success("新增成功！");
    }

    /**
     * 查询员工
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        // 分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 员工账号 禁用/启用
     * @param request
     * @param employee
     * @return
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        Long empId = (Long) request.getSession().getAttribute("employee");
        employeeService.updateById(employee);
        return R.success("操作成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        if (emp != null){
            R.error("操作失败！");
        }
        return R.success(emp);
    }
}
