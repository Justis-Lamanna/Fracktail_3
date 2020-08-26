package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.parse.xml.ActionParser;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultBehaviorParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringBehaviorParser extends DefaultBehaviorParser {
    @Autowired
    public SpringBehaviorParser(ActionParser actionParser) {
        super(actionParser);
    }
}
