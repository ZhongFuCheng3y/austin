package com.java3y.austin.stream.utils;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 3y
 * @date 2022/2/15
 * 获取SpringContext对象
 */
@Slf4j
public class SpringContextUtils {
    private static ApplicationContext context;

    /**
     * XML配置
     */
    private static List<String> xmlPath = new ArrayList<>();

    public static ApplicationContext loadContext(String path) {
        return loadContext(new String[]{path});
    }

    /**
     * 通过spring.xml文件配置将信息 装载 context
     *
     * @param paths
     * @return
     */
    public static synchronized ApplicationContext loadContext(String[] paths) {
        if (null != paths && paths.length > 0) {
            List<String> newPaths = new ArrayList<>();
            for (String path : paths) {
                if (!xmlPath.contains(path)) {
                    newPaths.add(path);
                }
            }
            if (CollUtil.isNotEmpty(newPaths)) {
                String[] array = new String[newPaths.size()];
                for (int i = 0; i < newPaths.size(); i++) {
                    array[i] = newPaths.get(i);
                    xmlPath.add(newPaths.get(i));
                }
                if (null == context) {
                    context = new ClassPathXmlApplicationContext(array);
                } else {
                    context = new ClassPathXmlApplicationContext(array, context);
                }
            }
        }
        return context;
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

}
