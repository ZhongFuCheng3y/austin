package com.java3y.austin.cron.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.cron.domain.CrowdInfoVo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取人群文件 工具类
 *
 * @author 3y
 * @date 2022/2/9
 */
@Slf4j
public class ReadFileUtils {

    /**
     * 读取csv文件
     * 1. 获取第一行信息(id,paramsKey1,params2Key2)，第一列默认为接收者Id
     * 2. 把文件信息塞进对象内
     * 3. 把对象返回
     *
     * @param path
     * @return
     */
    public static List<CrowdInfoVo> getCsvRowList(String path) {
        List<CrowdInfoVo> result = new ArrayList<>();
        try {
            CsvData data = CsvUtil.getReader().read(FileUtil.file(path));
            if (data == null || data.getRow(0) == null || data.getRow(1) == null) {
                log.error("read csv file empty!,path:{}", path);
            }
            // 第一行为默认为头信息,所以遍历从第二行开始,第一列默认为接收者Id(不处理)
            CsvRow headerInfo = data.getRow(0);
            for (int i = 1; i < data.getRowCount(); i++) {
                CsvRow row = data.getRow(i);
                Map<String, String> param = MapUtil.newHashMap();
                for (int j = 1; j < headerInfo.size(); j++) {
                    param.put(headerInfo.get(j), row.get(j));
                }
                result.add(CrowdInfoVo.builder().id(row.get(0)).params(param).build());
            }

        } catch (Exception e) {
            log.error("TaskHandler#getCsvRowList fail!{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }
}
