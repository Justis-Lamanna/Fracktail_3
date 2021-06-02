package com.github.lucbui.fracktail3.spring.command.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.magic.Editable;
import com.github.lucbui.fracktail3.magic.GenericSpec;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.params.EntryField;
import com.github.lucbui.fracktail3.magic.params.StringLengthLimit;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A converter function which allows for templating using the Spring engine
 * Arbitrary expressions can be computed by using #{template} syntax.
 * See https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html for information
 * on syntax and capabilities.
 *
 * The CommandUseContext is passed in to the templating context, so it can be referenced as such. For
 * example, #{sender.name} will be replaced with the name of the command's user. For Monos, normal methods
 * can be used, as well as the 'async' field, which functions the same as block().
 */
public class FormattedStrings extends StdReturnConverterFunctions.Strings {
    private static final ParserContext CONTEXT = new ParserContext() {
        @Override
        public boolean isTemplate() {
            return true;
        }

        @Override
        public String getExpressionPrefix() {
            return "#{";
        }

        @Override
        public String getExpressionSuffix() {
            return "}";
        }
    };

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private final Map<String, Expression> cache = new HashMap<>();

    @Override
    public Mono<Void> apply(CommandUseContext context, Object o) {
        String template = (String) o;
        StandardEvaluationContext evalContext = new StandardEvaluationContext(context);
        evalContext.addPropertyAccessor(MonoPropertyAccessor.INSTANCE);

        Expression expression = cache.computeIfAbsent(template, t -> PARSER.parseExpression(t, CONTEXT));

        String resolved = expression.getValue(evalContext, String.class);
        return super.apply(context, resolved);
    }

    @Data
    public static class Action implements CommandAction, Editable<Action> {
        @JsonIgnore
        private final Expression expression;

        public Action(String expString) {
            expression = PARSER.parseExpression(expString, CONTEXT);
        }

        public String getRawExpression() {
            return expression.getExpressionString();
        }

        @Override
        public Mono<Void> doAction(CommandUseContext context) {
            StandardEvaluationContext evalContext = new StandardEvaluationContext(context);
            evalContext.addPropertyAccessor(MonoPropertyAccessor.INSTANCE);

            String resolved = expression.getValue(evalContext, String.class);
            return context.respond(resolved);
        }

        @Override
        public Action edit(GenericSpec spec) {
            return new Action(spec.getRequired("response"));
        }

        @Override
        public List<EntryField> getEditFields() {
            return Collections.singletonList(
                    EntryField.builder()
                            .id("response")
                            .name("Response")
                            .description("Response object (supports SpEL)")
                            .typeLimit(StringLengthLimit.atMost(1024))
                            .build()
            );
        }
    }
}
