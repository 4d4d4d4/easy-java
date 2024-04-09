package com.easy.bean;

import com.easy.utils.PropertiesUtils;
import com.easy.utils.StringManipulationTool;

/**
 * @Classname Constant
 * @Description 什么也没有写哦~
 * @Date 2024/4/3 20:38
 * @Created by 憧憬
 */
public class CommonConstants {
    // java和resource
    private static final String PATH_JAVA = "java";
    private static final String PATH_RESOURCES = "resources";
    // 是否忽略表名前缀
    public static Boolean IGNORE_TABLE_PREFIX;
    // 搜索参数bean后缀
    public static String SUFFIX_BEAN_PARAM;
    // 模糊搜索bean后缀
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    // 参数起止后缀
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;
    // mapper后缀
    public static String SUFFIX_MAPPERS;
    // 文件输出路径
    public static String PATH_BASE;
    public static String PATH_PO;
    public static String PATH_QUERY;
    public static String PATH_UTILS;
    public static String PATH_ENUMS;
    public static String PATH_MAPPERS;
    public static String PATH_BASE_RESOURCES_MAPPERS_XML;
    public static String PATH_BASE_RESOURCES;
    // 作者名称
    public static String COMMENT_AUTHOR_NAME;
    // 包路径
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_MAPPERS;
    public static String PACKAGE_ENUMS;
    // 需要忽略序列化的属性
    public static String IGNORE_BEAN_TOJSON_FIELD; // 属性名
    public static String[] IGNORE_BEAN_TOJSON_FIELDS; // 属性名
    public static String IGNORE_BEAN_TOJSON_EXPRESSION; // 相应的标注的注解
    public static String IGNORE_BEAN_TOJSON_CLASS; // 注解类具体位置

    // 日期序列化与反序列化
    public static String BEAN_DATA_SERIALIZATION_FORMAT; // 序列化的格式
    public static String BEAN_DATA_SERIALIZATION_FORMAT_CLASS; // 序列化的注解
    public static String BEAN_DATA_DESERIALIZATION_FORMAT; // 反序列化的格式
    public static String BEAN_DATA_DESERIALIZATION_FORMAT_CLASS; // 反序列化的注解


    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = StringManipulationTool.upperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.param"));
        SUFFIX_BEAN_QUERY_FUZZY = StringManipulationTool.upperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query.fuzzy"));
        SUFFIX_BEAN_QUERY_TIME_START = StringManipulationTool.upperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query.time.start"));
        SUFFIX_BEAN_QUERY_TIME_END = StringManipulationTool.upperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query.time.end"));
        SUFFIX_MAPPERS = StringManipulationTool.upperCaseFirstLetter(PropertiesUtils.getString("suffix.mappers"));

        PATH_BASE = (PropertiesUtils.getString("path.base") + "." + PATH_JAVA + "." + PropertiesUtils.getString("package.base")).replace(".", "/");
        PATH_BASE_RESOURCES = (PropertiesUtils.getString("path.base") + "." + PATH_RESOURCES + "." + PropertiesUtils.getString("package.base")).replace(".", "/");
        PATH_BASE_RESOURCES_MAPPERS_XML = (PATH_BASE_RESOURCES + "." + PropertiesUtils.getString("package.mappers")).replace(".", "/");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = (PACKAGE_BASE + "." + PropertiesUtils.getString("package.po"));
        PACKAGE_QUERY = (PACKAGE_BASE + "." + PropertiesUtils.getString("package.query"));
        PATH_PO = (PATH_BASE + "." + PropertiesUtils.getString("package.po")).replace(".", "/");
        PATH_QUERY = (PATH_BASE + "." + PropertiesUtils.getString("package.query")).replace(".", "/");
        COMMENT_AUTHOR_NAME = PropertiesUtils.getString("comment.author.name");
        PACKAGE_UTILS = (PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils"));
        PATH_UTILS = (PATH_BASE + "." + PropertiesUtils.getString("package.utils")).replace(".", "/");
        PACKAGE_MAPPERS = (PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers"));
        PATH_MAPPERS = (PATH_BASE + "." + PropertiesUtils.getString("package.mappers")).replace(".", "/");

        PACKAGE_ENUMS = (PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums"));
        PATH_ENUMS = (PATH_BASE + "." + PropertiesUtils.getString("package.enums")).replace(".", "/");
        // 序列化忽略的字段
        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_FIELDS = IGNORE_BEAN_TOJSON_FIELD.split(",");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");
        // 日期序列化
        BEAN_DATA_SERIALIZATION_FORMAT = PropertiesUtils.getString("bean.data.serialization.format");
        BEAN_DATA_SERIALIZATION_FORMAT_CLASS = PropertiesUtils.getString("bean.data.serialization.format.class");
        // 日期反序列化
        BEAN_DATA_DESERIALIZATION_FORMAT = PropertiesUtils.getString("bean.data.deserialization.format");
        BEAN_DATA_DESERIALIZATION_FORMAT_CLASS = PropertiesUtils.getString("bean.data.deserialization.format.class");
    }

    public static void main(String[] args) {
        System.out.println(PATH_BASE_RESOURCES);
        System.out.println(PATH_BASE_RESOURCES_MAPPERS_XML);
    }
}