package com.java3y.austin.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;


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
        try (OutputStream out = Files.newOutputStream(file.toPath())){
            byte[] ss = multipartFile.getBytes();
            for (byte s : ss) {
                out.write(s);
            }
        } catch (IOException e) {
            log.error("SpringFileUtils#getFile multipartFile is converted to File error:{}", e.toString());
            return null;
        }
        return file;
    }
}
