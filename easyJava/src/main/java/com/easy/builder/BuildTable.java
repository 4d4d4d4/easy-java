package com.easy.builder;

import com.easy.bean.CommonConstants;
import com.easy.bean.FieldInfo;
import com.easy.bean.TableInfo;
import com.easy.enums.TypeConversionEnum;
import com.easy.utils.PropertiesUtils;
import com.easy.utils.StringManipulationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * @Classname BuildTable
 * @Description 创建表操作
 * @Date 2024/4/1 22:03
 * @Created by 憧憬
 */
public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    private static final String SQL_SHOW_TABLE_STATUS = "show table status";
    // 展示字段信息 %s 是表名的占位符
    private static final String SQL_SHOW_Fields_STATUS = "show full COLUMNS from %s";
    private static final String SQL_SHOW_TABLE_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String username = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        String url = PropertiesUtils.getString("db.url");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("连接数据库异常：", e);
            throw new RuntimeException(e);
        }
    }

    public static List<TableInfo> getTables() {
        // 预编译的sql语句 放置参数 执行sql语句等
        PreparedStatement statement = null;
        ResultSet tableResult = null;
        List<TableInfo> tables = new ArrayList<>();
        try {
            statement = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = statement.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
//                System.out.println("开始读取表：" + tableName); // 放在 while 里面最上面
                String tableComment = tableResult.getString("comment");
//                System.out.println(String.format("表名：%s, 表备注：%s", tableName, tableComment));

                String beanName = tableName;
                // 是否去除表名前缀
                if (CommonConstants.IGNORE_TABLE_PREFIX) {
                    beanName = formatTableName(tableName.substring(tableName.indexOf("_")), false, '_'); //_category_table --> CategoryTable
                }
//                logger.info("beanName:" + beanName);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(tableComment);
                tableInfo.setBeanParamName(beanName + StringManipulationTool.upperCaseFirstLetter(CommonConstants.SUFFIX_BEAN_PARAM));

                // 读取字段信息
                List<FieldInfo> fieldInfos = readFieldInfo(tableInfo);
                tableInfo.setFieldList(fieldInfos);

                getKeyIndexInfo(tableInfo);

                tables.add(tableInfo);
            }
        } catch (Exception e) {
            logger.error("获取表信息异常：", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//        logger.info("tables: {}", JSONUtil.toJsonStr(tables));
        return tables;
    }

    // 读取索引信息
    private static void getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Map<String, FieldInfo> tempMap = new HashMap<>();
            for (FieldInfo f : tableInfo.getFieldList()) {
                tempMap.put(f.getFieldName(), f);
            }
            statement = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String tableName = resultSet.getString("table"); // 表名
                String keyName = resultSet.getString("key_name"); // 索引名
                Integer unique = resultSet.getInt("non_unique"); // 唯一性
                String columnName = resultSet.getString("column_name");// 字段名
                String comment = resultSet.getString("comment"); // 注释
//                if(unique == 1){
//                    continue;
//                }
                List<FieldInfo> fieldInfos = tableInfo.getKeyIndexMap().get(keyName);
                if (null == fieldInfos) {
                    fieldInfos = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, fieldInfos);
                }
                fieldInfos.add(tempMap.get(columnName));
//                for(FieldInfo f : tableInfo.getFieldList()){
//                    if(f.getFieldName().equals(columnName)){
//                        System.out.println("f:  " + f.toString());
//                        fieldInfos.add(f);
//                        break;
//                    }
//                }
//                tableInfo.getKeyIndexMap().put(keyName, fieldInfos);
            }
        } catch (Exception e) {
            logger.error("表中字段索引获取异常:", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    // 读取字段信息
    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        PreparedStatement statement = null;
        ResultSet fieldsResult = null;
        ArrayList<FieldInfo> fieldInfoList = new ArrayList();
        // 扩展字段信息
        List<FieldInfo> fieldExtendList = new ArrayList<>();

        try {
            statement = conn.prepareStatement(String.format(SQL_SHOW_Fields_STATUS, tableInfo.getTableName()));
//            System.out.println(statement.toString());
            fieldsResult = statement.executeQuery();
            while (fieldsResult.next()) {
                FieldInfo fieldInfo = new FieldInfo();
                String fieldName = fieldsResult.getString("field");
                String type = fieldsResult.getString("type");
                fieldInfo.setSqlType(type);
                if (type.contains("(")) {
                    type = type.substring(0, type.indexOf("("));
                }
                String collation = fieldsResult.getString("collation");
                String isnull = fieldsResult.getString("null");
                String key = fieldsResult.getString("key");
                fieldInfo.setNull(isnull.equals("YES"));
                fieldInfo.setKey(key);
                String defaultValue = fieldsResult.getString("default");
                String extra = fieldsResult.getString("extra");
                String privileges = fieldsResult.getString("privileges");
                String comment = fieldsResult.getString("comment");
                fieldInfo.setComment(comment);
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setPropertyName(formatFieldName(fieldName, '_'));

                fieldInfo.setIsAutoIncrement("auto_increment".equals(extra));

                String javaType = TypeConversionEnum.getJavaTypeBySqlType(type).getJavaType();
                if (javaType.equals(TypeConversionEnum.SQL_DATE_TIME.getJavaType())) {
                    FieldInfo startFieldInfo = new FieldInfo();
                    startFieldInfo.setJavaType(TypeConversionEnum.SQL_DATE_TIME.getJavaType());
                    startFieldInfo.setPropertyName(formatFieldName(fieldName, '_') + CommonConstants.SUFFIX_BEAN_QUERY_TIME_START);
                    startFieldInfo.setFieldInfos(fieldName);

                    FieldInfo endFieldInfo = new FieldInfo();
                    endFieldInfo.setJavaType(TypeConversionEnum.SQL_DATE_TIME.getJavaType());
                    endFieldInfo.setPropertyName(formatFieldName(fieldName, '_') + CommonConstants.SUFFIX_BEAN_QUERY_TIME_END);
                    endFieldInfo.setFieldName(fieldName);

                    fieldExtendList.add(startFieldInfo);
                    fieldExtendList.add(endFieldInfo);
                    tableInfo.setHaveDateTime(true);
                } else if (javaType.equals(TypeConversionEnum.SQL_DATE.getJavaType())) {
                    FieldInfo startFieldInfo = new FieldInfo();
                    startFieldInfo.setJavaType(TypeConversionEnum.SQL_DATE.getJavaType());
                    startFieldInfo.setPropertyName(formatFieldName(fieldName, '_') + CommonConstants.SUFFIX_BEAN_QUERY_TIME_START);
                    startFieldInfo.setFieldInfos(fieldName);

                    FieldInfo endFieldInfo = new FieldInfo();
                    endFieldInfo.setJavaType(TypeConversionEnum.SQL_DATE.getJavaType());
                    endFieldInfo.setPropertyName(formatFieldName(fieldName, '_') + CommonConstants.SUFFIX_BEAN_QUERY_TIME_END);
                    endFieldInfo.setFieldName(fieldName);

                    fieldExtendList.add(startFieldInfo);
                    fieldExtendList.add(endFieldInfo);
                    tableInfo.setHaveDate(true);
                } else if (javaType.equals(TypeConversionEnum.SQL_DECIMAL.getJavaType())) {
                    tableInfo.setHaveBigDecimal(true);
                }
                if(javaType.equals(TypeConversionEnum.SQL_STRING_TYPE.getJavaType())){
                    FieldInfo stringFieldInfo = new FieldInfo();
                    stringFieldInfo.setJavaType(TypeConversionEnum.SQL_STRING_TYPE.getJavaType());
                    stringFieldInfo.setFieldName(fieldName);
                    stringFieldInfo.setPropertyName(formatFieldName(fieldName, '_') + CommonConstants.SUFFIX_BEAN_QUERY_FUZZY);
                    fieldExtendList.add(stringFieldInfo);
                }
                fieldInfo.setJavaType(javaType);
                fieldInfoList.add(fieldInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (fieldsResult != null) {
                try {
                    fieldsResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        tableInfo.setExtendFieldList(fieldExtendList);
        return fieldInfoList;
    }

    // 驼峰命名1
    private static String formatTableName(String tableName, Boolean upperCaseFirstLetter, char c) {
        if (!tableName.contains(String.valueOf(c))) {
            return upperCaseFirstLetter ? StringManipulationTool.upperCaseFirstLetter(tableName) : tableName;
        }
        String substring = tableName.substring(tableName.indexOf(c));
        int index;
        while ((index = substring.indexOf(c)) != -1) {
            Character s = substring.charAt(index + 1);
            if (Character.isLowerCase(s)) {
                s = Character.toUpperCase(s);
            }
            substring = substring.substring(0, index) + s + substring.substring(index + 2);
        }
        return upperCaseFirstLetter ? substring : Character.toLowerCase(substring.charAt(0)) + substring.substring(1);
    }

    // 字段驼峰命名
    private static String formatFieldName(String fieldName, char c) {
        String[] split = fieldName.split("_");
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append(split[0]);
        for (int i = 1; i < split.length; i++) {
            resultBuffer.append(Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));
        }
        return resultBuffer.toString();
    }

}

