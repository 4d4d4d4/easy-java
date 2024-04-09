package com.easy.builder;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.easy.bean.CommonConstants;
import com.easy.bean.FieldInfo;
import com.easy.bean.TableInfo;
import com.easy.utils.StringManipulationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * @Classname BuildMapper
 * @Description 什么也没有写哦~
 * @Date 2024/4/7 23:44
 * @Created by 憧憬
 */
public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(CommonConstants.PATH_MAPPERS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String beanName = StringManipulationTool.upperCaseFirstLetter(tableInfo.getBeanName());
        String bQueryName = beanName + CommonConstants.SUFFIX_BEAN_PARAM;

        File file = new File(folder, beanName + CommonConstants.SUFFIX_MAPPERS + ".java");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("mappers文件生成错误", e);
            }
        }
        try(OutputStream out = Files.newOutputStream(file.toPath());
            OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write("package " + CommonConstants.PACKAGE_MAPPERS + ";");
            bw.newLine();
            bw.newLine();
//            bw.write("import " + CommonConstants.PACKAGE_PO + "." + beanName + ";");
//            bw.newLine();
//            bw.write("import " + CommonConstants.PACKAGE_QUERY + "." + bQueryName+ ";");
//            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();

            BuildComment.createdMapperComment(bw, tableInfo.getComment());
            bw.write("public interface " + beanName + CommonConstants.SUFFIX_MAPPERS + "<T, P>" + " extends " + "BaseMapper<" + "T" + ", " + " P" + "> {");
            bw.newLine();
            bw.newLine();

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for(Map.Entry<String, List<FieldInfo>> e : keyIndexMap.entrySet()){
                StringBuilder methodName = new StringBuilder();
                StringBuilder comment = new StringBuilder("根据");
                StringBuilder methodParam = new StringBuilder();
                List<FieldInfo> value = e.getValue();
                Integer index = 0;
                for (FieldInfo f : value){
                    String pName = StringManipulationTool.upperCaseFirstLetter(f.getPropertyName());
                    if((++index) < value.size()){
                        methodName.append(pName).append("And");
                        comment.append(pName).append("、");
                        methodParam.append("@Param(\"" + f.getPropertyName() + "\") " + f.getJavaType() + " " +f.getPropertyName() + ", ");
                        continue;
                    }

                    methodParam.append("@Param(\"" + f.getPropertyName() + "\") " + f.getJavaType() + " " +f.getPropertyName());
                    methodName.append(pName);
                    comment.append(pName);
                }
                BuildComment.createdFieldComment(bw, comment + "查询");
                bw.write("\tT selectBy" + methodName + "(" + methodParam + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.createdFieldComment(bw, comment + "更新");
                bw.write("\tInteger updateBy" + methodName + "(" + methodParam + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.createdFieldComment(bw, comment.toString() + "删除");
                bw.write("\tInteger deleteBy" + methodName + "(" + methodParam + ");");
                bw.newLine();
                bw.newLine();
            }
            bw.write("}");
        } catch (IOException e) {
            logger.error("生成mapper文件失败");
        }
        logger.info("Mapper文件已生成在：{}", folder.getAbsolutePath());
    }
    public static void build(){

    }
}
