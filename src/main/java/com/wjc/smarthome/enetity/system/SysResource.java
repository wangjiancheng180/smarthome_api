package com.wjc.smarthome.enetity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.wjc.smarthome.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class SysResource extends BaseEnetity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    /**
    * 层级，1：代表最高级，2代表1级的下级，依次类推
    */
    private Integer level;

    private String name;
    /**
     * 类型 1：菜单 2：按钮
     */
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    private Integer sort;

    private String sourceKey;

    private String sourceUrl;
}