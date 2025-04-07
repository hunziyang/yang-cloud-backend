package com.yang.portal.core.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yang.portal.core.CoreConstant;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@EnableTransactionManagement
@MapperScan("com.yang.portal.**.mapper")
public class MybatisPlusConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CoreConstant.CREATED_TIME, ZonedDateTime.class, ZonedDateTime.now(ZoneId.systemDefault()));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, CoreConstant.UPDATED_TIME, ZonedDateTime.class, ZonedDateTime.now(ZoneId.systemDefault()));
    }
}
