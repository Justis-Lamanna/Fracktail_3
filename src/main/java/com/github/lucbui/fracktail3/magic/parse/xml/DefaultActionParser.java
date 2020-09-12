package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.action.*;
import com.github.lucbui.fracktail3.xsd.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultActionParser extends AbstractParser<Action> implements ActionParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultActionParser.class);

    public DefaultActionParser() {
        super(Action.class);
    }

    @Override
    public Action fromXml(DTDBot xml, DTDCommand command, DTDCommandAction action) {
        if(action.getMessage() != null) {
            return getRespondAction(action.getMessage());
        }
        if(action.getAlert() != null) {
            return getAlertAction(action.getAlert());
        }
        if(action.getRandom() != null) {
            return getRandomAction(xml, command, action.getRandom());
        }
        if(action.getSequence() != null) {
            return getSequenceAction(xml, command, action.getSequence());
        }
        if(action.getCustom() != null) {
            return getFromCustom(action.getCustom());
        }
        return null;
    }

    protected Action getRespondAction(I18NString respond) {
        LOGGER.debug("\t\t\t" + getDebugString("Response Action", respond));
        return new RespondAction(fromI18NString(respond));
    }

    protected Action getAlertAction(I18NString respond) {
        LOGGER.debug("\t\t\t" + getDebugString("Alert Action", respond));
        return new AlertAction(fromI18NString(respond));
    }

    protected Action getRandomAction(DTDBot xml, DTDCommand command, DTDWeightedCommandActions random) {
        LOGGER.debug("\t\t\tRandom Actions:");
        RandomAction.Builder rab = new RandomAction.Builder();
        for(DTDWeightedCommandAction a : random.getAction()) {
            rab.add(fromXml(xml, command, a), a.getWeight());
        }
        LOGGER.debug("\t\t\tRandom Actions Complete");
        return rab.build();
    }

    private Action getSequenceAction(DTDBot xml, DTDCommand command, DTDCommandActions sequence) {
        LOGGER.debug("\t\t\tSequence Actions:");
        List<Action> actions = sequence.getAction().stream()
                .map(dtd -> fromXml(xml, command, dtd))
                .collect(Collectors.toList());
        LOGGER.debug("\t\t\tSequence Actions Complete");
        return new SequenceAction(actions);
    }
}
