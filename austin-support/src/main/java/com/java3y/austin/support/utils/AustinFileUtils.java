package com.java3y.austin.support.utils;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Throwables;
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
            File file = new File(path, url.getPath());
            inputStream = url.openStream();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                fileOutputStream = new FileOutputStream(file);
                IoUtil.copy(inputStream, fileOutputStream);
            }
            return file;
        } catch (Exception e) {
            log.error("AustinFileUtils#getRemoteUrl2File fail:{},remoteUrl:{}", Throwables.getStackTraceAsString(e), remoteUrl);
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close#inputStream fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
            if (Objects.nonNull(fileOutputStream)) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("close#fileOutputStream fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
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

}
