package com.wrq.controller;

import com.wrq.config.ProjectUrlConfig;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * Created by wangqian on 2019/2/14.
 * 使用SDK
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/authorize")
    public String authorize (@RequestParam("returnUrl") String returnUrl) {

        // 配置文件中读取：微信公众平台授权url
        String url = projectUrlConfig.getWechatMpAuthorize() + "/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));

        log.info("[微信网页授权] 获取code，redirectUrl = {}", redirectUrl);

        // 理想情况：会请求 /userInfo
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl){

        // 用户请求此地址，会传递过来 code 和 state参数
        log.info("[微信网页授权] 进入userInfo方法");
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        log.info("[微信网页授权] 拿到的code = {} 和 returnUrl = {} ",code, returnUrl);

        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e){
            log.info("[微信授权] 获取accessToken失败");
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("[微信网页授权] 获取openid = {} ",openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }

    @GetMapping("/qrAuthorize")
    public String qrAuthorize (@RequestParam("returnUrl") String returnUrl) {

        // 配置文件中读取：开放平台授权url
        String url = projectUrlConfig.getWechatOpenAuthorize() + "/sell/wechat/qrUserInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QrConnectScope.SNSAPI_LOGIN, URLEncoder.encode(returnUrl));

        log.info("[微信扫码] 获取code，redirectUrl = {}", redirectUrl);

        // 理想情况：会请求 /userInfo
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl){

        // 用户请求此地址，会传递过来 code 和 state参数
        log.info("微信扫码登陆，请求了 /qrUserInfo");
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        log.info("[微信扫码登陆] 拿到的code = {} 和 returnUrl = {} ",code, returnUrl);

        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e){
            log.info("[微信扫码登陆] 获取accessToken失败");
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("[微信扫码登陆] 获取openid = {} ",openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }
}
