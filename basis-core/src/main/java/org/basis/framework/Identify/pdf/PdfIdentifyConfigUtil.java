package org.basis.framework.Identify.pdf;

import com.alibaba.fastjson.JSONObject;
import org.basis.framework.error.BizCodeEnume;
import org.basis.framework.error.ServiceException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description  加载pdf识别 规则配置
 * @Author ChenWenJie
 * @Data 2023/04/28 2:55 下午
 **/
public class PdfIdentifyConfigUtil {

    public static IdentifyPDFProperties loadProperties(String path) throws ServiceException {
        Resource resource = new ClassPathResource(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(),"UTF-8"))){
            StringBuffer sb = new StringBuffer();
            String str = "";
            while((str=br.readLine())!=null) {
                sb.append(str);
            }
            IdentifyPDFProperties identifyBean = JSONObject.parseObject(sb.toString(),IdentifyPDFProperties.class);
            return identifyBean;
        } catch (IOException e) {
            throw new ServiceException("加载 pdf 识别配置文件失败！", BizCodeEnume.DEFAULT.getCode(),e);
        }
    }
}