package com.github.jeesun.thymeleaf.extras.processor;

import com.github.jeesun.thymeleaf.extras.util.TypeUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author simon
 * @date 2018-09-15
 **/

public class SayToPlanetAttributeTagProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "saytoplanet";
    private static final int PRECEDENCE = 10000;

    private static final String SAYTO_PLANET_MESSAGE = "msg.helloplanet";

    private JdbcTemplate jdbcTemplate;

    public SayToPlanetAttributeTagProcessor(final String dialectPrefix, JdbcTemplate jdbcTemplate){
        super(
                TemplateMode.HTML, // This processor will apply only to HTML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                null,              // No tag name: match any tag name
                false,             // No prefix to be applied to tag name
                ATTR_NAME,         // Name of the attribute that will be matched
                true,              // Apply dialect prefix to attribute name
                PRECEDENCE,        // Precedence (inside dialect's precedence)
                true);             // Remove the matched attribute afterwards
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final AttributeName attributeName, final String attributeValue,
            final IElementTagStructureHandler structureHandler) {

        /*
         * In order to evaluate the attribute value as a Thymeleaf Standard Expression,
         * we first obtain the parser, then use it for parsing the attribute value into
         * an expression object, and finally execute this expression object.
         */
        final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);

        final IStandardExpression expression = parser.parseExpression(context, attributeValue);

        final String planet = (String) expression.execute(context);

        /*
         * Set the salutation as the body of the tag, HTML-escaped and
         * non-processable (hence the 'false' argument)
         */
        //structureHandler.setBody("Hello, planet " + planet, false);
        structureHandler.setBody(TypeUtil.getInstance().setJdbcTemplate(jdbcTemplate).getEmail(planet), false);
    }
}
