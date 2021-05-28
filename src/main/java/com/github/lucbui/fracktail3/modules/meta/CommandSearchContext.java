package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class CommandSearchContext implements CommandUseContext {
    private final CommandUseContext baseContext;
    private final Command command;

    @Override
    public Bot getBot() {
        return baseContext.getBot();
    }

    @Override
    public Platform getPlatform() {
        return baseContext.getPlatform();
    }

    @Override
    public Parameters getParameters() {
        return null;
    }

    @Override
    public Object getTrigger() {
        return baseContext.getTrigger();
    }

    @Override
    public Person getSender() {
        return baseContext.getSender();
    }

    @Override
    public Mono<Place> getTriggerPlace() {
        return baseContext.getTriggerPlace();
    }
}
