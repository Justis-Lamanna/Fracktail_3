package com.github.lucbui.fracktail3.spring.schedule.handler;

import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.service.Defaults;
import org.apache.commons.lang3.ClassUtils;

public class InjectPlatformHandler implements ParameterComponent.PCFunction {
    private final String id;
    private final Class<? extends Platform> platformClazz;

    public InjectPlatformHandler(String id, Class<? extends Platform> platformClazz) {
        this.id = id;
        this.platformClazz = platformClazz;
    }

    public InjectPlatformHandler(Class<? extends Platform> platformClazz) {
        this.id = null;
        this.platformClazz = platformClazz;
    }

    public String getId() {
        return id;
    }

    public Class<? extends Platform> getPlatformClazz() {
        return platformClazz;
    }

    @Override
    public Object apply(CommandUseContext context) {
        if(id == null) {
            return context.getBot()
                    .getPlatforms()
                    .stream()
                    .filter(p -> ClassUtils.isAssignable(p.getClass(), this.platformClazz))
                    .findFirst()
                    .orElseGet(() -> Defaults.getDefault(this.platformClazz));
        } else {
            return context.getBot()
                    .getPlatform(id)
                    .filter(p -> ClassUtils.isAssignable(p.getClass(), this.platformClazz))
                    .orElseGet(() -> Defaults.getDefault(this.platformClazz));
        }
    }
}
