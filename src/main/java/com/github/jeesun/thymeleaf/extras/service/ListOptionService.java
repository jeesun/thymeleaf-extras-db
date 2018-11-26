package com.github.jeesun.thymeleaf.extras.service;

import com.github.jeesun.thymeleaf.extras.model.ListOption;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 简单的字典service
 *
 * @author simon
 **/

public interface ListOptionService {
    List<ListOption> cache(String dictName, JdbcTemplate jdbcTemplate, boolean cacheable);
    List<ListOption> cacheSelect(String query, String order, JdbcTemplate jdbcTemplate, boolean cacheable);
}
