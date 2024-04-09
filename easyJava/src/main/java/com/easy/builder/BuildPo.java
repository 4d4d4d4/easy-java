package com.easy.builder;

import com.easy.bean.CommonConstants;
import com.easy.bean.FieldInfo;
import com.easy.bean.TableInfo;
import com.easy.enums.TypeConversionEnum;
import com.easy.utils.DateUtils;
import com.easy.utils.StringManipulationTool;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @Classname BuildPo
 * @Description 什么也没有写哦~
 * @Date 2024/4/4 17:34
 * @Created by 憧憬
 */
public class BuildPo{
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(CommonConstants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        File poFile = new File(folder, StringManipulationTool.upperCaseFirstLetter(tableInfo.getBeanName() + ".java"));
        try {
            poFile.createNewFile();
            // 文件字节输出流
            out = Files.newOutputStream(poFile.toPath());
            // 文件字符输出流
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            // 文件缓冲字符输出流
            bw = new BufferedWriter(outw);
            //
            bw.write(String.format("package %s;", CommonConstants.PACKAGE_PO));
            bw.newLine();
            bw.newLine();
            if(tableInfo.isHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;\n");
            }
            if(CommonConstants.IGNORE_BEAN_TOJSON_FIELDS.length > 0){
                bw.write(CommonConstants.IGNORE_BEAN_TOJSON_CLASS);
                bw.newLine();
            }
            if(tableInfo.isHaveDate() || tableInfo.isHaveDateTime()){
                bw.write("import java.util.Date;\n");
                bw.newLine();
                bw.write("import " + CommonConstants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import " + CommonConstants.PACKAGE_UTILS + ".DateUtil;");
                bw.newLine();
                bw.write(CommonConstants.BEAN_DATA_SERIALIZATION_FORMAT_CLASS);
                bw.newLine();
                bw.write(CommonConstants.BEAN_DATA_DESERIALIZATION_FORMAT_CLASS);
                bw.newLine();
            }

            bw.write("import java.io.Serializable;");
            bw.newLine();

            // 构建类注释
            BuildComment.createdClassComment(bw, tableInfo.getComment());
            bw.write("public class " + StringManipulationTool.upperCaseFirstLetter(tableInfo.getBeanName()) + " implements Serializable {");
            bw.newLine();

            // 写入属性
            for(FieldInfo field : tableInfo.getFieldList()){
                BuildComment.createdFieldComment(bw, field.getComment());
                if(TypeConversionEnum.getJavaTypeBySqlType(field.getSqlType()).getDescription().equals("DateTime类型")){
                    bw.write("    " + String.format(CommonConstants.BEAN_DATA_SERIALIZATION_FORMAT, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                    bw.write("    " + String.format(CommonConstants.BEAN_DATA_DESERIALIZATION_FORMAT, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }
                if(TypeConversionEnum.getJavaTypeBySqlType(field.getSqlType()).getDescription().equals("Date类型")){
                    bw.write("    " + String.format(CommonConstants.BEAN_DATA_SERIALIZATION_FORMAT, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("    " + String.format(CommonConstants.BEAN_DATA_DESERIALIZATION_FORMAT, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                if(ArrayUtils.contains(CommonConstants.IGNORE_BEAN_TOJSON_FIELDS, field.getFieldName())){
                    bw.write("    " + CommonConstants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.newLine();
                }
                bw.write("    private " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
            }
            for(FieldInfo f : tableInfo.getFieldList()){
                String tempField = StringManipulationTool.upperCaseFirstLetter(f.getPropertyName());
                bw.write("\tpublic void set" + tempField + "(" + f.getJavaType() + " " + f.getPropertyName() + ") {");
                bw.newLine();
                bw.write("\t\tthis." + f.getPropertyName() + " = " + f.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t }\n");
                bw.newLine();

                bw.write("\tpublic " + f.getJavaType() + " get" + tempField + "()" + " {");
                bw.newLine();
                bw.write("\t\treturn " + "this." + f.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}\n");
                bw.newLine();
            }
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn \"");
            int index = 0;
            for(FieldInfo f : tableInfo.getFieldList()){
                index++;
                String propertyName = f.getPropertyName();
                if (TypeConversionEnum.getJavaTypeBySqlType(f.getSqlType()).getDescription().equals("Date类型")){
                    propertyName = dateFormatPropertyName(propertyName, "YYYY_MM_DD");
                } else if (TypeConversionEnum.getJavaTypeBySqlType(f.getSqlType()).getDescription().equals("DateTime类型")) {
                    propertyName = dateFormatPropertyName(propertyName, "YYYY_MM_DD_HH_MM_SS");
                }
                if(index == tableInfo.getFieldList().size()){
                    bw.write(f.getComment() + ":\" + (" + f.getPropertyName() + " == null" + " ? \"null\" : " + propertyName + ");");
                    break;
                }
                bw.write(f.getComment() + ":\" + (" + f.getPropertyName() + " == null" + " ? \"null\" : " + propertyName + ")+\", ");
            }
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.write("}");
            bw.flush();

            logger.info("类文件已生成在：{}", folder.getAbsolutePath());
        }catch (Exception e){
            logger.error("类文件生成失败：", e);
        }finally {
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(outw != null){
                try {
                    outw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private static String dateFormatPropertyName(String propertyName, String pattern){
        String formatStr = "DateUtil.format(%s, DateTimePatternEnum.%s.getPattern())";
        return String.format(formatStr, propertyName , pattern);
    }
}
