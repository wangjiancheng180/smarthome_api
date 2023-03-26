package com.wjc.smarthome.controller;


import com.wjc.smarthome.dto.system.AuthInfo;
import com.wjc.smarthome.enetity.BaseEnetity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author 王建成
 * @date 2022/4/6--11:09
 */

public class BaseController {
   // @Autowired
    //private RedisTemplate<String,Object> redisTemplate;

//   public AuthInfo getUserInfo(){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//       AuthInfo userinfo = (AuthInfo) redisTemplate.opsForHash().get(RedisKey.USER_INFO, principal);
//       return userinfo;
//   }

   public void setCreate(BaseEnetity bean){
      // AuthInfo userInfo = getUserInfo();
       //bean.setCreateUserId(userInfo.getId());
      //bean.setCreateUserName(userInfo.getRealName());
     //  bean.setCreateTime(new Date());
   }

   public void setUpdate(BaseEnetity bean){
       //AuthInfo userInfo = getUserInfo();
      // bean.setUpdateUserId(userInfo.getId());
       //bean.setUpdateUserName(userInfo.getRealName());
       //bean.setUpdateTime(new Date());
   }

    /**
     * 设置excel下载响应头属性
     */
    public void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName , "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers","Content-disposition");
    }

}
