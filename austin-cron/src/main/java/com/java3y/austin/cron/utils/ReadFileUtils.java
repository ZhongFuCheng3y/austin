package com.java3y.austin.cron.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.*;
import com.google.common.base.Throwables;
import com.java3y.austin.cron.csv.CountFileRowHandler;
import com.java3y.austin.cron.vo.CrowdInfoVo;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.*;

/**
 * 读取人群文件 工具类
 *
 * @author 3y
 * @date 2022/2/9
 */
@Slf4j
public class ReadFileUtils {
    /**
     * csv文件 存储 接收者 的列名
     */
    public static final String RECEIVER_KEY = "userId";

    private ReadFileUtils() {
    }

    /**
     * 读取csv文件，每读取一行都会调用 csvRowHandler 对应的方法
     *
     * @param path
     * @param csvRowHandler
     */
    public static void getCsvRow(String path, CsvRowHandler csvRowHandler) {

        // 把首行当做是标题，获取reader
        try (CsvReader reader = CsvUtil.getReader(new FileReader(path),
                new CsvReadConfig().setContainsHeader(true))) {
            reader.read(csvRowHandler);
        } catch (Exception e) {
            log.error("ReadFileUtils#getCsvRow fail!{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 读取csv文件，获取文件里的行数
     *
     * @param path
     * @param countFileRowHandler
     */
    public static long countCsvRow(String path, CountFileRowHandler countFileRowHandler) {

        // 把首行当做是标题，获取reader
        try (CsvReader reader = CsvUtil.getReader(new FileReader(path),
                new CsvReadConfig().setContainsHeader(true))) {

            reader.read(countFileRowHandler);
        } catch (Exception e) {
            log.error("ReadFileUtils#getCsvRow fail!{}", Throwables.getStackTraceAsString(e));
        }
        return countFileRowHandler.getRowSize();
    }

    /**
     * 从文件的每一行数据获取到params信息
     * [{key:value},{key:value}]
     *
     * @param fieldMap
     * @return
     */
    public static Map<String, String> getParamFromLine(Map<String, String> fieldMap) {
        HashMap<String, String> params = MapUtil.newHashMap();
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            if (!ReadFileUtils.RECEIVER_KEY.equals(entry.getKey())) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }


    /**
     * 一次性读取csv文件整个内容
     * 1. 获取第一行信息(id,paramsKey1,params2Key2)，第一列默认为接收者Id
     * 2. 把文件信息塞进对象内
     * 3. 把对象返回
     *
     * @param path
     * @return
     * @Deprecated 可能会导致内存爆炸
     */
    @Deprecated
    public static List<CrowdInfoVo> getCsvRowList(String path) {
        List<CrowdInfoVo> result = new ArrayList<>();
        try {
            CsvData data = CsvUtil.getReader().read(FileUtil.file(path));
            if (Objects.isNull(data) || Objects.isNull(data.getRow(0)) || Objects.isNull(data.getRow(1))) {
                log.error("read csv file empty!,path:{}", path);
                return result;
            }
            // 第一行为默认为头信息,所以遍历从第二行开始,第一列默认为接收者Id(不处理)
            CsvRow headerInfo = data.getRow(0);
            for (int i = 1; i < data.getRowCount(); i++) {
                CsvRow row = data.getRow(i);
                Map<String, String> param = MapUtil.newHashMap();
                for (int j = 1; j < headerInfo.size(); j++) {
                    param.put(headerInfo.get(j), row.get(j));
                }

                result.add(CrowdInfoVo.builder().receiver(CollUtil.getFirst(row.iterator())).params(param).build());
            }

        } catch (Exception e) {
            log.error("ReadFileUtils#getCsvRowList fail!{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }


}
