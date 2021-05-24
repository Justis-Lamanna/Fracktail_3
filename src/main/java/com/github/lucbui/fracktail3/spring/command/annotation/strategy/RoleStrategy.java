package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.Role;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.RoleService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
@Order(0)
public class RoleStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleStrategy.class);

    @Autowired
    private RoleService roleService;

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        Role role = method.getAnnotation(Role.class);
        base.addGuard(new RoleGuard(role.value()));
        LOGGER.info("+-With role restriction:" + role.value());
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        Role role = field.getAnnotation(Role.class);
        base.addGuard(new RoleGuard(role.value()));
        LOGGER.info("+-With role restriction:" + role.value());
        return base;
    }

    @Data
    private class RoleGuard implements Guard {
        private final String roleNeeded;

        @Override
        public Mono<Boolean> matches(CommandUseContext ctx) {
            return roleService.hasRole(ctx.getSender(), roleNeeded);
        }
    }
}
