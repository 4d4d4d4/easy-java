package com.easy.builder;

import com.easy.bean.CommonConstants;
import com.easy.bean.TableInfo;
import com.easy.utils.StringManipulationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Classname BuildMapperXml
 * @Description 什么也没有写哦~
 * @Date 2024/4/9 20:39
 * @Created by 憧憬
 */
public class BuildMapperXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(CommonConstants.PATH_BASE_RESOURCES_MAPPERS_XML);
        if(!folder.exists()){
            try {
                folder.mkdirs();
            }catch (Exception e){
                logger.error("创建Mapper.xml文件夹失败", e);
            }
        }
        String tableName = tableInfo.getBeanName();
        String fileName = StringManipulationTool.upperCaseFirstLetter(tableName + CommonConstants.SUFFIX_MAPPERS + ".xml");
        File mapperXml = new File(folder, fileName);
        if(!mapperXml.exists()){
            try {
                mapperXml.createNewFile();
            } catch (Exception e) {
                logger.error("创建Mapper.xml文件失败", e);
            }
        }


        logger.info(fileName + "已创建在：" + folder + "路径下");
    }

    public static void build(){

    }
}
