package com.wjc.smarthome.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wjc.smarthome.dto.system.SysResourceDto;
import com.wjc.smarthome.dto.system.SysResourceTree;
import com.wjc.smarthome.enetity.system.SysResource;
import com.wjc.smarthome.mapper.system.SysResourceMapper;
import com.wjc.smarthome.param.system.SysResourceCreateBean;
import com.wjc.smarthome.param.system.SysResourceUpdateBean;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.service.system.SysResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
@Service
@RequiredArgsConstructor
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    private final SysResourceMapper sysResourceMapper;

    private final RedisService redisService;

    @Override
    public List<SysResourceTree> queryResourceTree() {
        //先获取所有的资源
        List<SysResource> list = list();
        //拿到根节点树
        List<SysResourceTree> treeRoot = findTreeRoot(list);
        //将list组装到树上去
        resourceOnTree(treeRoot,list);
        return treeRoot;
    }

    @Override
    public Long createResource(SysResourceCreateBean bean) {
        SysResource sysResource = new SysResource();
        sysResource.setParentId(bean.getParentId());
        sysResource.setName(bean.getName());
        sysResource.setLevel(bean.getLevel());
        sysResource.setSort(bean.getSort());
        sysResource.setType(bean.getType());
        sysResource.setIcon(bean.getIcon());
        sysResource.setSourceKey(bean.getSourceKey());
        sysResource.setSourceUrl(bean.getSourceUrl());
        sysResource.setCreateTime(bean.getCreateTime());
        sysResource.setCreateUserId(bean.getCreateUserId());
        sysResource.setCreateUserName(bean.getCreateUserName());
        if (save(sysResource)){
            return sysResource.getId();
        }
        return new Long(-1);
    }

    @Override
    public SysResourceDto queryResourceById(Long id) {
//        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysResource::getId,id);
        SysResource resource = baseMapper.selectById(id);
        if (resource==null){
            return null;
        }
        SysResourceDto sysResourceDto = new SysResourceDto(resource.getId(), null,resource.getParentId(), resource.getLevel(), resource.getName(), resource.getSort(),resource.getType(), resource.getIcon(),resource.getSourceKey(), resource.getSourceUrl());
        if (resource.getParentId()==0){
            return sysResourceDto;
        }
        List<SysResource> list = list();

        List<Long> parentIds = new ArrayList<>();
        findParentIds(resource.getId(),list,parentIds);
        //将父辈id全部倒序
        Collections.reverse(parentIds);
        //将父元素id集合放到返回对象中
        sysResourceDto.setParentIds(parentIds);
        return sysResourceDto;
    }

    @Override
    public boolean updateResource(SysResourceUpdateBean bean) {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysResource::getId,bean.getId());
        SysResource resource = new SysResource(bean.getId(), bean.getParentId(), bean.getLevel(), bean.getName(),bean.getType(), bean.getIcon(),bean.getSort(), bean.getSourceKey(), bean.getSourceUrl());
        resource.setUpdateTime(bean.getUpdateTime());
        resource.setUpdateUserId(bean.getUpdateUserId());
        resource.setUpdateUserName(bean.getUpdateUserName());
//        if(update(resource,queryWrapper)){
//            //这里要更改子节点资源的的层级
//           SysResource sysResource = new SysResource();
//           //层级是被修改的父元素层级+1
//           sysResource.setLevel(bean.getLevel()+1);
//           update(sysResource,new LambdaQueryWrapper<SysResource>().eq(SysResource::getParentId,bean.getId()));
//        }
        return update(resource,queryWrapper);
    }



    /**
     * 寻找所有的父类id
     * @param id
     * @param list
     * @param parentIds
     */
    @Override
    public void findParentIds(Long id, List<SysResource> list, List<Long> parentIds) {

        for (SysResource resource:list
             ) {
            if (resource.getId().equals(id)){
                Long parentId = resource.getParentId();
                //当父元素id是0就是顶级资源再没有父级资源直接return
                if (parentId==0){
                    return;
                }
                //将父元素id放进集合
                parentIds.add(parentId);
                //去寻找父元素的父id
                findParentIds(parentId,list,parentIds);

            }
        }

    }

    @Override
    public List<SysResource> resourceList() {
        return list();
    }

    /**
     * 转换树型结构
     * @param resourceDto
     * @return
     */
    @Override
    public SysResourceTree toTree(SysResourceDto resourceDto) {
        return new SysResourceTree(
                resourceDto.getId(),
                resourceDto.getLevel(),
                resourceDto.getName(),
                resourceDto.getSort(),
                resourceDto.getType(),
                resourceDto.getIcon(),
                resourceDto.getSourceKey(),
                resourceDto.getSourceUrl(),
                null
                );
    }

    /**
     * 组装树
     * @param treeRoot 各个树的节点
     * @param list 所有子节点
     */
    private void resourceOnTree(List<SysResourceTree> treeRoot, List<SysResource> list) {

        for (SysResourceTree tree: treeRoot
             ) {
            //这里给树新建一个子集
            tree.setChildren(new ArrayList<>());
            //这里返回的是一个iterator是一个new出来的对象,所以每次的iteator对象都是不同的
            Iterator<SysResource> iterator = list.iterator();
            while (iterator.hasNext()){
                //这里一定要用一个变量接收，iterator.next()会返回迭代器的下一个元素，并且更新迭代器的状态。
                SysResource resource = iterator.next();
                if (resource.getParentId().equals(tree.getId())){
                    tree.getChildren().add(toTree(resource));
                    //因为这里我想把list中加入tree中的资源删除所以用了迭代器，不能只foreach中对List中的元素进行添加或删除
                    //iterator.remove();
                }

        }
            //list中的元素被取完了直接return
            if (CollUtil.isEmpty(list)){
                return;
            }
            //只有树有子节点才能再去去寻找子节点
            if (CollUtil.isNotEmpty(tree.getChildren())){
                resourceOnTree(tree.getChildren(),list);
            }
        }
    }

    /**
     * 将
     * @param list
     * @return
     */
    public List<SysResourceTree> findTreeRoot(List<SysResource> list){
        //定义一个资源树的结果集
        List<SysResourceTree> trees = new ArrayList<>();
        Iterator<SysResource> iterator = list.iterator();
        while (iterator.hasNext()){
            SysResource resource = iterator.next();
            if(resource.getParentId()==0){
                trees.add(toTree(resource));
                //放进树里的就删掉
                //iterator.remove();
            }
        }

        return trees;
    }

    public List<SysResourceTree> findTreeRoot(Set<SysResourceDto> resourceDtos){
        List<SysResourceTree> trees = new ArrayList<>();
        Iterator<SysResourceDto> iterator = resourceDtos.iterator();
        while (iterator.hasNext()){
           SysResourceDto sysResourceDto = iterator.next();
            if(sysResourceDto.getParentId()==0){
                trees.add(toTree(sysResourceDto));
                //放进树里的就删掉
                //iterator.remove();
            }
        }
        return trees;
    }

    /**
     * resource转换成树结构
     * @param resource
     * @return
     */
    @Override
    public SysResourceTree toTree(SysResource resource) {

        return new SysResourceTree(
                resource.getId(),
                resource.getLevel(),
                resource.getName(),
                resource.getSort(),
                resource.getType(),
                resource.getIcon(),
                resource.getSourceKey(),
                resource.getSourceUrl(),
               null,
                resource.getCreateUserId(),
                resource.getUpdateUserId(),
                resource.getCreateUserName(),
                resource.getUpdateUserName(),
                resource.getCreateTime(),
                resource.getUpdateTime()
                );
    }

    @Override
    public void combinationTree(List<SysResourceTree> resourceTrees, List<SysResourceDto> resourceDtos) {
        List<SysResource> sysResources = new ArrayList<>();
        for (SysResourceDto resourceDto : resourceDtos
                ) {
            sysResources.add(new SysResource(
                    resourceDto.getId(),
                    resourceDto.getParentId(),
                    resourceDto.getLevel(),
                    resourceDto.getName(),
                    resourceDto.getType(),
                    resourceDto.getIcon(),
                    resourceDto.getSort(),
                    resourceDto.getSourceKey(),
                    resourceDto.getSourceUrl()
                    ));
        }
        resourceOnTree(resourceTrees,sysResources);
    }

    @Override
    public List<SysResourceTree> combinationTree(Set<SysResourceDto> resourceDtos) {

        List<SysResourceTree> trees = findTreeRoot(resourceDtos);
        resourceDtoOnTree(trees,resourceDtos);
        return trees;
    }

    private void resourceDtoOnTree(List<SysResourceTree> trees, Set<SysResourceDto> resourceDtos) {
        for (SysResourceTree tree:trees
             ) {
            List<SysResourceTree> childern = new ArrayList<>();
            Iterator<SysResourceDto> iterator = resourceDtos.iterator();
            while (iterator.hasNext()){
                SysResourceDto resourceDto = iterator.next();
                if (resourceDto.getParentId().equals(tree.getId())){
                   childern.add(toTree(resourceDto));
                }
            }
            tree.setChildren(childern);
            if (CollUtil.isEmpty(resourceDtos)){
                break;
            }
            if (CollUtil.isNotEmpty(childern)){
                resourceDtoOnTree(childern,resourceDtos);
            }
        }
    }

    @Override
    public boolean deleteResource(Long id) {
        //在删除前要先判断该资源是否有子资源，有的话不允许直接删除
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysResource::getParentId,id);

        List<SysResource> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)){
            return false;
        }
        //第一步要将角色关联资源的信息删除
        sysResourceMapper.removeRoleContactResource(id);
        return removeById(id);
    }

    @Override
    public Set<String> queryButtonKey(long userId) {
        List<String> stringList= redisService.getList("user:resource:button", String.valueOf(userId), String.class);
        if (CollUtil.isNotEmpty(stringList)){
            return stringList.stream().collect(Collectors.toSet());
        }

        Set<String> strings = sysResourceMapper.queryButtonKey(userId);
        redisService.save("user:resource:button", String.valueOf(userId),strings);
        return strings;
    }

}
