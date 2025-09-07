package com.patria.apps.helper;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class ThymeleafHelperService {

    private final SpringTemplateEngine templateEngine;
    
    public String renderTemplateAsString(String templateName, Map<String, Object> dataModel) {
        Context context = new Context();

        context.setVariables(dataModel);

        return templateEngine.process(templateName, context);
    }
    
    public String renderTemplateAsString(String templateName, Object dataModel) {
        Context context = new Context();

        context.setVariable("data", dataModel);

        return templateEngine.process(templateName, context);
    }
}
