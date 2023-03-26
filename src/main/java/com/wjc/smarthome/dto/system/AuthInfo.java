package com.wjc.smarthome.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author 王建成
 * @date 2022/4/19--10:57
 */
@ApiModel("用户类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名用来登录")
    private String username;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("性别，002：男，003：女")
    private String sex;

    @ApiModelProperty("手机号码")
    private String phone;

//    @ApiModelProperty("密码")
//    private String password;


    @ApiModelProperty("状态 1:启用 2：禁用")
    private Integer status;

    @ApiModelProperty("用户角色")
    private List<SysRoleDto> roleDtos;

    @ApiModelProperty("用户所有菜单")
    private List<SysResourceTree> resourceTrees;

//    @ApiModelProperty("权限资源")
//    private Set<SysResourceDto> resourceDtos;

//    @ApiModelProperty("权限信息")
//    private String authorities;
}
