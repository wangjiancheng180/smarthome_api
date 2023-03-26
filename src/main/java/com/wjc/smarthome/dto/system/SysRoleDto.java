package com.wjc.smarthome.dto.system;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/12--9:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色的返回类")
public class SysRoleDto {
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

    @ApiModelProperty("选择器数值绑定")
    private List<List<Long>> resourceModelIds;

    @ApiModelProperty("绑定的资源树")
    private List<SysResourceTree> resourceTrees;

    @ApiModelProperty("关联资源集合")
    private List<SysResourceDto> resourceList;

}
