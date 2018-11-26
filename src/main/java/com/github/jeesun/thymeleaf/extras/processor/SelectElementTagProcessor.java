package com.github.jeesun.thymeleaf.extras.processor;

import com.github.jeesun.thymeleaf.extras.model.ListOption;
import com.github.jeesun.thymeleaf.extras.service.ListOptionService;
import com.github.jeesun.thymeleaf.extras.service.impl.ListOptionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.context.SpringContextUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下拉框
 *
 * @author simon
 **/

public class SelectElementTagProcessor extends AbstractElementTagProcessor {
    private Logger log = LoggerFactory.getLogger(SelectElementTagProcessor.class);

    private static final String TAG_NAME = "select";
    private static final int PRECEDENCE = 1000;
    private JdbcTemplate jdbcTemplate;
    private ListOptionService listOptionService;

    public SelectElementTagProcessor(final String dialectPrefix, JdbcTemplate jdbcTemplate) {
        super(
                TemplateMode.HTML, // This processor will apply only to HTML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                TAG_NAME,          // Tag name: match specifically this tag
                true,              // Apply dialect prefix to tag name
                null,              // No attribute name: will match by tag name
                false,             // No prefix to be applied to attribute name
                PRECEDENCE);       // Precedence (inside dialect's own precedence)
        this.jdbcTemplate = jdbcTemplate;
        this.listOptionService = new ListOptionServiceImpl();
    }

    public SelectElementTagProcessor(final String dialectPrefix, JdbcTemplate jdbcTemplate, CacheManager cacheManager) {
        super(
                TemplateMode.HTML, // This processor will apply only to HTML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                TAG_NAME,          // Tag name: match specifically this tag
                true,              // Apply dialect prefix to tag name
                null,              // No attribute name: will match by tag name
                false,             // No prefix to be applied to attribute name
                PRECEDENCE);       // Precedence (inside dialect's own precedence)
        this.jdbcTemplate = jdbcTemplate;
        this.listOptionService = new ListOptionServiceImpl(cacheManager);
    }

    @Override
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {
        //html空格占位符
        String nbsp = "&nbsp;";
        List<String> options = new ArrayList<>();

        final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);

        final ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);

        final String order = tag.getAttributeValue("order");
        String classValue = tag.getAttributeValue("class");
        final String id = tag.getAttributeValue("id");
        final String name = tag.getAttributeValue("name");
        final String dataLiveSearch = tag.getAttributeValue("data-live-search");
        final String style = tag.getAttributeValue("style");
        final String multiple = tag.getAttributeValue("multiple");
        final Boolean allowEmpty = Boolean.parseBoolean(tag.getAttributeValue("allow-empty"));
        final String emptyMessage = tag.getAttributeValue("empty-message");
        Boolean cacheable;
        if(StringUtils.isEmpty(tag.getAttributeValue("cacheable"))){
            cacheable = true;
        }else{
            cacheable = Boolean.parseBoolean(tag.getAttributeValue("cacheable"));
        }
        //easyui data-options属性
        final String dataOptions = tag.getAttributeValue("data-options");
        if (StringUtils.isEmpty(classValue)) {
            classValue = "";
        }

        //属性规则：表名,显示的字段名[,作为option的value的字段名][,查询条件]
        final String query = tag.getAttributeValue("query");
        if (!StringUtils.isEmpty(query)) {
            List<ListOption> listOptions = listOptionService.cacheSelect(query, order, jdbcTemplate, cacheable);
            for (ListOption listOption : listOptions) {
                StringBuilder option = new StringBuilder();
                option.append("<option value=\"");
                option.append(listOption.getValue());
                option.append("\">");
                option.append(listOption.getText());
                option.append("</option>");
                options.add(option.toString());
            }
            if(allowEmpty){
                if (!StringUtils.isEmpty(emptyMessage)){
                    options.add(0, "<option value=\"\">" + emptyMessage + "</option>");
                }else{
                    options.add(0, "<option value=\"\">&nbsp;</option>");
                }
            }
        }

        final IModelFactory modelFactory = context.getModelFactory();

        final IModel model = modelFactory.createModel();
        model.add(modelFactory.createText("\n\t"));

        IProcessableElementTag openElementTag = modelFactory.createOpenElementTag("select", "class", classValue);
        if (!StringUtils.isEmpty(dataLiveSearch)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "data-live-search", dataLiveSearch);
        }
        if (!StringUtils.isEmpty(id)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "id", id);
        }
        if (!StringUtils.isEmpty(name)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "name", name);
        }
        if (!StringUtils.isEmpty(style)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "style", style);
        }
        if (!StringUtils.isEmpty(multiple)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "multiple", multiple);
        }
        if (!StringUtils.isEmpty(dataOptions)){
            openElementTag = modelFactory.setAttribute(openElementTag, "data-options", dataOptions);
        }
        model.add(openElementTag);
        model.add(modelFactory.createText("\n\t\t"));
        model.add(modelFactory.createText(HtmlEscape.unescapeHtml(String.join("\n\t\t", options))));
        model.add(modelFactory.createText("\n\t"));
        model.add(modelFactory.createCloseElementTag("select"));
        /*
         * Instruct the engine to replace this entire element with the specified model.
         */
        structureHandler.replaceWith(model, false);
    }
}
