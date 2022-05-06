package cn.shh.project.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.shh.project.reggie.pojo.OrderDetail;
import cn.shh.project.reggie.mapper.OrderDetailMapper;
import cn.shh.project.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
