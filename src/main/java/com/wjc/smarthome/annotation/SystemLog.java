package com.wjc.smarthome.annotation;


import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;

import java.lang.annotation.*;

/**
 * @author 王建成
 * @date 2023/2/25--15:28
 * 日志标记
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String module() default "";

    String methods() default "";

    /**
     * 日志类型,0操作日志,1登录日志
     *
     * @return
     */
    LogType logType();

    /**
     * 操作类型,0:查询,1:添加,2:修改,3:删除,4:导出
     *
     * @return
     */
    OperateType operateType();
}
