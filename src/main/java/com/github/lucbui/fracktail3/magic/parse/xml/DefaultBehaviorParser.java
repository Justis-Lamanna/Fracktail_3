package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Range;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBehaviorParser implements BehaviorParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBehaviorParser.class);

    private final ActionParser actionParser;

    public DefaultBehaviorParser(ActionParser actionParser) {
        this.actionParser = actionParser;
    }

    @Override
    public ActionParser getActionParser() {
        return actionParser;
    }

    @Override
    public Behavior fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior) {
        Range parameterRange;
        DTDValueOrRange valueOrRange = behavior.getParameters();
        if(valueOrRange == null) {
            LOGGER.debug("Behavior accepts unlimited params");
            parameterRange = Range.unbounded();
        } else if(valueOrRange.getValue() != null) {
            String value = valueOrRange.getValue();
            if(StringUtils.equalsIgnoreCase(value, "unbounded")) {
                LOGGER.debug("Behavior accepts unlimited params");
                parameterRange = Range.unbounded();
            } else {
                LOGGER.debug("Behavior accepts {} params", value);
                parameterRange = Range.single(Integer.parseInt(value));
            }
        } else if(valueOrRange.getRange() != null) {
            DTDRange range = valueOrRange.getRange();
            int start = range.getMin().intValue();
            if(StringUtils.equalsIgnoreCase(range.getMax(), "unbounded")) {
                LOGGER.debug("Behavior accepts {} and more params", start);
                parameterRange = Range.fromOnward(start);
            } else {
                LOGGER.debug("Behavior accepts between {} and {} params", start, range.getMax());
                parameterRange = Range.fromTo(start, Integer.parseInt(range.getMax()));
            }
        } else {
            throw new BotConfigurationException("Unable to parse parameters: expected value or range");
        }

        String role = behavior.getRole() == null ? null : behavior.getRole().getValue();

        return new Behavior(parameterRange, actionParser.fromXml(xml, command, behavior, behavior.getAction()), role);
    }
}
