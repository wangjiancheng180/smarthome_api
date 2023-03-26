package com.wjc.smarthome.service.system;


import com.wjc.smarthome.dto.system.SysResourceDto;
import com.wjc.smarthome.dto.system.SysRoleDto;
import com.wjc.smarthome.enetity.system.Role;
import com.wjc.smarthome.param.system.SysRoleCreateBean;
import com.wjc.smarthome.param.system.SysRoleUpdateBean;

import java.util.List;
import java.util.Set;

/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
public interface RoleService {

    List<Role> queryRoleList();

    Long createRole(SysRoleCreateBean bean);

    boolean updateRole(SysRoleUpdateBean bean);

    SysRoleDto queryRoleById(Long id);

    boolean deleteRole(Long id);

    Set<SysResourceDto> getResourceDtos(Set<Long> roleIds);
}
