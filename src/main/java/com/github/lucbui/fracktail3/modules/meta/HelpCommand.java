package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Configuration
public class HelpCommand implements CommandAction {
    @Bean
    public Command helpCmd() {
        return new Command.Builder("_help_cmd")
                .withName("help")
                .withHelp("Get information on how to use a command.")
                .withAction(new HelpCommand())
                .withParameter(new Command.Parameter(0, "command", "The command to look up", String.class, true))
                .build();
    }

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        Optional<Object> obj = context.getParameters().getParameter(0);
        if(obj.isPresent()) {
            String searchCmd = (String)obj.get();
            return Flux.fromIterable(context.getBot().getSpec().getCommandList().getCommands())
                    .filter(c -> c.getNames().contains(searchCmd))
                    .filterWhen(c -> new HelpSearchContext(context, c).canDoAction())
                    .next()
                    .map(this::getFancyHelp)
                    .defaultIfEmpty("Sorry, I don't know that command.")
                    .flatMap(help -> context.respond(help));
        } else {
            return context.respond(getFancyHelp(context.getCommand()));
        }
    }

    private String getFancyHelp(Command command) {
        Tuple2<String, List<String>> names = getNames(command);
        StringJoiner parameterStr = new StringJoiner(" ");
        StringJoiner parameterHelpStr = new StringJoiner("\n\t");
        parameterStr.add("!" + names.getT1());
        for(Command.Parameter p : command.getParameters()) {
            parameterStr.add("<" + p.getName() + (p.isOptional() ? "?" : "") + ">");
            parameterHelpStr.add(p.getName() + ": " + p.getDescription() + (p.isOptional() ? "(Optional)" : ""));
        }

        String fancyHelp = StringUtils.appendIfMissing(command.getHelp(), ".") +
                "\n\tUsage: " + parameterStr.toString();

        if(!names.getT2().isEmpty()) {
            fancyHelp += "\n\tAliases: " + String.join(", ", names.getT2());
        }

        if(!command.getParameters().isEmpty()) {
            fancyHelp += "\nWhere:\n\t" + parameterHelpStr;
        }

        return fancyHelp;
    }

    private Tuple2<String, List<String>> getNames(Command command) {
        Iterator<String> names = command.getNames().iterator();
        String name = names.next();
        List<String> remaining = IteratorUtils.toList(names, command.getNames().size());
        return Tuples.of(name, remaining);
    }
}
