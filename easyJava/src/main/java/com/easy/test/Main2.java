package com.easy.test;

import cn.hutool.json.JSONUtil;
import com.easy.bean.FieldInfo;
import com.easy.bean.TableInfo;
import com.easy.builder.BuildTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qing yuan cheng
 * @date 2025/6/27
 * @time 上午10:53
 */
public class Main2 {
    private static final Logger log = LoggerFactory.getLogger(Main2.class);

    public static void main(String[] args) {
        List<TableInfo> tables = BuildTable.getTables();
//        try( BufferedWriter writer = new BufferedWriter( new FileWriter("tables_info_json.txt"));) {
//            writer.write(JSONUtil.toJsonStr(tables));
//            writer.flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("所有表信息的JSON数据： 【{}】", JSONUtil.toJsonStr(tables));
        List<String> ignoreTableNames = Arrays.asList("ceshi_note", "colleges_project_member2", "demo", "demo_field_def_val_main", "demo_field_def_val_sub",
                "jeecg_monthly_growth_analysis", "jeecg_order_customer", "jeecg_order_main","jeecg_project_nature_income", "jeecg_order_ticket",
                "jimu_dict", "jimu_dict_item", "jimu_report", "jimu_report_data_source", "jimu_report_db", "jimu_report_db_field", "jimu_report_db_param", "jimu_report_link", "jimu_report_map", "jimu_report_share", "joa_demo",
                "onl_auth_data", "onl_auth_page", "onl_auth_relation", "onl_cgform_button", "onl_cgform_enhance_java", "onl_cgform_enhance_js", "onl_cgform_enhance_sql", "onl_cgform_field", "onl_cgform_head", "onl_cgform_index", "onl_cgreport_head", "onl_cgreport_item", "onl_cgreport_param", "onl_drag_comp", "onl_drag_dataset_head", "onl_drag_dataset_item", "onl_drag_dataset_param", "onl_drag_page", "onl_drag_page_comp",
                "rep_demo_dxtj", "rep_demo_employee", "rep_demo_gongsi", "rep_demo_jianpiao", "sys_dict_item_20231128", "sys_dict_item_tm", "sys_dict_item_tm2",
                "tmp_report_data_income", "tmp_report_data_1", "t_payment_business_receipt");

        tables = tables.stream().filter((e) ->
                (e.getFieldList() != null && e.getFieldList().size() > 0)
                        &&
                        !e.getTableName().toLowerCase().contains("test")
                        &&
                        !e.getTableName().toLowerCase().contains("demo")
                        &&
                        !ignoreTableNames.contains(e.getTableName()))
                .collect(Collectors.toList());

        List<Map<String, Object>> tableList = new ArrayList<>();
        List<Map<String, Object>> catalogue = new ArrayList<>();
        // 每个表
        for (TableInfo table : tables) {
            Map<String, Object> tableMap = new HashMap<>();
            Map<String, Object> objectItemMap = new HashMap<>();
            tableMap.put("tableName", table.getTableName().toLowerCase());
            tableMap.put("tableComment", table.getComment());
            objectItemMap.put("tableName", table.getTableName());
            objectItemMap.put("comment", table.getComment());
            List<Map<String, Object>> fields = new ArrayList<>();
            int filedIndex = 1;
            for (FieldInfo filed : table.getFieldList()) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("filedIndex", filedIndex ++);
                fieldMap.put("nameEn", filed.getFieldName().toLowerCase());
                fieldMap.put("dataType", filed.getSqlType());
                String range = "";
                if(filed.getFieldName().toLowerCase().contains("del")){
                    int min = 0;
                    int max = 1;
                    int randomInt = (int) (Math.random() * (max - min + 1)) + min;
                    range = randomInt + "";
                }else if(filed.getFieldName().toLowerCase().contains("status") || filed.getFieldName().toLowerCase().contains("state")){
                    int min = 1;
                    int max = 3;
                    int randomInt = (int) (Math.random() * (max - min)) + min;
                    range = randomInt + "";
                }
                fieldMap.put("range", range);
                fieldMap.put("fieldComment", filed.getComment());
                fieldMap.put("p", filed.getKey().equals("PRI") ? "*" : "");
                fieldMap.put("f", "");
                fieldMap.put("i", "");
                fieldMap.put("n", filed.getNull() ? "" : "*");
                fieldMap.put("d", "");
                fieldMap.put("u", "");
                fieldMap.put("c", "");
                fields.add(fieldMap);
            }
            tableMap.put("fields", fields);
            tableList.add(tableMap);
            catalogue.add(objectItemMap);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tables_info_map_json.txt"));) {
            writer.write(JSONUtil.toJsonStr(tableList));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            exportMultiTableToWord(tableList, catalogue, "tableTemplate2.ftl", "F:\\TEST.docx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void exportMultiTableToWord(List<Map<String, Object>> tableList, List<Map<String, Object>> catalogue, String templateName, String outputFilePath) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding("UTF-8");
        // 模板目录(绝对路径)
        cfg.setDirectoryForTemplateLoading(new File("D:\\java_study\\easy-java\\easyJava\\src\\main\\resources\\template"));

        Template template = cfg.getTemplate(templateName);

        Map<String, Object> root = new HashMap<>();
        root.put("tables", tableList);
        root.put("catalogue", catalogue);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8))) {
            template.process(root, out);
        }

        System.out.println("导出成功：" + outputFilePath);
    }

}
