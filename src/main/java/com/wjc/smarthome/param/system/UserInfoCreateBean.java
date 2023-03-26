package com.wjc.smarthome.param.system;

import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/18--15:21
 */
@Data
@ApiModel("用户创建类")
public class UserInfoCreateBean extends BaseEnetity {
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

    @ApiModelProperty("密码")
    private String password;

    /**
     * 1:启用，2禁用
     */
    @ApiModelProperty("状态 1:启用 2：禁用")
    private Integer status;


    @ApiModelProperty("关联角色")
    private List<Long> roleIds;
}
