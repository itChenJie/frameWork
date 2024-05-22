package org.basis.framework.json;

import lombok.Data;

/**
 * @Description 带泛型的api返回结果
 * @Author ChenWenJie
 * @Data 2021/7/4 5:03 下午
 **/
@Data
public class JsonReturnApi<T> {
    public static final Integer SUCCESS = 1;
    public static final Integer ERROR = 0;
    /**
     * 1表示成功
     */
    private int status = 1; // 默认为1

    private String errorCode;

    private String msg = "success";

    private String requestId;

    private T result;


    public static JsonReturnApi successData(Object object) {
        JsonReturnApi jsonReturnApi = new JsonReturnApi();
        jsonReturnApi.setStatus(SUCCESS);
        jsonReturnApi.setResult(object);
        return jsonReturnApi;
    }

    public static JsonReturnApi errorMsg(String errorMsg) {
        JsonReturnApi jsonReturnApi = new JsonReturnApi();
        jsonReturnApi.setStatus(ERROR);
        jsonReturnApi.setMsg(errorMsg);
        return jsonReturnApi;
    }

    public static JsonReturnApi error(String errorCode,String errorMsg){
        JsonReturnApi jsonReturnApi = new JsonReturnApi();
        jsonReturnApi.setStatus(ERROR);
        jsonReturnApi.setErrorCode(errorCode);
        jsonReturnApi.setMsg(errorMsg);
        return  jsonReturnApi;
    }

}
