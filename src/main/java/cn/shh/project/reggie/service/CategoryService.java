package cn.shh.project.reggie.service;

import cn.shh.project.reggie.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    public void removeById(Long id);
}
