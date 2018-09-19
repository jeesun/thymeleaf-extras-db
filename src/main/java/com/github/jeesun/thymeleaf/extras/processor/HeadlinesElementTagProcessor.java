package com.github.jeesun.thymeleaf.extras.processor;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
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

import java.util.List;

/**
 * @author simon
 * @date 2018-09-16
 **/

public class HeadlinesElementTagProcessor extends AbstractElementTagProcessor {

    private static final String TAG_NAME = "col";
    private static final int PRECEDENCE = 1000;
    private JdbcTemplate jdbcTemplate;


    public HeadlinesElementTagProcessor(final String dialectPrefix, JdbcTemplate jdbcTemplate){
        super(
                TemplateMode.HTML, // This processor will apply only to HTML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                TAG_NAME,          // Tag name: match specifically this tag
                true,              // Apply dialect prefix to tag name
                null,              // No attribute name: will match by tag name
                false,             // No prefix to be applied to attribute name
                PRECEDENCE);       // Precedence (inside dialect's own precedence)
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {
        /*
         * In order to evaluate the attribute value as a Thymeleaf Standard Expression,
         * we first obtain the parser, then use it for parsing the attribute value into
         * an expression object, and finally execute this expression object.
         */
        final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);

        /*
         * Obtain the Spring application context.
         */
        final ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);

        /*
         * Read the 'order' attribute from the tag. This optional attribute in our tag
         * will allow us to determine whether we want to show a random headline or
         * only the latest one ('latest' is default).
         */
        System.out.println(JSON.toJSONString(tag));
        /*System.out.println(JSON.toJSONString(tag.getElementCompleteName()));
        System.out.println(JSON.toJSONString(tag.getElementDefinition()));
        System.out.println(JSON.toJSONString(tag.getTemplateMode()));*/
        System.out.println(JSON.toJSONString(structureHandler));


        final String order = tag.getAttributeValue("order");

        final String data = tag.getAttributeValue("data");
        final IStandardExpression expression = parser.parseExpression(context, data);
        final String planet = String.valueOf(expression.execute(context));
        System.out.println("planet=" + planet);

        final String query = tag.getAttributeValue("query");
        String[] strArr = query.split(",");

        //dictionary属性规则：表名,关联字段名[一般是id],显示的字段名
        List<String> results = jdbcTemplate.queryForList("SELECT " + strArr[2] + " FROM " + strArr[0] + " WHERE " + strArr[1] + "=" + planet, String.class);


        /*
         * Create the DOM structure that will be substituting our custom tag.
         * The headline will be shown inside a '<div>' tag, and so this must
         * be created first and then a Text node must be added to it.
         */
        final IModelFactory modelFactory = context.getModelFactory();

        final IModel model = modelFactory.createModel();

        IProcessableElementTag openElementTag = modelFactory.createOpenElementTag("td", "class", "authorities");
        model.add(openElementTag);
        model.add(modelFactory.createText(HtmlEscape.escapeHtml5(String.join(",", results))));
        model.add(modelFactory.createCloseElementTag("td"));

        /*
         * Instruct the engine to replace this entire element with the specified model.
         */
        structureHandler.replaceWith(model, false);

    }

}