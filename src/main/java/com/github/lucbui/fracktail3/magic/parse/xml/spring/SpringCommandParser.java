package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.parse.xml.BehaviorParser;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultCommandParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringCommandParser extends DefaultCommandParser {
    @Autowired
    public SpringCommandParser(BehaviorParser behaviorParser) {
        super(behaviorParser);
    }
}
