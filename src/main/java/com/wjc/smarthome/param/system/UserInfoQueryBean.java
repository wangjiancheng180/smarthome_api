package com.wjc.smarthome.param.system;


import com.wjc.smarthome.common.PageRequestInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王建成
 * @date 2022/4/17--22:50
 */
@ApiModel("用户查询条件")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoQueryBean extends PageRequestInfo {

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

    /**
     * 1:启用，2禁用
     */
    @ApiModelProperty("状态 1:启用 2：禁用")
    private Integer status;


}
