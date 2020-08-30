package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.trigger.BehaviorTrigger;
import com.github.lucbui.fracktail3.magic.utils.Range;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBehaviorParser extends AbstractParser<Behavior> implements BehaviorParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBehaviorParser.class);

    private final ActionParser actionParser;

    public DefaultBehaviorParser(ActionParser actionParser) {
        super(Behavior.class);
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
        BehaviorTrigger trigger;
        if(behavior.getTrigger() == null) {
            trigger = BehaviorTrigger.DEFAULT;
        } else {
            DTDBehaviorTrigger xmlTrigger = behavior.getTrigger();

            boolean enabled = BooleanUtils.isNotFalse(xmlTrigger.isEnabled());
            Range parameterRange = getParameterRange(xmlTrigger);
            String role = xmlTrigger.getRole() == null ? null : xmlTrigger.getRole().getValue();

            trigger = new BehaviorTrigger(enabled, parameterRange, role);
        }

        LOGGER.debug("\t\tEnabled: {} | Range: {} | Role: {}",
                trigger.isEnabled(),
                trigger.getParameters(),
                StringUtils.defaultString(trigger.getRole(), "Any"));

        return new Behavior(trigger, actionParser.fromXml(xml, command, behavior, behavior.getAction()));
    }

    protected Range getParameterRange(DTDBehaviorTrigger behavior) {
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
        parameterRange = Range.unbounded();
        return parameterRange;
    }

    protected Range getSingleValueRange(DTDValueOrRange valueOrRange) {
        String value = valueOrRange.getValue();
        if(StringUtils.equalsIgnoreCase(value, "unbounded")) {
            return getUnboundedRange();
        } else {
            return Range.single(Integer.parseInt(value));
        }
    }

    protected Range getBoundedRange(DTDValueOrRange valueOrRange) {
        DTDRange range = valueOrRange.getRange();
        int start = range.getMin().intValue();
        if(StringUtils.equalsIgnoreCase(range.getMax(), "unbounded")) {
            return Range.fromOnward(start);
        } else {
            return Range.fromTo(start, Integer.parseInt(range.getMax()));
        }
    }
}
