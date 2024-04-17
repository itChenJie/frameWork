package org.basis.framework.http;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
public class AlibabaHttpClient extends ApacheHttpClient {
    protected String HOST;

    protected String APPKEY;

    protected String APPSECRET;

    @PostConstruct
    public void initParam() {
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(APPKEY);
        httpParam.setAppSecret(APPSECRET);
        httpParam.setScheme(Scheme.HTTP);
        httpParam.setHost(HOST);
        super.init(httpParam);
    }

    /**
     * 调用天图系统接口
     */
    public <T> BaseResponse<T>  call(String url, Object param, TypeReference<BaseResponse<T>> typeReference) throws IOException {
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY, url);
        log.info("aibaba http client，url[{}]，APPKEY[{}]，APPSECRET[{}]，参数[{}]", url, APPKEY, APPSECRET, JSON.toJSON(param));
        request.setBody(JSONObject.toJSONBytes(param));
        request.addHeader("Content-Type", "application/json; charset=utf-8");
        ApiResponse apiResponse = sendSyncRequest(request);
        BaseResponse result = getResultString(apiResponse,typeReference);
        log.info("aibaba http client，result：{}", JSON.toJSON(result));
        if (result.getCode() != 200) {
            log.error("client error-----> msg---->{}", result.getMsg());
            return result;
        }
        return result;
    }

    public <T>  BaseResponse<T>  getResultString(ApiResponse response, TypeReference<BaseResponse<T>> typeReference) throws IOException {
        StringBuilder result = new StringBuilder();
        if (response.getCode() != 200) {
            result.append(response.getHeaders().get("X-Ca-Error-Message"));
            return BaseResponse.error( result.toString(),null);
        }
        String res = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        log.info("body数据：{}", res);
        return  JSON.parseObject(res, typeReference);
    }

}
