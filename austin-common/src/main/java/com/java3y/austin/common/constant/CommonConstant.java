package com.java3y.austin.common.constant;


/**
 * @author 3y
 */
public class CommonConstant {

    public final static String PERIOD = ".";
    public final static String COMMA = ",";
    public final static String COLON = ":";
    public final static String SEMICOLON = ";";
    public final static String POUND = "#";
    public final static String SLASH = "/";
    public final static String BACKSLASH = "\\";
    public final static String EMPTY_STRING = "";
    public final static String RADICAL = "|";

    public final static String QM_STRING = "?";
    public final static String EQUAL_STRING = "=";
    public final static String AND_STRING = "&";


    public final static String ONE = "1";
    public final static String ZERO = "0";
    public final static String MINUS_ONE = "-1";
    public final static String YES = "Y";
    public final static String NO = "N";


    public final static char QM = '?';

    /**
     * boolean转换
     */
    public final static Integer TRUE = 1;
    public final static Integer FALSE = 0;


    /**
     * 加密算法
     */
    public static final String HMAC_SHA256_ENCRYPTION_ALGO = "HmacSHA256";

    /**
     * 编码格式
     */
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * HTTP请求内容格式
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    public static final String CONTENT_TYPE_TEXT = "text/html;charset=utf-8";
    public static final String CONTENT_TYPE_XML = "application/xml; charset=UTF-8";
    public static final String CONTENT_TYPE_FORM_URL_ENCODE = "application/x-www-form-urlencoded;charset=utf-8;";
    public static final String CONTENT_TYPE_MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * HTTP 请求方法
     */
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";

    /**
     * JSON默认值
     */
    public final static String EMPTY_JSON_OBJECT = "{}";
    public final static String EMPTY_VALUE_JSON_ARRAY = "[]";

    /**
     * cron时间格式
     */
    public final static String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";

    /**
     * 环境常量
     */
    public final static String ENV_DEV = "dev";
    public final static String ENV_TEST = "test";


}