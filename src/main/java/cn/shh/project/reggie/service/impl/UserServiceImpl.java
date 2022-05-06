package cn.shh.project.reggie.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.shh.project.reggie.util.R;
import cn.shh.project.reggie.util.RegexUtils;
import cn.shh.project.reggie.util.ReggieConstant;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.shh.project.reggie.pojo.User;
import cn.shh.project.reggie.mapper.UserMapper;
import cn.shh.project.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

import static cn.shh.project.reggie.util.ReggieConstant.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R<String> sendCode(String phone, HttpSession session) {
        // 1、验证手机号
        if (RegexUtils.isPhoneInvalid(phone)){
            return R.error("手机号码格式错误！");
        }
        // 2、生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 2、保存验证码到session
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code,
                LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 3、发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
        return R.success("发送成功");
    }
}
