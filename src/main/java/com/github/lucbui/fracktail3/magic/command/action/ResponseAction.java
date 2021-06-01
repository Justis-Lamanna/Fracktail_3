package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.BasicEditable;
import com.github.lucbui.fracktail3.magic.GenericSpec;
import com.github.lucbui.fracktail3.magic.params.EntryField;
import com.github.lucbui.fracktail3.magic.params.StringLengthLimit;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ResponseAction implements CommandAction, BasicEditable<ResponseAction> {
    private final String respondString;

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        return context.respond(respondString);
    }

    @Override
    public ResponseAction edit(GenericSpec spec) {
        return new ResponseAction(spec.getRequired("response", String.class));
    }

    @Override
    public List<EntryField> getEditFields() {
        return Collections.singletonList(
                EntryField.builder().id("response").description("Response").description("Text to respond with").typeLimit(StringLengthLimit.atMost(1024)).build()
        );
    }
}
