package com.github.jeesun.thymeleaf.extras.dialect;

import com.github.jeesun.thymeleaf.extras.processor.HeadlinesElementTagProcessor;
import com.github.jeesun.thymeleaf.extras.processor.SayToAttributeTagProcessor;
import com.github.jeesun.thymeleaf.extras.processor.SayToPlanetAttributeTagProcessor;
import com.github.jeesun.thymeleaf.extras.processor.SelectElementTagProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义标签
 *
 * @author simon
 * @create 2018-09-02 15:03
 **/

public class HelloDialect extends AbstractProcessorDialect {

    /**
     * 定义方言名称
     */
    private static final String DIALECT_NAME = "Data Table Dialect";

    private JdbcTemplate jdbcTemplate;

    public HelloDialect(JdbcTemplate jdbcTemplate){
        super(DIALECT_NAME, "t", 1000);
        this.jdbcTemplate = jdbcTemplate;
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
        processors.add(new SayToAttributeTagProcessor(dialectPrefix));
        processors.add(new SayToPlanetAttributeTagProcessor(dialectPrefix, jdbcTemplate));
        processors.add(new HeadlinesElementTagProcessor(dialectPrefix, jdbcTemplate));
        processors.add(new SelectElementTagProcessor(dialectPrefix, jdbcTemplate));
        return processors;
    }
}
