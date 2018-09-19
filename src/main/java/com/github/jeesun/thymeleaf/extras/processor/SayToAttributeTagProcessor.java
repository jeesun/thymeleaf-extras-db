package com.github.jeesun.thymeleaf.extras.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

/**
 * 测试
 *
 * @author simon
 * @create 2018-09-02 15:16
 **/

public class SayToAttributeTagProcessor extends AbstractAttributeTagProcessor {
    public SayToAttributeTagProcessor(String dialectPrefix){
        super(TemplateMode.HTML, dialectPrefix, null, false, "sayto", true, 10000, true);
    }

    @Override
    protected void doProcess(ITemplateContext iTemplateContext, IProcessableElementTag iProcessableElementTag, AttributeName attributeName, String s, IElementTagStructureHandler iElementTagStructureHandler) {
        iElementTagStructureHandler.setBody("Hello, " + HtmlEscape.escapeHtml5(s) + "!", false);
        //iElementTagStructureHandler.setBody("Hello, " + TypeUtil.getEmail(s) + "!", false);
        /*final IEngineConfiguration configuration = iTemplateContext.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(iTemplateContext, s);
        final Boolean position = (Boolean) expression.execute(iTemplateContext);
        if(position){
            iElementTagStructureHandler.setAttribute("style", "background:green");
        }else{
            iElementTagStructureHandler.setAttribute("style", "background:red");
        }*/

    }
}
