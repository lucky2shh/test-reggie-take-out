package cn.shh.project.reggie.service;

import cn.shh.project.reggie.pojo.User;
import cn.shh.project.reggie.util.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    R<String> sendCode(String phone, HttpSession session);
}
