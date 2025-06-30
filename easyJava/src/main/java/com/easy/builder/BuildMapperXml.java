package com.easy.builder;

import com.easy.bean.CommonConstants;
import com.easy.bean.FieldInfo;
import com.easy.bean.TableInfo;
import com.easy.enums.TypeConversionEnum;
import com.easy.utils.StringManipulationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * @Classname BuildMapperXml
 * @Description 什么也没有写哦~
 * @Date 2024/4/9 20:39
 * @Created by 憧憬
 */
public class BuildMapperXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(CommonConstants.PATH_BASE_RESOURCES_MAPPERS_XML);
        if (!folder.exists()) {
            try {
                folder.mkdirs();
            } catch (Exception e) {
                logger.error("创建Mapper.xml文件夹失败", e);
            }
        }
        String tableName = tableInfo.getBeanName();
        String poPackage = CommonConstants.PACKAGE_PO;
        String poName = StringManipulationTool.upperCaseFirstLetter(tableName);
        String beanName = StringManipulationTool.upperCaseFirstLetter(tableName + CommonConstants.SUFFIX_MAPPERS);
        String fileName = StringManipulationTool.upperCaseFirstLetter(tableName + CommonConstants.SUFFIX_MAPPERS + ".xml");
        File mapperXml = new File(folder, fileName);
        if (!mapperXml.exists()) {
            try {
                mapperXml.createNewFile();
            } catch (Exception e) {
                logger.error("创建Mapper.xml文件失败", e);
            }
        }

        try (OutputStream outputStream = Files.newOutputStream(mapperXml.toPath());
             OutputStreamWriter osw = new OutputStreamWriter(outputStream);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                    "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write(String.format("<mapper namespace=\"%s.%s\">", CommonConstants.PACKAGE_MAPPERS, beanName));
            bw.newLine();
            bw.newLine();

            bw.write("\t<!--实体映射-->");
            bw.newLine();
            bw.write(String.format("\t<resultMap id=\"base_result_map\" type=\"%s.%s\">", poPackage, poName));
            bw.newLine();

            StringBuilder qRColumnsBuild = new StringBuilder();
            int index = 0;
            FieldInfo idField = null;
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> m : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(m.getKey())) {
                    List<FieldInfo> values = m.getValue();
                    if (values.size() == 1) {
                        idField = values.get(0);
                        break;
                    }
                }
            }
            for (FieldInfo f : tableInfo.getFieldList()) {
                index++;
                if (index < tableInfo.getFieldList().size()) {
                    qRColumnsBuild.append(f.getFieldName()).append(", ");
                } else {
                    qRColumnsBuild.append(f.getFieldName());
                }
                bw.write(String.format("\t\t<!--%s-->", f.getComment()));
                bw.newLine();
                if (idField != null && f.getPropertyName().equals(idField.getPropertyName())) {
                    bw.write(String.format("\t\t<id column=\"%s\" property=\"%s\"/>", f.getFieldName(), f.getPropertyName()));
                } else {
                    bw.write(String.format("\t\t<result column=\"%s\" property=\"%s\"/>", f.getFieldName(), f.getPropertyName()));

                }
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();
            bw.newLine();

            bw.write("\t<!-- 通用查询结果列-->");
            bw.newLine();
            bw.write("\t<sql id=\"base_column_list\">");
            bw.newLine();
            bw.write(String.format("\t\t%s", qRColumnsBuild));
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            // 基础查询列
            bw.write("\t<!-- 基础查询条件 -->");
            bw.newLine();
            bw.write("\t<sql id=\"base_condition_filed\">");
            bw.newLine();

            for (FieldInfo f : tableInfo.getFieldList()) {
                bw.write(String.format("\t\t<if test=\"query.%s != null and query.%s != ''\">", f.getPropertyName(), f.getPropertyName()));
                bw.newLine();
                if (f.getJavaType().equals(TypeConversionEnum.SQL_DATE_TIME.getJavaType()) || f.getJavaType().equals(TypeConversionEnum.SQL_DATE.getJavaType())) {
                    bw.write(String.format("\t\t\t<![CDATA[ and  %s=str_to_date(#{query.%s},%s) ]]>", f.getPropertyName(), f.getPropertyName(), " '%Y-%m-%d'"));
                    bw.newLine();
                } else {
                    bw.write(String.format("\t\t\tand %s = #{query.%s}", f.getFieldName(), f.getPropertyName()));
                    bw.newLine();
                }
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            bw.write("\t<!-- 通用条件列-->");
            bw.newLine();
            bw.write("\t<sql id=\"base_condition\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"base_condition_filed\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            bw.write("\t<!-- 扩展查询条件 -->");
            bw.newLine();
            List<FieldInfo> extendFieldList = tableInfo.getExtendFieldList();
            bw.write("\t<sql id=\"query_condition\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"base_condition_filed\"/>");
            bw.newLine();
            for (FieldInfo f : extendFieldList) {
                String propertyName = f.getPropertyName();
                String fieldName = f.getFieldName();
                if (f.getJavaType().equals(TypeConversionEnum.SQL_STRING_TYPE.getJavaType())) {
                    bw.write(String.format("\t\t\t<if test=\"query.%s!= null  and query.%s!=''\">", propertyName, propertyName));
                    bw.newLine();
                    bw.write("\t\t\t\tand " + fieldName + " like concat('%', #{query." + propertyName + "}, '%')");
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();
                } else if (f.getJavaType().equals(TypeConversionEnum.SQL_DATE.getJavaType()) || f.getJavaType().equals(TypeConversionEnum.SQL_DATE_TIME.getJavaType())) {
                    bw.write(String.format("\t\t\t<if test=\"query.%s!= null and query.%s!=''\">", propertyName, propertyName));
                    bw.newLine();
                    if (f.getPropertyName().endsWith(CommonConstants.SUFFIX_BEAN_QUERY_TIME_START)) {
                        bw.write("\t\t\t\t<![CDATA[ and  " + fieldName + ">=str_to_date(#{query." + propertyName + "}, '%Y-%m-%d') ]]>");
                    } else {
                        bw.write("\t\t\t\t<![CDATA[ and  " + fieldName + "< date_sub(str_to_date(#{query." + propertyName + "},'%Y-%m-%d'),interval -1 day) ]]>");
                    }
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();

                }
            }
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();

            bw.write("\t<!-- 查询集合-->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT");
            bw.newLine();
            bw.write("\t\t<include refid=\"base_column_list\"/>");
            bw.newLine();
            bw.write("\t\tFROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\"query_condition\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy!=null\">");
            bw.newLine();
            bw.write("\t\t\torder by ${query.orderBy}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
            bw.write("        <if test=\"query.simplePage!=null\">\n" +
                    "            limit #{query.simplePage.start},#{query.simplePage.end}\n" +
                    "        </if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();


            bw.write("\t<!-- 查询数量-->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\"query_condition\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("生成xml文件错误", e);
        }

        logger.info(fileName + "已创建在：" + folder);
    }

    public static void build() {

    }
}
