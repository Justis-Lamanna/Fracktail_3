package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.action.RandomAction;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.handlers.action.SequenceAction;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultActionParser implements ActionParser {
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
            return getCustomAction(action.getCustom());
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

    protected Action getCustomAction(DTDCustomClass custom) {
        if(custom.getClazz() != null) {
            return getCustomActionByClassElement(custom.getClazz(), custom.getMethod());
        } else if(custom.getSpring() != null) {
            return getCustomActionBySpringBean(custom.getSpring());
        }
        throw new BotConfigurationException("Custom actions must be specified by <class> element");
    }

    protected Action getCustomActionBySpringBean(String spring) {
        throw new BotConfigurationException("Spring bean actions are not permitted");
    }

    protected Action getCustomActionByClassElement(String className, String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            if(StringUtils.isEmpty(methodName)) {
                return getCustomActionByClass(clazz);
            } else {
                return getCustomActionByStaticMethod(clazz, methodName);
            }
        } catch (ClassNotFoundException e) {
            throw new BotConfigurationException("Unknown class " + className, e);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new BotConfigurationException("Unable to instantiate class type " + className, e);
        } catch (NoSuchMethodException e) {
            throw new BotConfigurationException("Unknown zero-param method " + className + "." + methodName, e);
        } catch (InvocationTargetException e) {
            throw new BotConfigurationException("Unable to invoke method " + className + "." + methodName, e);
        }
    }

    protected Action getCustomActionByClass(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if(!Action.class.isAssignableFrom(clazz)) {
            throw new BotConfigurationException("Class " + clazz + " is not a subtype of Action");
        }
        LOGGER.debug("Instantiating Action by creating a new instance of class {}", clazz.getCanonicalName());
        return (Action) clazz.newInstance();
    }

    protected Action getCustomActionByStaticMethod(Class<?> clazz, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = clazz.getMethod(methodName);
        if(!Modifier.isStatic(method.getModifiers())) {
            throw new BotConfigurationException("Method " + clazz.getCanonicalName() + "." + methodName + " is not static");
        }
        if(!Action.class.isAssignableFrom(method.getReturnType())) {
            throw new BotConfigurationException("Return type " + method.getReturnType() + " is not a subtype of Action");
        }
        LOGGER.debug("Instantiating Action by invoking {}", method.getName());
        return (Action) method.invoke(null);
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
