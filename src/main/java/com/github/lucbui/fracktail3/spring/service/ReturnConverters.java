package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.handler.StdReturnConverterFunctions;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ReturnConverters {
    /**
     * Get a ReturnConverterFunction to handle a specific type
     * @return A ReturnConverterFunction to handle that class, if it exists
     */
    public Optional<ReturnComponent.RCFunction> getHandlerForType(TypeDescriptor descriptor) {
        Class<?> objectType = descriptor.getObjectType();
        if(objectType.equals(Void.class)) {
            return Optional.of(new StdReturnConverterFunctions.Voids());
        } else if(objectType.equals(String.class)) {
            return Optional.of(new StdReturnConverterFunctions.Strings());
        } else if(objectType.equals(Mono.class)) {
            ResolvableType innerType = descriptor.getResolvableType().getGeneric(0);
            ReturnComponent.RCFunction subCall = getHandlerForType(new TypeDescriptor(innerType, null, null))
                    .orElseThrow(() -> new BotConfigurationException("Unable to resolve generic " + innerType + " for type " + descriptor));
            return Optional.of(new StdReturnConverterFunctions.Monos(subCall));
        } else if(objectType.equals(Flux.class)) {
            ResolvableType innerType = descriptor.getResolvableType().getGeneric(0);
            ReturnComponent.RCFunction subCall = getHandlerForType(new TypeDescriptor(innerType, null, null))
                    .orElseThrow(() -> new BotConfigurationException("Unable to resolve generic " + innerType + " for type " + descriptor));
            return Optional.of(new StdReturnConverterFunctions.Fluxs());
        }
        return Optional.empty();
    }

    /**
     * Get a ReturnConverterFunction to handle a specific type
     * @return A ReturnConverterFunction to handle that class, if it exists
     */
    public Optional<ReturnScheduledComponent.RCSFunction> getScheduleHandlerForType(TypeDescriptor descriptor) {
        Class<?> objectType = descriptor.getObjectType();
        if(objectType.equals(Void.class)) {
            return Optional.of(new StdReturnConverterFunctions.Voids());
        } else if(objectType.equals(Mono.class)) {
            ResolvableType innerType = descriptor.getResolvableType().getGeneric(0);
            ReturnScheduledComponent.RCSFunction subCall = getScheduleHandlerForType(new TypeDescriptor(innerType, null, null))
                    .orElseThrow(() -> new BotConfigurationException("Unable to resolve generic " + innerType + " for type " + descriptor));
            return Optional.of(new StdReturnConverterFunctions.Monos(subCall));
        } else if(objectType.equals(Flux.class)) {
            ResolvableType innerType = descriptor.getResolvableType().getGeneric(0);
            ReturnScheduledComponent.RCSFunction subCall = getScheduleHandlerForType(new TypeDescriptor(innerType, null, null))
                    .orElseThrow(() -> new BotConfigurationException("Unable to resolve generic " + innerType + " for type " + descriptor));
            return Optional.of(new StdReturnConverterFunctions.Fluxs());
        }
        return Optional.empty();
    }
}
