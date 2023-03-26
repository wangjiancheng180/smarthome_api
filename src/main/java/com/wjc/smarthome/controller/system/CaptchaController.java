//package com.wjc.smarthome.controller.system;
//
//import cn.hutool.core.io.resource.ResourceUtil;
//import com.wjc.captcha.dto.CaptchaResult;
//import com.wjc.captcha.dto.CaptchaVo;
//import com.wjc.captcha.utill.CaptchaUtil;
//import com.wjc.captcha.utill.PuzzleCaptcha;
//import com.wjc.common.JsonResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.awt.*;
//import java.util.Map;
//
///**
// * @author 王建成
// * @date 2022/5/21--17:16
// */
//@RestController
//@RequestMapping("/captcha")
//public class CaptchaController {
//
//    @Autowired
//    private CaptchaUtil captchaUtil;
//
//
//    @GetMapping(value = "/getImage")
//    public JsonResult<CaptchaVo> captcha() {
//        PuzzleCaptcha puzzleCaptcha = new PuzzleCaptcha(ResourceUtil.getStream("images/captcha/default.jpg"));
//        puzzleCaptcha.setImageQuality(Image.SCALE_AREA_AVERAGING);
//        puzzleCaptcha.run();
//        return JsonResult.success(captchaUtil.captcha(puzzleCaptcha));
//    }
//
//    @PostMapping(value = "/verify")
//    public JsonResult<CaptchaResult> verify(@RequestBody Map<String, Object> map) {
//        return JsonResult.success(captchaUtil.verify(map));
//    }
//}
