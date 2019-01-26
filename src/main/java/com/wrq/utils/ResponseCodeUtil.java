package com.wrq.utils;

import com.wrq.common.ResponseCode;

/**
 * 封装的响应对对象
 * Created by wangqian on 2019/1/26.
 */
public class ResponseCodeUtil {

    /**
     * 成功的响应对象
     * @param object
     * @return ResponseCode成功对应对象
     */
    public static ResponseCode success (Object object) {
        ResponseCode responseCode = new ResponseCode();
        responseCode.setData(object);
        responseCode.setMsg("成功");
        responseCode.setCode(0);
        return responseCode;
    }

    /**
     * 成功响应，不响应信息
     * @return
     */
    public static ResponseCode success () {
        return success(null);
    }

    /**
     * 错误的响应
     * @param code
     * @param msg
     * @return
     */
    public static ResponseCode error (Integer code, String msg) {
        ResponseCode responseCode = new ResponseCode();
        responseCode.setMsg(msg);
        responseCode.setCode(code);
        return responseCode;
    }

}
