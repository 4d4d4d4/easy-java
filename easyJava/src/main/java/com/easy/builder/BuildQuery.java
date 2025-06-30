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
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BuildQuery
 * @Description 生成query代码
 * @Date 2024/4/4 17:34
 * @Created by 憧憬
 */
public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(CommonConstants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + CommonConstants.SUFFIX_BEAN_PARAM;
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        File poFile = new File(folder, StringManipulationTool.upperCaseFirstLetter(className) + ".java");
        try {
            poFile.createNewFile();
            // 文件字节输出流
            out = Files.newOutputStream(poFile.toPath());
            // 文件字符输出流
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            // 文件缓冲字符输出流
            bw = new BufferedWriter(outw);
            // 写入包名
            bw.write(String.format("package %s;", CommonConstants.PACKAGE_QUERY));
            bw.newLine();
            bw.newLine();
            if(tableInfo.isHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;\n");
            }
            if(tableInfo.isHaveDate() || tableInfo.isHaveDateTime()){
                bw.write("import java.util.Date;\n");
                bw.newLine();
                bw.newLine();
            }
            // 构建类注释
            BuildComment.createdClassComment(bw, tableInfo.getComment() + "查询对象");
            bw.write("public class " + StringManipulationTool.upperCaseFirstLetter(className) + " extends BaseQuery" + " {");
            bw.newLine();
            List<FieldInfo> tempList = new ArrayList<>(tableInfo.getFieldList());
            List<FieldInfo> extendList = new ArrayList<>();
            // 写入属性 以及 扩展属性
            for(FieldInfo field : tableInfo.getFieldList()){
                BuildComment.createdFieldComment(bw, field.getComment());
                bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
                if(field.getJavaType().equals("String")){
                    String propertyName = field.getPropertyName() + CommonConstants.SUFFIX_BEAN_QUERY_FUZZY;
                    bw.write("\tprivate " + field.getJavaType() + " " + propertyName  + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(field.getJavaType());
                    fuzzyField.setPropertyName(propertyName);
                    tempList.add(fuzzyField);
                    extendList.add(fuzzyField);
                }
                if(field.getJavaType().equals("Date")){
                    bw.write("\tprivate String " + field.getPropertyName() + CommonConstants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.write("\tprivate String " + field.getPropertyName() + CommonConstants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                    bw.newLine();
                    FieldInfo timeStartField = new FieldInfo();
                    timeStartField.setJavaType("String");
                    timeStartField.setPropertyName(field.getPropertyName() + CommonConstants.SUFFIX_BEAN_QUERY_TIME_START);
                    tempList.add(timeStartField);
                    extendList.add(timeStartField);

                    FieldInfo timeEndField = new FieldInfo();
                    timeEndField.setJavaType("String");
                    timeEndField.setPropertyName(field.getPropertyName() + CommonConstants.SUFFIX_BEAN_QUERY_TIME_END);
                    tempList.add(timeEndField);
                    extendList.add(timeEndField);
                }
            }

//            tableInfo.setExtendFieldList(extendList);

            // String类型的参数
            for(FieldInfo f : tempList){
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
            bw.write("}");
            bw.flush();

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
            logger.info("类文件已生成在：{}", folder.getAbsolutePath());
        }
    }
    private static String dateFormatPropertyName(String propertyName, String pattern){
        String formatStr = "DateUtil.format(%s, DateTimePatternEnum.%s.getPattern())";
        return String.format(formatStr, propertyName , pattern);
    }
}
