package com.wjc.smarthome.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wjc.smarthome.dto.system.*;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.mapper.system.UserInfoMapper;
import com.wjc.smarthome.param.system.UserInfoCreateBean;
import com.wjc.smarthome.param.system.UserInfoQueryBean;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.service.system.RoleService;
import com.wjc.smarthome.service.system.SysResourceService;
import com.wjc.smarthome.service.system.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王建成
 * @date 2022/3/17--13:29
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {


    private final UserInfoMapper userInfoMapper;


    private final RoleService roleService;


    private final SysResourceService resourceService;

    @Value("${security.salt}")
    private String salt;


    private final RedisService redisService;


    @Override
    public UserInfo findByUserName(String username) {
        //先去redis找
        UserInfo authInfo = redisService.get("user:info", username, UserInfo.class);
        authInfo = Optional.ofNullable(authInfo).orElseGet(()->getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username)));
        redisService.save("user:info", username, authInfo);
        return authInfo;
    }

    @Override
    public IPage<UserInfo> pageList(UserInfoQueryBean bean) {
        IPage<UserInfo> pageInfo = new Page<>(bean.getPage(),bean.getSize());
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(UserInfo::getUsername,bean.getUsername());
        queryWrapper.like(UserInfo::getRealName,bean.getRealName());
        queryWrapper.like(UserInfo::getPhone,bean.getPhone());
        queryWrapper.eq(UserInfo::getSex,bean.getSex());
        queryWrapper.eq(UserInfo::getStatus,bean.getStatus());
        return page(pageInfo,queryWrapper);
    }

    @Override
    public AuthInfo queryByUsername(String username) {
        AuthInfo authInfo = redisService.get("user:auth", username, AuthInfo.class);
        authInfo = Optional.ofNullable(authInfo).orElseGet(() -> userInfoMapper.queryByUsername(username));
        authInfo = Optional.ofNullable(authInfo).map(e -> {
            if (CollUtil.isNotEmpty(e.getRoleDtos())) {
                Set<Long> roleIds = e.getRoleDtos().stream().map(SysRoleDto::getId).collect(Collectors.toSet());
                //这里需要获取所有角色绑定的菜单，去重
                Set<SysResourceDto> sysResourceDtos = roleService.getResourceDtos(roleIds);
                //将菜单转换成树型结构
                List<SysResourceTree> sysResourceTrees = resourceService.combinationTree(sysResourceDtos);
                sysResourceTrees = sysResourceTrees.stream().sorted(Comparator.comparing(SysResourceTree::getSort)).collect(Collectors.toList());
                e.setResourceTrees(sysResourceTrees);
            }
            return e;
        }).orElse(null);
        redisService.save("user:auth",username,authInfo);
        return authInfo;
    }


    @Override
    public List<UserInfoDto> queryUserInfo() {

        return userInfoMapper.queryUserInfo();
    }

    @Override
    @Transactional
    public boolean createUserInfo(UserInfoCreateBean bean) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUsername,bean.getUsername());
        if (getOne(queryWrapper)!=null){
            return false;
        }
        queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone,bean.getPhone());
        if (getOne(queryWrapper)!=null){
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(bean.getUsername());
        userInfo.setNickName(bean.getNickName());
        userInfo.setRealName(bean.getRealName());
        Md5Hash md5Hash = new Md5Hash(bean.getPassword(),salt,3);
        userInfo.setPassword(md5Hash.toHex());
        userInfo.setPhone(bean.getPhone());
        userInfo.setSex(bean.getSex());
        userInfo.setStatus(bean.getStatus());
        userInfo.setCreateUserId(bean.getCreateUserId());
        userInfo.setCreateUserName(bean.getCreateUserName());
        userInfo.setCreateTime(bean.getCreateTime());
        if (save(userInfo)){
            userInfoMapper.createUserRelationRole(userInfo.getId(),bean.getRoleIds());
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateUserInfo(UserInfoCreateBean bean) {
        if (getById(bean.getId())==null){
            return false;
        }
        UserInfo userInfo1 = getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, bean.getUsername()).ne(UserInfo::getId, bean.getId()));
        if (userInfo1!=null){
            return false;
        }
        userInfo1 = baseMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, bean.getPhone()).ne(UserInfo::getId, bean.getId()));
        if (userInfo1!=null){
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(bean.getId());
        userInfo.setUsername(bean.getUsername());
        userInfo.setNickName(bean.getNickName());
        userInfo.setRealName(bean.getRealName());
        if (StrUtil.isNotBlank(bean.getPassword())){
            Md5Hash md5Hash = new Md5Hash(bean.getPassword(),salt,3);
            userInfo.setPassword(md5Hash.toHex());
        }
        userInfo.setPhone(bean.getPhone());
        userInfo.setSex(bean.getSex());
        userInfo.setStatus(bean.getStatus());
        userInfo.setUpdateUserId(bean.getUpdateUserId());
        userInfo.setUpdateUserName(bean.getUpdateUserName());
        userInfo.setUpdateTime(bean.getUpdateTime());
        if (updateById(userInfo)){
            userInfoMapper.removeUserRelationRole(bean.getId());
            userInfoMapper.createUserRelationRole(bean.getId(),bean.getRoleIds());
            return true;
        }
//        AuthInfo authInfo = queryByUsername(bean.getUsername());
        //这里直接删除redis里的缓存用户信息
        redisService.delete("user:info",userInfo.getUsername());
        redisService.delete("user:auth",userInfo.getUsername());
        redisService.delete(StrUtil.format("token:{}",userInfo.getUsername()));
        redisService.delete("user:resource:button",String.valueOf(userInfo.getId()));
        return false;
    }

    @Transactional
    @Override
    public boolean deleteUserInfo(Long id) {
        UserInfo user = getById(id);
        if (user==null){
            return false;
        }
        removeById(id);
        userInfoMapper.removeUserRelationRole(id);
        //删除redis中的缓存
        redisService.delete("user:info",user.getUsername());
        redisService.delete("user:auth",user.getUsername());
        redisService.delete(StrUtil.format("token:{}",user.getUsername()));
        redisService.delete("user:resource:button",String.valueOf(user.getId()));
        return true;
    }

    @Override
    public UserInfoDto queryUserById(Long id) {
        return userInfoMapper.queryUserById(id);
    }

//    /**
//     *角色改变时删除缓存信息
//     * @param roleId
//     */
//    @Override
//    public void updteByRoleId(Long roleId) {
//        //获取跟roleId绑定的所有用户
//        List<AuthInfo> authInfos = userInfoMapper.updteByRoleId(roleId);
//
//        for (AuthInfo authInfo: authInfos
//             ) {
//            //删除authinfo的缓存信息即可没必要去添加回去
//            redisService.delete("user:auth",authInfo.getUsername());
//            //并且将token也删除让他们重新登录
//            redisService.delete(StrUtil.format("token:{}",authInfo.getUsername()));
//        }
//
//    }
}
