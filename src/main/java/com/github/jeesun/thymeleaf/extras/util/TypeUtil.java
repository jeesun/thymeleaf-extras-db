package com.github.jeesun.thymeleaf.extras.util;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author simon
 * @create 2018-08-21 15:04
 **/

public class TypeUtil {
    private static ApplicationContext applicationContext = SpringUtils.getApplicationContext();
    private static JdbcTemplate jdbcTemplate;
    private static TypeUtil instance = new TypeUtil();

    private TypeUtil(){

    }

    public static TypeUtil getInstance(){
        return instance;
    }

    public TypeUtil setJdbcTemplate(JdbcTemplate jdbcTemplate){
        TypeUtil.jdbcTemplate = jdbcTemplate;
        return instance;
    }

    public String getEmail(String queryInfo){
        String[] strArr = queryInfo.split(",");
        //数据字典组编码 或 自定义字典(格式：表名,编码,显示文本)
        List<String> email = jdbcTemplate.queryForList("SELECT " + strArr[0] + " FROM " + strArr[1] + " WHERE "+ strArr[2] + "='" + strArr[3] + "'", String.class);
        return (null == email) ? "" : String.join(",", email);
    }
}
