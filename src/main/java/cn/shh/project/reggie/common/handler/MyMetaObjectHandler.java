package cn.shh.project.reggie.common.handler;

import cn.shh.project.reggie.util.BaseContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatisPlus 公共字段自动填充
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
