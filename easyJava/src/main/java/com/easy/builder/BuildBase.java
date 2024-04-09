package com.easy.builder;

import com.easy.bean.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BuildBase
 * @Description 生成模板类
 * @Date 2024/4/6 12:39
 * @Created by 憧憬
 */
public class BuildBase {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    private static void build(List<String> headerInfoList,String fileName, String outputPath) {
        InputStream dateUtilStream = BuildBase.class.getClassLoader().getResourceAsStream("template/" + fileName + ".txt");
        char[] bytes = new char[256];
        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File text = new File(folder, fileName + ".java");
        int i = -1;
        try (InputStreamReader reader = new InputStreamReader(dateUtilStream, StandardCharsets.UTF_8);
             OutputStream out = Files.newOutputStream(text.toPath());
             OutputStreamWriter otw = new OutputStreamWriter(out);
             BufferedWriter bw = new BufferedWriter(otw)) {

            text.createNewFile();
            for(String head : headerInfoList){
                bw.write(head);
                bw.newLine();
            }
            bw.newLine();
            while ((i = reader.read(bytes)) != -1) {
                bw.write(bytes, 0, i);
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("生成模板类{}错误", fileName, e);
        }
    }

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();

        // 生成DateFormat枚举类
        headerInfoList.add("package " + CommonConstants.PACKAGE_ENUMS + ";");
        build(headerInfoList, "DateTimePatternEnum", CommonConstants.PATH_ENUMS);
        logger.info("枚举类{}，已生成在{}", "DateTimePatternEnum", CommonConstants.PATH_ENUMS);

        // 生成工具类
        headerInfoList.clear();
        headerInfoList.add("package " + CommonConstants.PACKAGE_UTILS + ";");
        build(headerInfoList, "DateUtil", CommonConstants.PATH_UTILS);
        logger.info("工具类{}，已生成在{}", "DateUtil", CommonConstants.PATH_UTILS);

        // 生成baseMapper
        headerInfoList.clear();
        headerInfoList.add("package " + CommonConstants.PACKAGE_MAPPERS + ";");
        build(headerInfoList, "BaseMapper", CommonConstants.PATH_MAPPERS);
        logger.info("Mapper基础类{}，以生成在{}", "BaseMapper", CommonConstants.PATH_MAPPERS);

    }
}
