package com.wjc.smarthome.dto.system;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/7--22:58
 */
@ApiModel("资源返回类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SysResourceDto {

    @ApiModelProperty("资源id")
    private Long id;

    @ApiModelProperty("父级资源id集合")
    private List<Long> parentIds;


    @ApiModelProperty("父级资源id")
    private Long parentId;

    /**
     * 层级，1：代表最高级，2代表1级的下级，依次类推
     */
    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 类型 1：菜单 2：按钮
     */
    @ApiModelProperty("类型 1：菜单 2：按钮")
    private Integer type;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("资源key，当用作权限的时候现在率改成类似 user:add，user:update")
    private String sourceKey;

    @ApiModelProperty("资源路径")
    private String sourceUrl;
}
