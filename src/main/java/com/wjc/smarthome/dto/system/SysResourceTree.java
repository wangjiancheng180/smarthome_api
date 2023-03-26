package com.wjc.smarthome.dto.system;


import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author 王建成
 * @date 2022/4/1--15:34
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResourceTree extends BaseEnetity {
    private Long id;

    private Integer level;

    private String name;

    private Integer sort;

    /**
     * 类型 1：菜单 2：按钮
     */
    @ApiModelProperty("类型 1：菜单 2：按钮")
    private Integer type;

    @ApiModelProperty("图标")
    private String icon;


    private String sourceKey;

    private String sourceUrl;

    private List<SysResourceTree> children;



    public SysResourceTree(Long id, Integer level, String name, Integer sort, Integer type, String icon, String sourceKey, String sourceUrl, List<SysResourceTree> children, Long createUserId, Long updateUserId, String createUserName, String updateUserName, LocalDateTime createTime, LocalDateTime updateTime) {
        super(createUserId, updateUserId, createUserName, updateUserName, createTime, updateTime);
        this.id = id;
        this.level = level;
        this.name = name;
        this.sort = sort;
        this.type = type;
        this.icon = icon;
        this.sourceKey = sourceKey;
        this.sourceUrl = sourceUrl;
        this.children = children;
    }
}
