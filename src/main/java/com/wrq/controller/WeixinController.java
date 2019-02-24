package com.wrq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wangqian on 2019/2/14.
 *  不使用SDK获取 openid - 测试使用
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    /**
     * 1. 诱导用户点击：https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=www.xxx.com/weixin/auth&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     * 2. 点击后会请求：www.xxx.com/weixin/auth?code=515151wdw
     * 3. 映射到下方方法，获取code的值
     */
    @GetMapping("/auth")
    public void auth (@RequestParam("code")String code) {
        log.info("进入auth方法...");
        log.info("code={}", code);

        // 获取code后，请求以下链接获取access_token：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

        String url = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=公众号的appid&secret=公众号的SECRET&code="+ code +"&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        // 请求url， 返回String
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);

        /**
         * response:
         *
         * { "access_token":"ACCESS_TOKEN",
         *   "expires_in":7200,
         *   "refresh_token":"REFRESH_TOKEN",
         *   "openid":"OPENID",
         *   "scope":"SCOPE"
         *  }
         */

    }


}
