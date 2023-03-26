package com.wjc.smarthome.service.system;



import com.wjc.smarthome.dto.system.SysResourceDto;
import com.wjc.smarthome.dto.system.SysResourceTree;
import com.wjc.smarthome.enetity.system.SysResource;
import com.wjc.smarthome.param.system.SysResourceCreateBean;
import com.wjc.smarthome.param.system.SysResourceUpdateBean;

import java.util.List;
import java.util.Set;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
public interface SysResourceService{


    List<SysResourceTree> queryResourceTree();

    Long createResource(SysResourceCreateBean bean);

    SysResourceDto queryResourceById(Long id);

    boolean updateResource(SysResourceUpdateBean bean);

    void findParentIds(Long id, List<SysResource> list, List<Long> parentIds);

    List<SysResource> resourceList();

    SysResourceTree toTree(SysResourceDto sysResourceDto);

    SysResourceTree toTree(SysResource resource);

    void combinationTree(List<SysResourceTree> resourceTrees, List<SysResourceDto> resourceDtos);

    List<SysResourceTree> combinationTree(Set<SysResourceDto> resourceDtos);

    boolean deleteResource(Long id);

    /**
     * 根据用户名返回所有授权的资源key
     * @param userId
     * @return
     */
    Set<String> queryButtonKey(long userId);
}
