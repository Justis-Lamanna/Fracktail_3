package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.TestPerson;
import com.github.lucbui.fracktail3.TestPlace;
import com.github.lucbui.fracktail3.magic.platform.NonePerson;
import com.github.lucbui.fracktail3.magic.platform.NonePlace;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.magic.platform.context.BasicCommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class FormattedStringsTest {
    private static final FormattedStrings handler = new FormattedStrings();

    @Test
    public void testBasicUsage() {
        String template = "Hello, #{sender.name}!";
        Person sender = new TestPerson("World");
        CommandUseContext ctx = Mockito.spy(new BasicCommandUseContext(null, null, null, sender,
                Mono.just(NonePlace.INSTANCE), null, null));

        handler.apply(ctx, template);

        verify(ctx).respond(eq("Hello, World!"));
    }

    @Test
    public void testMonoUsage() {
        String template = "Hello, #{triggerPlace.async.name}!";
        Place place = new TestPlace("World");

        CommandUseContext ctx = Mockito.spy(new BasicCommandUseContext(null, null, null, NonePerson.INSTANCE,
                Mono.just(place), null, null));

        handler.apply(ctx, template);

        verify(ctx).respond(eq("Hello, World!"));
    }

    @Test
    public void testAdvanceMonoUsage() {
        String template = "Hello, #{triggerPlace.block().name}!";
        Place place = new TestPlace("World");

        CommandUseContext ctx = Mockito.spy(new BasicCommandUseContext(null, null, null, NonePerson.INSTANCE,
                Mono.just(place), null, null));

        handler.apply(ctx, template);

        verify(ctx).respond(eq("Hello, World!"));
    }
}