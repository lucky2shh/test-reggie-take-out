package cn.shh.project.reggie.common.handler;

import cn.shh.project.reggie.common.config.LocalCache;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatisPlus 公共字段自动填充
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    private LocalCache localCache;
    @Override
    public void insertFill(MetaObject metaObject) {
        Long empId = (Long) localCache.get("employeeId");
        Long userId = (Long) localCache.get("userId");
        Long user = empId == null ? userId : empId;
        log.info("插入填充：用户：{}，时间：{}", localCache.get("employeeId"), LocalDateTime.now());
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "createUser", () -> user, Long.class);
        this.strictInsertFill(metaObject, "updateUser", () -> user, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long empId = (Long) localCache.get("employeeId");
        Long userId = (Long) localCache.get("userId");
        Long user = empId == null ? userId : empId;
        log.info("更新填充：用户：{}，时间：{}", localCache.get("employeeId"), LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateUser", () -> user, Long.class);
    }
}
