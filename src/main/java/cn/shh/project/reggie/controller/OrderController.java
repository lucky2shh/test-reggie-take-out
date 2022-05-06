package cn.shh.project.reggie.controller;

import cn.shh.project.reggie.util.R;
import cn.shh.project.reggie.pojo.Orders;
import cn.shh.project.reggie.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number,
                          String beginTime, String endTime){
        Page<Orders> orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null, Orders::getId, number);
        queryWrapper.between((beginTime != null && endTime != null), Orders::getOrderTime, beginTime, endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(orderPage, queryWrapper);
        return R.success(orderPage);
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        Page<Orders> orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(orderPage, queryWrapper);
        return R.success(orderPage);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders){
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(orders.getStatus() != null, Orders::getStatus, orders.getStatus());
        updateWrapper.eq(orders.getId() != null, Orders::getId, orders.getId());
        orderService.update(updateWrapper);
        return R.success("操作成功!");
    }
}
