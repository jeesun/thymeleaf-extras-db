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
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典
 *
 * @author simon
 * @date 2018-09-28
 **/

public class DictTypeTagProcessor extends AbstractElementTagProcessor {
    private Logger log = LoggerFactory.getLogger(DictTypeTagProcessor.class);
    private static final String TAG_NAME = "dict";
    private static final int PRECEDENCE = 1000;
    private JdbcTemplate jdbcTemplate;
    private ListOptionService listOptionService;

    public DictTypeTagProcessor(final String dialectPrefix, JdbcTemplate jdbcTemplate) {
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

    public DictTypeTagProcessor(final String dialectPrefix, JdbcTemplate jdbcTemplate, CacheManager cacheManager) {
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
            ITemplateContext context,
            IProcessableElementTag tag,
            IElementTagStructureHandler structureHandler) {
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
        final String dictName = tag.getAttributeValue("dict-name");
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

        List<ListOption> listOptionList = listOptionService.cache(dictName, jdbcTemplate, cacheable);
        for(ListOption listOption : listOptionList){
            options.add("<option value=\"" + listOption.getValue() + "\">" + listOption.getText() + "</option>");
        }

        if (allowEmpty){
            if(!StringUtils.isEmpty(emptyMessage)){
                options.add(0, "<option value=\"\">" + emptyMessage + "</option>");
            }else{
                options.add(0, "<option value=\"\">&nbsp;</option>");
            }
        }

        final IModelFactory modelFactory = context.getModelFactory();

        final IModel model = modelFactory.createModel();

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
