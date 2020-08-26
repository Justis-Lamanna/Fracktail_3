package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Range;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBehaviorParser implements BehaviorParser, SupportsCustom<Behavior> {
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
        if(behavior.getCustom() != null) {
            return getFromCustom(behavior.getCustom());
        } else {
            return getBehaviorFromXml(xml, command, behavior);
        }
    }

    protected Behavior getBehaviorFromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior) {
        Range parameterRange = getParameterRange(behavior);

        String role = behavior.getRole() == null ? null : behavior.getRole().getValue();

        return new Behavior(parameterRange, actionParser.fromXml(xml, command, behavior, behavior.getAction()), role);
    }

    protected Range getParameterRange(DTDBehavior behavior) {
        DTDValueOrRange valueOrRange = behavior.getParameters();
        if(valueOrRange == null) {
            return getUnboundedRange();
        } else if(valueOrRange.getValue() != null) {
            return getSingleValueRange(valueOrRange);
        } else if(valueOrRange.getRange() != null) {
            return getBoundedRange(valueOrRange);
        } else {
            throw new BotConfigurationException("Unable to parse parameters: expected value or range");
        }
    }

    protected Range getUnboundedRange() {
        Range parameterRange;
        LOGGER.debug("Behavior accepts unlimited params");
        parameterRange = Range.unbounded();
        return parameterRange;
    }

    protected Range getSingleValueRange(DTDValueOrRange valueOrRange) {
        String value = valueOrRange.getValue();
        if(StringUtils.equalsIgnoreCase(value, "unbounded")) {
            LOGGER.debug("Behavior accepts unlimited params");
            return getUnboundedRange();
        } else {
            LOGGER.debug("Behavior accepts {} params", value);
            return Range.single(Integer.parseInt(value));
        }
    }

    protected Range getBoundedRange(DTDValueOrRange valueOrRange) {
        DTDRange range = valueOrRange.getRange();
        int start = range.getMin().intValue();
        if(StringUtils.equalsIgnoreCase(range.getMax(), "unbounded")) {
            LOGGER.debug("Behavior accepts {} and more params", start);
            return Range.fromOnward(start);
        } else {
            LOGGER.debug("Behavior accepts between {} and {} params", start, range.getMax());
            return Range.fromTo(start, Integer.parseInt(range.getMax()));
        }
    }

    @Override
    public Class<Behavior> getParsedClass() {
        return Behavior.class;
    }
}
