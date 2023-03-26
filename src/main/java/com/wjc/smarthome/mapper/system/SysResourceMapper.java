package com.wjc.smarthome.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.wjc.smarthome.enetity.system.SysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
public interface SysResourceMapper extends BaseMapper<SysResource> {

    /**
     * 删除角色与资源的关联，通过资源id删除
     * @param resourceId
     */
    void removeRoleContactResource(@Param("resource_id") Long resourceId);

    Set<String> queryButtonKey(@Param("userId") long userId);

}