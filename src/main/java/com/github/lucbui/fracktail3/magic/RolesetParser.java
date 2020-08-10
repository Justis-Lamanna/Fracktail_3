package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDRoleset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Component
public class RolesetParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(RolesetParser.class);
    private static final ExpressionParser parser = new SpelExpressionParser();

    public void fromXml(DTDBot xml) {
        if(xml.getRolesets() != null) {
            LOGGER.debug("Parsing Roleset List");
            for (DTDRoleset set : xml.getRolesets().getRoleset()) {
                LOGGER.debug("Creating Roleset {}", set.getName());
            }
        }
    }
}
