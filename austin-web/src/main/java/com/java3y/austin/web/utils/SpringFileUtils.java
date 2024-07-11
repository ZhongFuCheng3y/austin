package com.java3y.austin.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @author 3y
 * multipartFile 转成 File 对象
 */
@Slf4j
public class SpringFileUtils {
    private SpringFileUtils() {
    }

    /**
     * multipartFile 转成 File 对象
     *
     * @param multipartFile
     * @return
     */
    public static File getFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        File file = new File(fileName);
        try (OutputStream out = new FileOutputStream(file)){
            byte[] ss = multipartFile.getBytes();
            for (int i = 0; i < ss.length; i++) {
                out.write(ss[i]);
            }
        } catch (IOException e) {
            log.error("SpringFileUtils#getFile multipartFile is converted to File error:{}", e);
            return null;
        }
        return file;
    }
}
