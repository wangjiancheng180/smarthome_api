package com.wjc.smarthome.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.enetity.BaseEnetity;
import com.wjc.smarthome.enetity.system.SysLogInfo;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.service.system.UserInfoService;
import com.wjc.smarthome.util.GetLocalIpUtil;
import com.wjc.smarthome.util.LogManager;
import com.wjc.smarthome.util.LogTaskFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王建成
 * @date 2023/2/25--15:31
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SystemLogAspect {

//    @Autowired
//    private UserInfoService userInfoService;

    private final LogTaskFactory logTaskFactory;

    @Pointcut(value = "@annotation(com.wjc.smarthome.annotation.SystemLog)")
    public void cutService(){}

    /**
     *  Before、After、AfterReturning、AfterThrowing、Around五种通知类型
     *  参数填写切入点方法
     *  通知使用方法详见博客内
     **/
    @Before("cutService()")
    public void before(JoinPoint joinPoint){
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        request.getHeader("")
//        log.info("接口访问前置");
        //获取被代理对象的类
//        Class<?> targetClass = joinPoint.getTarget().getClass();
//        //获取被增强的方法名
//        String methodName = joinPoint.getSignature().getName();
//        //获取方法的参数类型
//        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
//        try {
//            Method method = targetClass.getMethod(methodName, parameterTypes);
//            SystemLog annotation = method.getAnnotation(SystemLog.class);
//            String value = annotation.value();
//            if (StrUtil.isNotBlank(value)){
//                Object[] args = joinPoint.getArgs();
//                for (Object arg :args){
//                    if (arg instanceof BaseEnetity){
//                        ((BaseEnetity) arg).setCreateUserId(null);
//                        ((BaseEnetity) arg).setCreateUserName(null);
//                        ((BaseEnetity) arg).setCreateTime(null);
//                        ((BaseEnetity) arg).setUpdateUserId(null);
//                        ((BaseEnetity) arg).setUpdateUserName(null);
//                        ((BaseEnetity) arg).setUpdateTime(null);
//                        Date now = new Date();
//                        Subject subject = SecurityUtils.getSubject();
//                        String username = (String) subject.getPrincipal();
//                        UserInfo user = userInfoService.findByUserName(username);
//                        if (value.equals("create")){
//                            ((BaseEnetity) arg).setCreateTime(now);
//                            ((BaseEnetity) arg).setCreateUserId(user.getId());
//                            ((BaseEnetity) arg).setCreateUserName(user.getRealName());
//                        }else {
//                            ((BaseEnetity) arg).setUpdateTime(now);
//                            ((BaseEnetity) arg).setUpdateUserId(user.getId());
//                            ((BaseEnetity) arg).setUpdateUserName(user.getRealName());
//                        }
//                    }
//                }
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

//    @After("aop()")
//    public void after(){
//        log.info("后置通知");
//    }
//
//    @AfterReturning(pointcut = "aop()",returning = "re")
//    public void afterReturning(Object re){
//        log.info("返回通知" + "---" + re);
//    }
//
//    @AfterThrowing(pointcut = "aop()", throwing = "ex")
//    public void afterThrowing(Exception ex){
//        log.info("异常通知" + "---" + ex.getMessage());
//    }


    @AfterReturning(pointcut = "cutService()", returning = "res")
    public void recordOperLog(JoinPoint joinPoint, Object res) {
        handle(joinPoint, null);
    }

    @AfterThrowing(pointcut = "cutService()", throwing = "e")
    public void recordExceptionLog(JoinPoint joinPoint, Throwable e) {
        log.info("进入异常日志{}------------------------------", e.getMessage());
        handle(joinPoint, e);
        log.info("退出异常通知{}--------------------", e.getMessage());
    }

    private void handle(JoinPoint joinPoint, Throwable exception) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        SysLogInfo sysLogInfo = new SysLogInfo();
        String uri = request.getRequestURI();
        if (uri.contains("/log")){
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        sysLogInfo.setUrl(uri);
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        SystemLog anSysLog = method.getAnnotation(SystemLog.class);
        Optional.ofNullable(anSysLog).ifPresent((e)->{
            sysLogInfo.setMethod(e.methods());
            sysLogInfo.setModule(e.module());
            sysLogInfo.setOperateType(e.operateType().getNum());
            sysLogInfo.setLogType(e.logType().getNum());
        });
        sysLogInfo.setExeStatus(1);
        Optional.ofNullable(exception).ifPresent((e)->{
           sysLogInfo.setExeStatus(2);
           sysLogInfo.setErrorMsg(StrFormatter.format("异常类：{}--异常信息：{}",e.getClass().getName(),e.getMessage()));
        });
        //获取参数
        Object[] args = joinPoint.getArgs();
        Optional.ofNullable(args).ifPresent((e)->{
            List<Object> params = new ArrayList<>();
            for (Object arg: e) {
                if (arg instanceof BeanPropertyBindingResult || arg instanceof HttpServletResponse
                        || arg instanceof MultipartFile[] || arg instanceof MultipartFile) {
                    continue;
                }
                params.add(arg);
            }
            try{
                if (CollUtil.isNotEmpty(params)){
                    sysLogInfo.setParams(JSONUtil.toJsonStr(params.stream().findFirst().orElse("")));
                }
            }catch (Exception ex){
                log.error("参数解析异常");
            }
        });
        //获取请求ip并填充
        String ip = GetLocalIpUtil.getIp(request);
        sysLogInfo.setIp(ip);
        sysLogInfo.setEndTime(now);
        try {
            //异步执行存储日志
            LogManager.me().executeLog(logTaskFactory.bussinessLog(sysLogInfo));
        }catch (Exception ex){
            log.error("异步日志出错！");
        }

    }

}
