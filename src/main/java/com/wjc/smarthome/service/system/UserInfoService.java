package com.wjc.smarthome.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjc.smarthome.dto.system.AuthInfo;
import com.wjc.smarthome.dto.system.UserInfoDto;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.param.system.UserInfoCreateBean;
import com.wjc.smarthome.param.system.UserInfoQueryBean;


import java.util.List;

/**
 * @author 王建成
 * @date 2022/3/17--13:28
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 这个权限认证在用
     * @param username
     * @return
     */
    UserInfo findByUserName(String username);

    IPage<UserInfo> pageList(UserInfoQueryBean bean);

    AuthInfo queryByUsername(String username);

//    String getAuthorityList(AuthInfo userInfo);

    List<UserInfoDto> queryUserInfo();

    boolean createUserInfo(UserInfoCreateBean bean);

    boolean updateUserInfo(UserInfoCreateBean bean);

    boolean deleteUserInfo(Long id);

    UserInfoDto queryUserById(Long id);

//    void updteByRoleId(Long roleId);
}
