package com.easy;

import com.easy.bean.TableInfo;
import com.easy.builder.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * @Classname RunApplication
 * @Description 什么也没有写哦~
 * @Date 2024/4/1 22:15
 * @Created by 憧憬
 */
//@SpringBootApplication
public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tables = BuildTable.getTables();
        BuildBase.execute();
        for(TableInfo t : tables){
            BuildPo.execute(t);
            BuildQuery.execute(t);
            BuildMapper.execute(t);
            BuildMapperXml.execute(t);
        }
    }
}
