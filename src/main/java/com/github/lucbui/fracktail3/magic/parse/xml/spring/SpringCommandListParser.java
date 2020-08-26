package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.parse.xml.CommandParser;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultCommandListParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringCommandListParser extends DefaultCommandListParser {
    @Autowired
    public SpringCommandListParser(CommandParser commandParser) {
        super(commandParser);
    }
}
