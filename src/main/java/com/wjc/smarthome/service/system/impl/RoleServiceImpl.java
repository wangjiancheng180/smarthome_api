package com.wjc.smarthome.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wjc.smarthome.dto.system.SysResourceDto;
import com.wjc.smarthome.dto.system.SysRoleDto;
import com.wjc.smarthome.enetity.system.Role;
import com.wjc.smarthome.enetity.system.SysResource;
import com.wjc.smarthome.mapper.system.RoleMapper;
import com.wjc.smarthome.param.system.SysRoleCreateBean;
import com.wjc.smarthome.param.system.SysRoleUpdateBean;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.service.system.RoleService;
import com.wjc.smarthome.service.system.SysResourceService;
import com.wjc.smarthome.service.system.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王建成
 * @date 2022/3/17--11:25
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    private final SysResourceService resourceService;

    private final RedisService redisService;


    @Override
    public List<Role> queryRoleList() {
        return list();
    }

    @Transactional
    @Override
    public Long createRole(SysRoleCreateBean bean) {
        Role role = new Role();
        role.setName(bean.getName());
        role.setRoleKey(bean.getRoleKey());
        role.setStatus(bean.getStatus());
        role.setSort(bean.getSort());
        role.setDescription(bean.getDescription());
        role.setCreateUserId(bean.getCreateUserId());
        role.setCreateUserName(bean.getCreateUserName());
        role.setCreateTime(bean.getCreateTime());
        if (save(role)) {
            if (CollUtil.isNotEmpty(bean.getResourceIds())) {
                roleMapper.contactResourceIds(role.getId(), bean.getResourceIds());
            }
            return role.getId();
        }
        return -1L;
    }

    @Transactional
    @Override
    public boolean updateRole(SysRoleUpdateBean bean) {
        Role role = new Role();
        role.setId(bean.getId());
        role.setName(bean.getName());
        role.setRoleKey(bean.getRoleKey());
        role.setStatus(bean.getStatus());
        role.setSort(bean.getSort());
        role.setDescription(bean.getDescription());
        role.setUpdateUserId(bean.getUpdateUserId());
        role.setUpdateUserName(bean.getUpdateUserName());
        role.setUpdateTime(bean.getUpdateTime());
        if (updateById(role)) {
            //这里需要判断一下关联关系是否改变了先查
            List<Long> resourceIds = roleMapper.queryContactResourceIds(role.getId());
            if (CollUtil.isNotEmpty(resourceIds) && CollUtil.isNotEmpty(bean.getResourceIds())) {
                String s1 = resourceIds.stream().sorted().map(s -> String.valueOf(s)).collect(Collectors.joining());
                String s2 = bean.getResourceIds().stream().sorted().map(s -> String.valueOf(s)).collect(Collectors.joining());
                if (!s1.equals(s2)) {
                    //先将所有与角色关联的资源关联信息删除
                    roleMapper.removeRoleContactResource(role.getId());
                    //重新添加关联资源信息
                    roleMapper.contactResourceIds(role.getId(), bean.getResourceIds());
                    //删除user的缓存信息让用户重新登录
                    List<String> usrnames = roleMapper.getUsernameByRoleId(role.getId());
                    usrnames.stream().map(e -> {
                        //删除authinfo的缓存信息即可没必要去添加回去
                        redisService.delete("user:auth", e);
                        //并且将token也删除让他们重新登录
                        redisService.delete(StrUtil.format("token:{}", e));
                        return e;

                    }).close();

//                 userInfoService.updteByRoleId(role.getId());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public SysRoleDto queryRoleById(Long id) {
        SysRoleDto sysRoleDto = roleMapper.queryRoleById(id);
        List<SysResourceDto> resourceDtos = sysRoleDto.getResourceList();
        //获取所有资源
        List<SysResource> sysResources = resourceService.resourceList();
        if (CollUtil.isNotEmpty(resourceDtos)) {
            //结果集合，当关联资源不为空时创建list用来存放资源的所有父级id
            List<List<Long>> modelIds = new ArrayList<>();
            //结构集合，用来存放关联资源的树型结构
//            List<SysResourceTree> resourceTrees = new ArrayList<>();
            //中间结果集合
            List<Long> modelId = new ArrayList<>();
            //用集合来收集所有关联资源的根资源id，以便后续将他们组成树形结构
            Set<Long> rootIds = new HashSet<>();
            for (SysResourceDto resourceDto : resourceDtos
            ) {
                //这里要把关联的元素先放到集合里
                modelId.add(resourceDto.getId());
                resourceService.findParentIds(resourceDto.getId(), sysResources, modelId);
                //要将结果倒叙符合前端需要
                Collections.reverse(modelId);
                //将根资源id加入结果集
                rootIds.add(modelId.get(0));
                //加入结果集合
                modelIds.add(modelId);
                //清空中间结果
                modelId = new ArrayList<>();
            }

//            for (Long rooId: rootIds
//                 ) {
//                for (SysResource resource:sysResources
//                     ) {
//                    if (rooId.equals(resource.getId())){
//                        resourceTrees.add(resourceService.toTree(resource));
//                        break;
//                    }
//                }
//            }
            //关联资源组成树型结构
//            resourceService.combinationTree(resourceTrees,resourceDtos);
            //将最终结果加入role中
            sysRoleDto.setResourceModelIds(modelIds);
//            sysRoleDto.setResourceTrees(resourceTrees);
        }

        return sysRoleDto;
    }

    @Override
    public boolean deleteRole(Long id) {
        int count = roleMapper.countUser(id);
        if (count > 0) {
            //角色还关联这人就不允许删除
            return false;
        }
        //先删除关联的资源
        roleMapper.removeRoleContactResource(id);

        return removeById(id);
    }

    /**
     * 获取集合角色中的所有菜单类型的资源
     *
     * @param roleIds
     * @return
     */
    @Override
    public Set<SysResourceDto> getResourceDtos(Set<Long> roleIds) {
        Set<SysResourceDto> resourceDtos;
        resourceDtos = roleMapper.queryMenu(roleIds);
        return resourceDtos;

    }


}
