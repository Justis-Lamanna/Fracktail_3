package com.github.milomarten.fracktail3.modules.meta;

import com.github.milomarten.fracktail3.magic.command.Command;
import com.github.milomarten.fracktail3.magic.command.action.CommandAction;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class CommandsCommand {
    @Bean
    public Command cmdCmd() {
        return new Command.Builder("_cmd_cmd")
                .withNames("cmds", "commands")
                .withAction(new CommandsCommand.Action())
                .build();
    }

    private static class Action implements CommandAction {
        @Override
        public Mono<Void> doAction(CommandUseContext context) {
            return Flux.fromIterable(context.getBot().getSpec().getCommandList())
                    .filterWhen(c -> new CommandSearchContext(context, c).canDoAction())
                    .flatMap(c -> Flux.fromIterable(c.getNames()))
                    .collectSortedList()
                    .map(list -> String.join(", ", list))
                    .flatMap(cmdStr -> context.respond("The following commands are usable:\n" + cmdStr));
        }
    }
}
