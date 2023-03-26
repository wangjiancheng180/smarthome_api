package com.wjc.smarthome.enetity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色基础返回类")
public class Role extends BaseEnetity {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    /**
    * 角色名称 普通用户，超级管理员，运营人员
    */
    @ApiModelProperty("角色名称")
    private String name;

    /**
    * 角色key role_user role_admin 

    */
    @ApiModelProperty("角色key")
    private String roleKey;

    /**
    * 状态 1：开启 0：禁用
    */
    @ApiModelProperty("状态，1：启用，0：禁用")
    private Integer status;

    @ApiModelProperty("排序")
    private Integer sort;

    /**
    * 描述
    */
    @ApiModelProperty("描述")
    private String description;

}