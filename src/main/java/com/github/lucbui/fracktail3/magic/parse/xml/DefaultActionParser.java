package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.action.RandomAction;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultActionParser implements ActionParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultActionParser.class);

    @Override
    public Action fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior, DTDAction action) {
        if(action.getRespond() != null) {
            I18NString respond = action.getRespond();
            LOGGER.debug(getDebugString("Response Action", respond));
            return new RespondAction(fromI18NString(respond));
        }
        if(action.getRandom() != null) {
            DTDAction.Random random = action.getRandom();
            LOGGER.debug("Random Actions:");
            RandomAction.Builder rab = new RandomAction.Builder();
            for(DTDWeightedAction a : random.getAction()) {
                rab.add(fromXml(xml, command, behavior, a), a.getWeight());
            }
            LOGGER.debug("Random Actions Complete");
            return rab.build();
        }
        return null;
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
}
