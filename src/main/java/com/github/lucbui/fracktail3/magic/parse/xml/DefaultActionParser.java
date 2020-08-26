package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.action.RandomAction;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.handlers.action.SequenceAction;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultActionParser implements ActionParser, SupportsCustom<Action> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultActionParser.class);

    @Override
    public Action fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior, DTDAction action) {
        if(action.getRespond() != null) {
            return getRespondAction(action.getRespond());
        }
        if(action.getRandom() != null) {
            return getRandomAction(xml, command, behavior, action.getRandom());
        }
        if(action.getSequence() != null) {
            return getSequenceAction(xml, command, behavior, action.getSequence());
        }
        if(action.getCustom() != null) {
            return getFromCustom(action.getCustom());
        }
        return null;
    }

    protected Action getRespondAction(I18NString respond) {
        LOGGER.debug(getDebugString("Response Action", respond));
        return new RespondAction(fromI18NString(respond));
    }

    protected Action getRandomAction(DTDBot xml, DTDCommand command, DTDBehavior behavior, DTDAction.Random random) {
        LOGGER.debug("Random Actions:");
        RandomAction.Builder rab = new RandomAction.Builder();
        for(DTDWeightedAction a : random.getAction()) {
            rab.add(fromXml(xml, command, behavior, a), a.getWeight());
        }
        LOGGER.debug("Random Actions Complete");
        return rab.build();
    }

    private Action getSequenceAction(DTDBot xml, DTDCommand command, DTDBehavior behavior, DTDAction.Sequence sequence) {
        LOGGER.debug("Sequence Actions:");
        List<Action> actions = sequence.getAction().stream()
                .map(dtd -> fromXml(xml, command, behavior, dtd))
                .collect(Collectors.toList());
        LOGGER.debug("Sequence Actions Complete");
        return new SequenceAction(actions);
    }

    private static String getDebugString(String type, I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                type + " From Key: " + string.getValue() :
                type + ": " + string.getValue();
    }

    private static Resolver<String> fromI18NString(I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                new I18NResolver(string.getValue()) :
                Resolver.identity(string.getValue());
    }

    @Override
    public Class<Action> getParsedClass() {
        return Action.class;
    }
}
