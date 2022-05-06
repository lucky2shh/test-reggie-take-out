package cn.shh.project.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.shh.project.reggie.pojo.ShoppingCart;
import cn.shh.project.reggie.mapper.ShoppingCartMapper;
import cn.shh.project.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
