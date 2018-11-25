package com.github.jeesun.thymeleaf.extras.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.jeesun.thymeleaf.extras.model.ListOption;
import com.github.jeesun.thymeleaf.extras.processor.DictTypeTagProcessor;
import com.github.jeesun.thymeleaf.extras.service.ListOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简单的字典service impl
 *
 * @author simon
 * @date 2018-11-25
 **/

public class ListOptionServiceImpl implements ListOptionService {
    private Logger log = LoggerFactory.getLogger(DictTypeTagProcessor.class);
    private CacheManager cacheManager;

    public ListOptionServiceImpl() {
    }

    public ListOptionServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public List<ListOption> cache(String dictName, JdbcTemplate jdbcTemplate, boolean cacheable) {
        Cache cache = null;
        if(null != cacheManager){
            cache = cacheManager.getCache("listOptionCache");
        }
        if (null != cache && null != cache.get(dictName) && cacheable){
            log.info("read from listOptionCache");
            return JSON.parseArray(String.valueOf(cache.get(dictName).get()), ListOption.class);
        }

        List<ListOption> listOptionList = new ArrayList<>();
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT type_code, type_name FROM t_dict_type WHERE type_group_id=(SELECT id FROM t_dict_type_group WHERE type_group_code=?)";
        log.info(sql);
        results = jdbcTemplate.queryForList(sql, new Object[]{dictName});
        for(int i = 0; i < results.size(); i++){
            ListOption listOption = new ListOption();
            listOption.setValue(String.valueOf(results.get(i).get("type_code")));
            listOption.setText(String.valueOf(results.get(i).get("type_name")));
            listOptionList.add(listOption);
        }
        log.info(JSON.toJSONString(listOptionList));
        if(null != cache && cacheable){
            cache.put(dictName, JSON.toJSONString(listOptionList));
        }
        return listOptionList;
    }

    @Override
    public List<ListOption> cacheSelect(String query, String order, JdbcTemplate jdbcTemplate, boolean cacheable) {
        Cache cache = null;
        if(null != cacheManager){
            cache = cacheManager.getCache("listOptionCache");
        }
        if (null != cache && null != cache.get(query + "-" + order) && cacheable){
            log.info("read from listOptionCache");
            return JSON.parseArray(String.valueOf(cache.get(query + "-" + order).get()), ListOption.class);
        }

        List<ListOption> listOptionList = new ArrayList<>();
        List<Map<String, Object>> results = new ArrayList<>();
        if (!StringUtils.isEmpty(query)) {
            String[] strArr = query.split(",");
            StringBuffer sql = new StringBuffer();
            if (strArr.length > 1) {
                if (2 == strArr.length) {
                    sql.append("SELECT " + strArr[1] + " FROM " + strArr[0]);
                } else if (3 == strArr.length) {
                    sql.append("SELECT " + strArr[1] + "," + strArr[2] + " FROM " + strArr[0]);
                } else {
                    StringBuilder where = new StringBuilder();
                    where.append(" WHERE ");
                    for(int i = 3; i < strArr.length; i++){
                        where.append(strArr[i]);
                        if (i != (strArr.length - 1)){
                            where.append(" AND ");
                        }
                    }
                    sql.append("SELECT " + strArr[1] + "," + strArr[2] + " FROM " + strArr[0] + where);
                }
                if (!StringUtils.isEmpty(order)) {
                    sql.append(" ORDER BY " + strArr[1] + " " + order);
                }
                log.info(sql.toString());
                results = jdbcTemplate.queryForList(sql.toString());
                if(null != results && results.size() > 0){
                    for(int i = 0; i < results.size(); i++){
                        ListOption listOption = new ListOption();
                        listOption.setValue(String.valueOf(results.get(i).get((strArr.length >= 3) ? strArr[2] : strArr[1])));
                        listOption.setText(String.valueOf(results.get(i).get(strArr[1])));
                        listOptionList.add(listOption);
                    }
                    if(null != cache && cacheable){
                        cache.put(query + "-" + order, JSON.toJSONString(listOptionList));
                    }
                }
            }
        }
        return listOptionList;
    }
}
