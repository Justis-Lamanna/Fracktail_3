package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import reactor.core.publisher.Mono;

public interface CommandUseContext {
    Bot getBot();
    Message getMessage();
    Platform getPlatform();
    Command getCommand();
    Parameters getParameters();

    default Mono<Boolean> matches() {
        return getCommand().matches(this);
    }

    default Mono<Void> doAction() {
        return getCommand().doAction(this);
    }
}
