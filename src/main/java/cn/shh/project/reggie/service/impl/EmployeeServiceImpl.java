package cn.shh.project.reggie.service.impl;

import cn.shh.project.reggie.mapper.EmployeeMapper;
import cn.shh.project.reggie.pojo.Employee;
import cn.shh.project.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
