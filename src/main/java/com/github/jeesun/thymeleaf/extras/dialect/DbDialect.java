package com.github.jeesun.thymeleaf.extras.dialect;

import com.github.jeesun.thymeleaf.extras.processor.*;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义标签
 *
 * @author simon
 **/

public class DbDialect extends AbstractProcessorDialect {

    /**
     * 定义方言名称
     */
    private static final String DIALECT_NAME = "Database Dialect";

    private JdbcTemplate jdbcTemplate;
    private CacheManager cacheManager;

    public DbDialect(JdbcTemplate jdbcTemplate){
        super(DIALECT_NAME, "t", 1000);
        this.jdbcTemplate = jdbcTemplate;
    }

    public DbDialect(JdbcTemplate jdbcTemplate, CacheManager cacheManager){
        super(DIALECT_NAME, "t", 1000);
        this.jdbcTemplate = jdbcTemplate;
        this.cacheManager = cacheManager;
    }

    /*
     * Initialize the dialect's processors.
     *
     * Note the dialect prefix is passed here because, although we set
     * "hello" to be the dialect's prefix at the constructor, that only
     * works as a default, and at engine configuration time the user
     * might have chosen a different prefix to be used.
     */
    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new SelectElementTagProcessor(dialectPrefix, jdbcTemplate, cacheManager));
        processors.add(new DictTypeTagProcessor(dialectPrefix, jdbcTemplate, cacheManager));
        return processors;
    }
}
