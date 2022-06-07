package com.java3y.austin.web.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

/**
 * for Amis!!! amis框架在【表单】回显的时候，不支持嵌套动态语法!!
 * 编写工具类将 List/Object 铺平成 Map
 * https://baidu.gitee.io/amis/zh-CN/components/form/index#%E8%A1%A8%E5%8D%95%E9%A1%B9%E6%95%B0%E6%8D%AE%E5%88%9D%E5%A7%8B%E5%8C%96
 *
 * @author 3y
 * @date 2022/1/23
 */
public class ConvertMap {
    /**
     * 需要打散的字段(将json字符串打散为一个一个字段返回）
     * (主要是用于回显数据)
     */
    private static final List<String> FLAT_FIELD_NAME = Arrays.asList("msgContent");

    /**
     * 需要格式化为jsonArray返回的字段
     * (前端是一个JSONArray传递进来)
     */
    private static final List<String> PARSE_JSON_ARRAY = Arrays.asList("feedCards", "btns");

    /**
     * 钉钉工作消息OA实际的映射
     */
    private static final List<String> DING_DING_OA_FIELD = Arrays.asList("dingDingOaHead", "dingDingOaBody");
    /**
     * 钉钉OA字段名实际的映射
     */
    private static final Map<String, String> DING_DING_OA_NAME_MAPPING = new HashMap<>();
    static {
        DING_DING_OA_NAME_MAPPING.put("bgcolor", "dingDingOaHeadBgColor");
        DING_DING_OA_NAME_MAPPING.put("text", "dingDingOaHeadTitle");
        DING_DING_OA_NAME_MAPPING.put("title", "dingDingOaTitle");
        DING_DING_OA_NAME_MAPPING.put("image", "media_id");
        DING_DING_OA_NAME_MAPPING.put("author", "dingDingOaAuthor");
        DING_DING_OA_NAME_MAPPING.put("content", "dingDingOaContent");
    }

    /**
     * 将List对象转换成Map(无嵌套)
     *
     * @param param
     * @return
     */
    public static <T> List<Map<String, Object>> flatList(List<T> param) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (T t : param) {
            Map<String, Object> map = flatSingle(t);
            result.add(map);
        }
        return result;
    }

    /**
     * 将单个对象转换成Map(无嵌套)
     * <p>
     * 主要兼容amis的回显(前端不用amis可忽略)
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> flatSingle(Object obj) {
        Map<String, Object> result = MapUtil.newHashMap(32);
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            if (FLAT_FIELD_NAME.contains(field.getName())) {
                String fieldValue = (String) ReflectUtil.getFieldValue(obj, field);
                JSONObject jsonObject = JSONObject.parseObject(fieldValue);
                for (String key : jsonObject.keySet()) {
                    /**
                     * 钉钉OA消息回显
                     */
                    if (DING_DING_OA_FIELD.contains(key)) {
                        JSONObject object = jsonObject.getJSONObject(key);
                        for (String objKey : object.keySet()) {
                            result.put(DING_DING_OA_NAME_MAPPING.get(objKey), object.getString(objKey));
                        }
                    } else if (PARSE_JSON_ARRAY.contains(key)) {
                        /**
                         * 部分字段是直接传入数组，把数组直接返回(也是用于回显)
                         */
                        result.put(key, JSON.parseArray(jsonObject.getString(key)));
                    } else{
                        result.put(key, jsonObject.getString(key));
                    }
                }
            }
            result.put(field.getName(), ReflectUtil.getFieldValue(obj, field));
        }
        return result;
    }
}
