package com.wjc.smarthome.param.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王建成
 * @date 2022/4/4--23:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("新建资源bean")
public class SysResourceCreateBean extends BaseEnetity {

        @TableId(type = IdType.AUTO)
        @ApiModelProperty("id")
        private Long id;

        @ApiModelProperty("父id")
        private Long parentId;

        /**
         * 层级，1：代表最高级，2代表1级的下级，依次类推
         */
        @ApiModelProperty("层级")
        private Integer level;

        @ApiModelProperty("名称")
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

        @ApiModelProperty("资源key")
        private String sourceKey;

        @ApiModelProperty("资源路径")
        private String sourceUrl;
}
