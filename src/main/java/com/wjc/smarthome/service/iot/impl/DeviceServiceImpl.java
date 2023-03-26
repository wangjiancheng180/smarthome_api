package com.wjc.smarthome.service.iot.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.sdk.service.iot20180120.AsyncClient;
import com.aliyun.sdk.service.iot20180120.models.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjc.smarthome.common.DeviceStatus;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.mapstruct.iot.DeviceMap;
import com.wjc.smarthome.param.iot.DeviceCreateBean;
import com.wjc.smarthome.param.iot.DeviceQueryBean;
import com.wjc.smarthome.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.smarthome.mapper.iot.DeviceMapper;
import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.service.iot.DeviceService;
import com.wjc.smarthome.common.RedisKeyCounts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author wjc
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    private final AsyncClient alIotClient;

    //private final DeviceMapper deviceMapper;

    @Value("${iot.iotInstanceId}")
    private String iotInstanceId;

    @Value("${iot.productKey}")
    private String productKey;

    private final RedisService redisService;

    @Override
    public JsonResult<Boolean> create(DeviceCreateBean bean) {
        RegisterDeviceRequest registerDeviceRequest = RegisterDeviceRequest.builder()
                .productKey(productKey)
                .deviceName(bean.getDeviceName())
                .nickname(bean.getNickName())
                .iotInstanceId(iotInstanceId)
                .build();
        CompletableFuture<RegisterDeviceResponse> response = alIotClient.registerDevice(registerDeviceRequest);
        try {
            RegisterDeviceResponse registerDeviceResponse = response.get();
            if (registerDeviceResponse.getBody().getSuccess()) {
                RegisterDeviceResponseBody.Data data = registerDeviceResponse.getBody().getData();
                Device device = DeviceMap.INSTANCE.beanToEentity(bean);
                device.setProductKey(data.getProductKey());
                device.setDeviceSecret(data.getDeviceSecret());
                device.setIotId(data.getIotId());
                //刚注册的设备未注册
                device.setDeviceStatus(DeviceStatus.UNACTIVE.i);
                if (save(device)) {
                    return JsonResult.success(true);
                }
            }
            log.error("阿里云--错误码:{}--错误信息", registerDeviceResponse.getBody().getCode(), registerDeviceResponse.getBody().getErrorMessage());
        } catch (InterruptedException e) {
            log.error("阿里云注册设备异步线程被打断：{}", e.getMessage());
        } catch (ExecutionException e) {
            log.error("阿里云注册设备未完成异常：{}", e.getMessage());
        }
        return JsonResult.failure("设备注册失败", false);
    }

    @Override
    public JsonResult<Boolean> disable(String iotId) {
        Device device = queryByIotId(iotId).getData();
        if (device == null) {
            return JsonResult.failure("设备不存在", false);
        }
        if (device.getDisable().equals(2)) {
            return JsonResult.success(true);
        }
        DisableThingRequest disableThingRequest = DisableThingRequest.builder()
                .iotId(iotId)
                .iotInstanceId(iotInstanceId)
                .build();
        CompletableFuture<DisableThingResponse> response = alIotClient.disableThing(disableThingRequest);
        try {
            DisableThingResponse resp = response.get();
            Boolean success = resp.getBody().getSuccess();
            if (success) {
                LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<Device>();
                wrapper.eq(Device::getIotId, iotId);
                device.setDisable(2);
                if (update(device, wrapper)) {
                    return JsonResult.success(200, "禁用设备成功", true);
                }
            }
            log.error("阿里云--错误码:{}--错误信息", resp.getBody().getCode(), resp.getBody().getErrorMessage());
        } catch (InterruptedException e) {
            log.error("阿里云禁用设备异步线程被打断：{}", e.getMessage());
        } catch (ExecutionException e) {
            log.error("阿里云禁用设备未完成异常：{}", e.getMessage());
        }
        return JsonResult.failure("禁用设备失败", false);
    }

    @Override
    public JsonResult<Boolean> enable(String iotId) {
        Device device = queryByIotId(iotId).getData();
        if (device == null) {
            return JsonResult.failure("设备不存在", false);
        }
        if (device.getDisable().equals(1)) {
            return JsonResult.success(true);
        }
        EnableThingRequest enableThingRequest = EnableThingRequest.builder()
                .iotId(iotId)
                .iotInstanceId(iotInstanceId)
                .build();
        CompletableFuture<EnableThingResponse> response = alIotClient.enableThing(enableThingRequest);
        try {
            EnableThingResponse resp = response.get();
            Boolean success = resp.getBody().getSuccess();
            if (success) {
                LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<Device>();
                wrapper.eq(Device::getIotId, iotId);
                device.setDisable(1);
                if (update(device, wrapper)) {
                    return JsonResult.success(200, "启用设备成功", true);
                }
            }
            log.error("阿里云--错误码:{}--错误信息", resp.getBody().getCode(), resp.getBody().getErrorMessage());
        } catch (InterruptedException e) {
            log.error("阿里云启用设备异步线程被打断：{}", e.getMessage());
        } catch (ExecutionException e) {
            log.error("阿里云启用设备未完成异常：{}", e.getMessage());
        }
        return JsonResult.failure("启用设备失败!", false);
    }

    @Override
    public JsonResult<Boolean> delete(String iotId) {
        Device device = queryByIotId(iotId).getData();
        if (device == null) {
            return JsonResult.failure("设备不存在", false);
        }
        DeleteDeviceRequest deleteThingRequest = DeleteDeviceRequest.builder()
                .iotId(iotId)
                .iotInstanceId(iotInstanceId)
                .build();
        CompletableFuture<DeleteDeviceResponse> response = alIotClient.deleteDevice(deleteThingRequest);
        try {
            DeleteDeviceResponse resp = response.get();
            Boolean success = resp.getBody().getSuccess();
            if (success) {
                LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<Device>();
                wrapper.eq(Device::getIotId, iotId);
                if (remove(wrapper)) {
                    return JsonResult.success(200, "删除设备成功", true);
                }
            }
            log.error("阿里云--错误码:{}--错误信息", resp.getBody().getCode(), resp.getBody().getErrorMessage());
        } catch (InterruptedException e) {
            log.error("阿里云注册设备异步线程被打断：{}", e.getMessage());
        } catch (ExecutionException e) {
            log.error("阿里云注册设备未完成异常：{}", e.getMessage());
        }
        return JsonResult.failure("删除设备失败", false);
    }

    @Override
    public JsonResult<Device> queryByIotId(String iotId) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getIotId, iotId);
        Device device = getOne(queryWrapper);
        if (device == null) {
            return JsonResult.failure("设备不存在", null);
        }
        return JsonResult.success(device);
    }

    @Override
    public JsonResult<IPage<Device>> queryPage(DeviceQueryBean bean) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(bean.getDeviceName()),Device::getDeviceName, bean.getDeviceName());
        queryWrapper.like(StrUtil.isNotBlank(bean.getNickName()),Device::getNickName, bean.getNickName());
        queryWrapper.eq(Optional.ofNullable(bean.getDeviceStatus()).isPresent(),Device::getDeviceStatus, bean.getDeviceStatus());
        queryWrapper.eq(Optional.ofNullable(bean.getDisable()).isPresent(),Device::getDisable, bean.getDisable());
        queryWrapper.orderByDesc(Device::getCreateTime);
        Page<Device> page = page(new Page<>(bean.getPage(), bean.getSize()), queryWrapper);
        return JsonResult.success(page);
    }

    @Override
    public JsonResult<List<Device>> queryOnlineDevice() {
        Set<Object> fields = redisService.getFileds(RedisKeyCounts.DeviceKeys.DEVICES_ONLINE);
        List<Device> devices = new ArrayList<>();
        if (CollUtil.isNotEmpty(fields)){
            List<String> keys = fields.stream().map(Object::toString).collect(Collectors.toList());
            keys.forEach((e)->{
                devices.add(redisService.get(RedisKeyCounts.DeviceKeys.DEVICES_ONLINE,e,Device.class));
            });

        }else {
            List<Device> deviceList = new LambdaQueryChainWrapper<>(baseMapper).eq(Device::getDeviceStatus, RedisKeyCounts.DeviceKeys.ONLINE_STATUS).list();
            return JsonResult.success(deviceList);
        }


        return JsonResult.success(devices);
    }

    @Override
    public JsonResult<List<Device>> queryOfflineDevice() {
        Set<Object> fields = redisService.getFileds(RedisKeyCounts.DeviceKeys.DEVICES_OFFLINE);
        List<Device> devices = new ArrayList<>();
        if (CollUtil.isNotEmpty(fields)){
            List<String> keys = fields.stream().map(Object::toString).collect(Collectors.toList());
            keys.forEach((e)->{
                devices.add(redisService.get(RedisKeyCounts.DeviceKeys.DEVICES_OFFLINE,e,Device.class));
            });

        }else {
            List<Device> deviceList = new LambdaQueryChainWrapper<>(baseMapper).eq(Device::getDeviceStatus, RedisKeyCounts.DeviceKeys.OFFLINE_STATUS).list();
            return JsonResult.success(deviceList);
        }


        return JsonResult.success(devices);
    }


}
