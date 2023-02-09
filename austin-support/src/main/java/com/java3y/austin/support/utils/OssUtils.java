package com.java3y.austin.support.utils;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author 3y
 * @description 对象存储（七牛云）
 */
public class OssUtils {

    public static void main(String[] args) throws UnsupportedEncodingException {
        //upload();
        String fmnBLE4QtkwlErXIAh9pYS029GZk = getFileUrl("FmnBLE4QtkwlErXIAh9pYS029GZk");
        System.out.println(fmnBLE4QtkwlErXIAh9pYS029GZk);
    }

    private static void upload() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "123";
        String secretKey = "123";
        String bucket = "austin3y";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\zhongfucheng\\Desktop\\1201.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = IdUtil.fastSimpleUUID();

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            //解析上传成功的结果
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    public static String getFileUrl(String fileName) throws UnsupportedEncodingException {

        String domainOfBucket = "http://devtools.qiniu.com/austin3y";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);
        return finalUrl;
    }
}
