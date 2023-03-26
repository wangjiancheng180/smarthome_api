package com.wjc.smarthome.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @Author: wjc
 * @Date: 2023-03-11 10:50
 **/
@Slf4j
public class CreateAndUpdateMetaHandler implements MetaObjectHandler {

    @Autowired
    private RedisService redisService;
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        try {
            //填充用户
            String username =  SecurityUtils.getSubject().getPrincipal().toString();
            UserInfo userInfo = redisService.get("user:info", username, UserInfo.class);
            if(ObjectUtil.isNotNull(userInfo)){
                this.strictInsertFill(metaObject, "createUserId", Long.class, userInfo.getId());
                this.strictInsertFill(metaObject, "createUserName", String.class, userInfo.getRealName());
            }
        }catch (Exception e){
            log.error("填充失败:{}","创建人创建用户填充失败");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        try {
            //填充用户
            String username =  SecurityUtils.getSubject().getPrincipal().toString();
            UserInfo userInfo = redisService.get("user:info", username, UserInfo.class);
            if(ObjectUtil.isNotNull(userInfo)){
                this.strictUpdateFill(metaObject, "updateUserId",  Long.class, userInfo.getId());
                this.strictUpdateFill(metaObject, "updateUserName", String.class, userInfo.getRealName());
            }
        }catch (Exception e){
            log.info("填充失败:{}",e.getMessage());
        }
    }
}
