/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package org.basis.framework.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.basis.framework.error.IgnoreException;
import org.basis.framework.type.FilenameExtensionEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 *
 *
 * @author Stabber
 * @date 2020/2/22 13:45
 * @since 1.0.0
 */
@Slf4j
public class OSSClientUtil {
    // endpoint以杭州为例，其它region请按实际情况填写
    private String endpoint;
    // accessKey
    private String accessKeyId;
    private String accessKeySecret ;
    //空间
    public  final String bucketName;
    //apk的空间
    public final String apkBucketName ;
    //文件存储目录
    private String filedir;

    private OSSClient ossClient;

    public OSSClientUtil(String endpoint,String accessKeyId, String accessKeySecret,String bucketName,
                         String apkBucketName,String filedir){
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.apkBucketName = apkBucketName;
        this.filedir = filedir;
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
    

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Oss(String url) {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1], null);
        } catch (FileNotFoundException e) {
            throw new IgnoreException("图片上传失败");
        }
    }


    public String uploadImg2Oss(MultipartFile file) {
        String name = fileName(file);
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name, null);
            return name;
        } catch (Exception e) {
            throw new IgnoreException("图片上传失败");
        }
    }

    private String fileName(MultipartFile file) {
        if (file.getSize() > 4096*4096) {
            throw new IgnoreException("上传图片大小不能超过4M！");
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        return random.nextInt(10000) + System.currentTimeMillis() + substring;
    }

    public String uploadImg2Oss(MultipartFile file,String bucketName) {
        String name = fileName(file);
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name, bucketName);
            return name;
        } catch (Exception e) {
            throw new IgnoreException("图片上传失败");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl, String privateBucketName) {
        if (StringUtils.isNotEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1], privateBucketName);
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
    public String uploadFile2OSS(InputStream instream, String fileName,String privateBucketName) {
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
            PutObjectResult putResult = ossClient.putObject(StringUtils.isNotBlank(privateBucketName)?privateBucketName:bucketName, filedir + fileName, instream, objectMetadata);
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
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadApk2OSS(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name, apkBucketName);
            return name;
        } catch (Exception e) {
            throw new IgnoreException("apk上传失败");
        }
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
     * @return
     */
    public String getUrl(String key,String privateBucketName) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(StringUtils.isNotBlank(privateBucketName) ? privateBucketName : bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
}
