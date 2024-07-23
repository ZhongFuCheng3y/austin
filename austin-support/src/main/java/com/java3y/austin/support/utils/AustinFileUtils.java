package com.java3y.austin.support.utils;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author 3y
 * @date 2023/2/14
 */
@Slf4j
public class AustinFileUtils {

    private AustinFileUtils() {

    }

    /**
     * 读取 远程链接 返回File对象
     *
     * @param path      文件路径
     * @param remoteUrl cdn/oss文件访问链接
     * @return
     */
    public static File getRemoteUrl2File(String path, String remoteUrl) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(remoteUrl);
            String protocol = url.getProtocol();
            // 防止SSRF攻击
            if (!CommonConstant.HTTP.equalsIgnoreCase(protocol)
                    && !CommonConstant.HTTPS.equalsIgnoreCase(protocol)
                    && !CommonConstant.OSS.equalsIgnoreCase(protocol)) {
                log.error("AustinFileUtils#getRemoteUrl2File fail:{}, remoteUrl:{}",
                        "The remoteUrl is invalid, it can only be of the types http, https, and oss.", remoteUrl);
                return null;
            }
            File file = new File(path, url.getPath());
            inputStream = url.openStream();
            if (!file.exists()) {
                boolean res = file.getParentFile().mkdirs();
                if (!res) {
                    log.error("AustinFileUtils#getRemoteUrl2File Failed to create folder, path:{}, remoteUrl:{}", path, remoteUrl);
                    return null;
                }
                fileOutputStream = new FileOutputStream(file);
                IoUtil.copy(inputStream, fileOutputStream);
            }
            return file;
        } catch (Exception e) {
            log.error("AustinFileUtils#getRemoteUrl2File fail:{},remoteUrl:{}", Throwables.getStackTraceAsString(e), remoteUrl);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(fileOutputStream);
        }
        return null;
    }

    /**
     * 读取 远程链接集合 返回有效的File对象集合
     *
     * @param path       文件路径
     * @param remoteUrls cdn/oss文件访问链接集合
     * @return
     */
    public static List<File> getRemoteUrl2File(String path, Collection<String> remoteUrls) {
        List<File> files = new ArrayList<>();
        remoteUrls.forEach(remoteUrl -> {
            File file = getRemoteUrl2File(path, remoteUrl);
            if (file != null) {
                files.add(file);
            }
        });
        return files;
    }

    /**
     * 关闭InputStream流
     *
     * @param inputStream
     */
    private static void closeQuietly(InputStream inputStream) {
        if (Objects.nonNull(inputStream)) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("close#inputStream fail:{}", Throwables.getStackTraceAsString(e));
            }
        }
    }

    /**
     * 关闭FileOutputStream流
     *
     * @param fileOutputStream
     */
    private static void closeQuietly(FileOutputStream fileOutputStream) {
        if (Objects.nonNull(fileOutputStream)) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("close#fileOutputStream fail:{}", Throwables.getStackTraceAsString(e));
            }
        }
    }
}
