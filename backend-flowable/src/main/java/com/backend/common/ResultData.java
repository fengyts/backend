package com.backend.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;


/**
 * @param <T>
 * @author fengyt
 */
@Getter
public class ResultData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private static final String SUCCESS_FLAG = "0";
    private static final String FAILURE_FLAG = "-1";

    /**
     * 状态码描述
     */
    private static final String MSG_SUCCESS = "SUCCESS";
    private static final String MSG_FAILURE = "FAILURE";

    /**
     * 请求结果 错误信息
     */
    public static final String MSG_ERR_SYSTEM = "系统繁忙";
    public static final String MSG_ERR_PARAM = "参数错误";
    public static final String MSG_ERR_BUSINESS = "业务异常";

    private String code;
    private String msg;
    private T data;

    /**
     * 响应主体
     */
//    private final JSONObject jsonObject = new JSONObject();

    private ResultData() {
    }

    public ResultData(String code, String msg) {
        this(code, msg, null);
    }

    public ResultData(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultData okOfMapData(){
        Map<String, Object> mapData = Maps.newHashMapWithExpectedSize(2);
        return ok(mapData);
    }

    public ResultData extendMapData(String key, Object value){
        if (Objects.isNull(this.data)) {
            Map<String, Object> mapData = Maps.newHashMapWithExpectedSize(2);
            mapData.put(key, value);
            this.data = (T) mapData;
        } else {
            if(data instanceof Map){
                ((Map<String, Object>) data).put(key, value);
            }
        }
        return this;
    }

    public static ResultData ok() {
        return success(null);
    }

    public static <T> ResultData ok(T data) {
        return success(data);
    }

    public static ResultData err() {
        return err(null, null);
    }

    public static ResultData err(String msg) {
        return err(msg, null);
    }

    public static <T> ResultData err(String msg, T data) {
        return new ResultData(FAILURE_FLAG, msg, data);
    }

    public static ResultData errSystem() {
        return err(MSG_ERR_SYSTEM, null);
    }

    public static ResultData errParam() {
        return err(MSG_ERR_PARAM, null);
    }

    public static ResultData errBusiness() {
        return err(MSG_ERR_BUSINESS, null);
    }

    private static <T> ResultData success(T data) {
        return new ResultData(SUCCESS_FLAG, MSG_SUCCESS, data);
    }

    public String toJsonString() {
//        return this.jsonObject.toJSONString();
        return JSONObject.toJSONString(this);
    }

    @Override
    public String toString(){
        return toJsonString();
    }

}
