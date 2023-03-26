package com.wjc.smarthome.param.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 王建成
 * @date 2023/2/22--22:09
 */
@Data
@ApiModel("登录信息")
public class LoginInfo {
    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    private String password;
}
