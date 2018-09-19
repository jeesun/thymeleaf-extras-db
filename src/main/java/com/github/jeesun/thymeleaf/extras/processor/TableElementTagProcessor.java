package com.github.jeesun.thymeleaf.extras.processor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 表格
 *
 * @author simon
 * @date 2018-09-18
 **/

public class TableElementTagProcessor extends AbstractProcessor {
    private static final String TAG_NAME = "select";
    private static final int PRECEDENCE = 1000;
    private JdbcTemplate jdbcTemplate;

    public TableElementTagProcessor(TemplateMode templateMode, int precedence) {
        super(templateMode, precedence);
    }
}
