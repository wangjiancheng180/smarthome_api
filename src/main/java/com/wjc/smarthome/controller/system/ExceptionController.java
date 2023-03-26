package com.wjc.smarthome.controller.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.wjc.smarthome.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王建成
 * @date 2023/2/24--14:03
 * 主要用来处理shiro异常
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {

    /**
     * 权限不足异常处理
     *
     * @return
     */
    @ExceptionHandler({AuthorizationException.class, UnauthorizedException.class})
    public JsonResult<Boolean> handleAuthorization() {
        return JsonResult.failure(5, "权限不足", false);
    }

    /**
     * 参数校验为实体类
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult<Object> handleValidException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        List<HashMap<String, String>> collect = allErrors.stream().map(a -> {
            HashMap<String, String> data = new HashMap<>(1);
            data.put(Optional.ofNullable(a.getCodes()[1]).orElse("不合格字段为空"), a.getDefaultMessage());
            return data;
        }).collect(Collectors.toList());
        log.error("请求参数出错：{}", JSONUtil.toJsonStr(collect));
        return JsonResult.failure("请求参数异常！", collect);
    }

    /**
     * 参数校验 单个参数或多个参数时
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResult<Object> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return JsonResult.failure("请求参数异常！", messages);
    }

    public JsonResult<Boolean> handleAuthentication() {
        return JsonResult.failure(500, "请先登录！", false);
    }
}
