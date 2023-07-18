package com.java3y.austin.support.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.util.PropertyPlaceholderHelper;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author 3y
 * 内容占位符 替换
 * <p>
 * austin占位符格式{$var}
 * json 占位符 {$_JSON_var}
 */
public class ContentHolderUtil {

    /**
     * 占位符前缀
     */
    private static final String PLACE_HOLDER_PREFIX = "{$";

    /**
     * 模版引擎解析字段前缀
     */
    private static final String JSON_FLAG_PREFIX = "_JSON_";

    /**
     * 占位符后缀
     */
    private static final String PLACE_HOLDER_SUFFIX = "}";

    private static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER = new PropertyPlaceholderHelper(PLACE_HOLDER_PREFIX, PLACE_HOLDER_SUFFIX);

    private static final TemplateEngine engine = TemplateUtil.createEngine();


    public static String replacePlaceHolder(final String template, final Map<String, Object> paramMap) {
        String result = PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(template, new CustomPlaceholderResolver(template, paramMap));
        // 最后用模板引擎动态解析一次json字符串
        result = engine.getTemplate(result).render(paramMap);
        return result;
    }

    private static class CustomPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private final String template;
        private final Map<String, Object> paramMap;

        public CustomPlaceholderResolver(String template, Map<String, Object> paramMap) {
            super();
            this.template = template;
            this.paramMap = paramMap;
        }

        @Override
        public String resolvePlaceholder(String placeholderName) {
            Object value = paramMap.get(placeholderName);
            if (ObjectUtil.isEmpty(value)) {
                String errorStr = MessageFormat.format("template:{0} require param:{1},but not exist! paramMap:{2}", template, placeholderName, paramMap.toString());
                throw new IllegalArgumentException(errorStr);
            }
            // 如果遇到 Json, 则先跳过不处理, 到外部使用整体模板引擎渲染
            if (StrUtil.startWith(placeholderName, JSON_FLAG_PREFIX)) {
                // 如果值为 字符串, 则将其转换为 JSON 做字段解析
                if (value instanceof String) {
                    Object jsonValue = JSONUtil.parse(value);
                    paramMap.put(placeholderName, jsonValue);
                }
                return placeholderName;
            }
            return StrUtil.toString(value);

        }
    }

}
