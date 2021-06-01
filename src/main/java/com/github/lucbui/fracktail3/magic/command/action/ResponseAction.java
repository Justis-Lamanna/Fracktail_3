package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.Editable;
import com.github.lucbui.fracktail3.magic.GenericSpec;
import com.github.lucbui.fracktail3.magic.params.EntryField;
import com.github.lucbui.fracktail3.magic.params.StringLengthLimit;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ResponseAction implements CommandAction, Editable<GenericSpec> {
    private String respondString;

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        return context.respond(respondString);
    }

    @Override
    public void edit(GenericSpec spec) {
        respondString = spec.getRequired("response", String.class);
    }

    @Override
    public List<EntryField> getFields() {
        return Collections.singletonList(
                new EntryField("response", "Response", "Text to respond with", StringLengthLimit.atMost(1024))
        );
    }
}
