/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package org.basis.framework.utils;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.basis.framework.error.ServiceException;
import org.basis.framework.type.FilenameExtensionEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * @Description OSS 工具类
 * @Author ChenWenJie
 * @Data 2021/6/11 2:55 下午
 **/
@Slf4j
public class OSSClientUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret ;
    //空间
    public  final String bucketName;
    //apk的空间
    public final String apkBucketName ;
    //文件存储目录
    private String filedir;
    private OSSClient ossClient;
    // 附件最大限制 单位kb
    private Integer maxSizekb;

    public OSSClientUtil(String endpoint,String accessKeyId, String accessKeySecret,String bucketName,
                         String apkBucketName,String filedir,Integer maxSizekb){
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.apkBucketName = apkBucketName;
        this.filedir = filedir;
        this.maxSizekb = maxSizekb == null ? (4096*4096):maxSizekb;
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
    

    /**
     * 销毁
     */
    @PreDestroy
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 通过网址上传图片
     *
     * @param url
     * @return url
     */
    public void uploadImgByUrl(String url) {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFileByStream(fin, split[split.length - 1], null);
        } catch (FileNotFoundException e) {
            throw new ServiceException("图片上传失败");
        }
    }

    /**
     * 通过附件上传图片
     * @param file
     * @return
     */
    public String uploadImgByFile(MultipartFile file) {
        String name = fileName(file);
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFileByStream(inputStream, name, null);
            return name;
        } catch (Exception e) {
            throw new ServiceException("图片上传失败");
        }
    }

    /**
     * 或者附件名称
     * @param file
     * @return
     */
    private String fileName(MultipartFile file) {
        if (file.getSize() > maxSizekb) {
            throw new ServiceException("上传图片大小不能超过"+maxSizekb+" kb");
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        return random.nextInt(10000) + System.currentTimeMillis() + substring;
    }

    /**
     * 通过文件和存储桶上传图片
     * @param file
     * @param bucketName 存储桶名称
     * @return
     */
    public String uploadImgByFileAndBaycket(MultipartFile file,String bucketName) {
        String name = fileName(file);
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFileByStream(inputStream, name, bucketName);
            return name;
        } catch (Exception e) {
            throw new ServiceException("图片上传失败");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @param privateBucketName 私有存储桶名称
     * @return
     */
    public String getImgUrl(String fileUrl, String privateBucketName) {
        if (StringUtils.isNotEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getTimeBarUrl(dir(filedir)+ split[split.length - 1], privateBucketName);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFileByStream(InputStream instream, String fileName,String privateBucketName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(StringUtils.isNotBlank(privateBucketName)?privateBucketName:bucketName, dir(filedir) + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getContentType(String filenameExtension) {
        return FilenameExtensionEnum.of(filenameExtension).getDescription();
    }

    /**
     * 获得url链接
     *
     * @param key
     * @param privateBucketName 私有存储桶名称
     * @return
     */
    public String getTimeBarUrl(String key,String privateBucketName) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(StringUtils.isNotBlank(privateBucketName) ? privateBucketName : bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }


    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadByFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFileByStream(inputStream, name, apkBucketName);
            return name;
        } catch (Exception e) {
            throw new ServiceException("apk上传失败");
        }
    }

    /**
     * 附件上传
     * @param instream
     * @param fileName
     * @return url
     */
    public String uploadByFile(InputStream instream, String fileName){
       try {
           String path = dir(filedir)+fileName;
           PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, instream);
           ossClient.putObject(putObjectRequest);
           return getUrl(path);
       }catch (OSSException oe){
            throw new ServiceException("oss 文件上传失败！");
       }finally {
           if (instream!=null){
               try {
                   instream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
    }

    public String getUrl(String path){
        return "https://"+bucketName+"."+endpoint+"/"+path;
    }

    public String dir(String filedir){
        if (StringUtils.isNotBlank(filedir)){
            return filedir+"/";
        }
        return DateUtil.format(new Date(), "yyyyMMdd")+"/";
    }
}
