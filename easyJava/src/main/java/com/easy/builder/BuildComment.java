package com.easy.builder;

import com.easy.bean.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname BuildComment
 * @Description 什么也没有写哦~
 * @Date 2024/4/5 16:18
 * @Created by 憧憬
 */
public class BuildComment {
    public static void createdClassComment(BufferedWriter bw, String classComment) throws IOException {
        bw.newLine();
        bw.write("/**\n");
        bw.write(" * @Description: " + classComment);
        bw.newLine();
        bw.write(" * @Date: " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
        bw.newLine();
        bw.write(" * @Created by: " + (!StringUtils.isEmpty(CommonConstants.COMMENT_AUTHOR_NAME)?CommonConstants.COMMENT_AUTHOR_NAME:System.getProperty("user.name")));
        bw.newLine();
        bw.write("  */");
        bw.newLine();

    }
    public static void createdFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
        bw.write("    /**\n");
        bw.write("     * " + fieldComment);
        bw.newLine();
        bw.write("     */");
        bw.newLine();
    }
    public static void createdMapperComment(BufferedWriter bw, String fieldComment) throws IOException{
        bw.write("/**\n");
        bw.write(" * @Description " + fieldComment + " 操作接口类");
        bw.newLine();
        bw.write("  */");
        bw.newLine();
    }
    public static void createdMethodComment(){

    }

}
