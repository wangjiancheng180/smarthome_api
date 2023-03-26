package com.wjc.smarthome.param.system;

import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/14--9:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色更新类")
public class SysRoleUpdateBean extends BaseEnetity {

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

    @ApiModelProperty("关联资源Id集合")
    private List<Long> resourceIds;
}
