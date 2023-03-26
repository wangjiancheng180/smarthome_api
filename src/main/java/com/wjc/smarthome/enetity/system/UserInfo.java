package com.wjc.smarthome.enetity.system;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author 王建成
* @date 2022/3/16--22:37
*/     
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户主题类")
public class UserInfo extends BaseEnetity implements Serializable {

    @ApiModelProperty("用户id")
    @TableId(type = IdType.AUTO)
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

//
//    @ApiModelProperty("角色列表")
//    private List<Role> roles;
}