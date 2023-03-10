package com.java3y.austin.support.utils;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * @author 3y
 * @date 2023/2/14
 */
@Slf4j
public class AustinFileUtils {

    /**
     * 读取 远程链接 返回File对象
     *
     * @param path      文件路径
     * @param remoteUrl cdn/oss文件访问链接
     * @return
     */
    public static File getRemoteUrl2File(String path, String remoteUrl) {
        try {
            URL url = new URL(remoteUrl);
            File file = new File(path, url.getPath());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                IoUtil.copy(url.openStream(), new FileOutputStream(file));
            }
            return file;
        } catch (Exception e) {
            log.error("AustinFileUtils#getRemoteUrl2File fail:{},remoteUrl:{}", Throwables.getStackTraceAsString(e), remoteUrl);
        }
        return null;
    }

}
