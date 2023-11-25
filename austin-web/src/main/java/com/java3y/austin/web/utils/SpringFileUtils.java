package com.java3y.austin.web.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


/**
 * @author 3y
 * multipartFile 转成 File 对象
 */
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
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] ss = multipartFile.getBytes();
            for (int i = 0; i < ss.length; i++) {
                out.write(ss[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(out)) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
