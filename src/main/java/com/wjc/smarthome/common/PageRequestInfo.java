package com.wjc.smarthome.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 王建成
 * @date 2022/4/17--22:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestInfo implements Serializable {

    @ApiModelProperty(value = "页数", required = true)
    /**
     *@NotNull(message = "页码不能为空")
     */
    //@DecimalMax(value = "20",message = "")
    @DecimalMin(value = "1",message = "最小页1页")
    @NotNull(message = "页码不能为空")
    private Integer page;

    /**
     * 每页最大返回20条数据
     * @NotNull(message = "每页显示条数不能为空")
     */
    @ApiModelProperty(value = "每页显示条数", required = true)
    @DecimalMax(value = "20",message = "每页最多返回20条数据")
    @DecimalMin(value = "1",message = "每页最少返回1条数据")
    @NotNull(message = "每页显示条数不能为空")
    private Integer size;
}
