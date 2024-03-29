package com.github.milomarten.fracktail3.magic.platform.formatting;

import com.github.milomarten.fracktail3.magic.command.action.CommandAction;
import com.github.milomarten.fracktail3.magic.platform.Platform;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Data
public class SemanticMessage implements CommandAction {
    private List<Token> tokens;

    private SemanticMessage(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Create a plain message with no intent
     * @param message The message to convert
     * @return A SemanticMessage with no intent
     */
    public static SemanticMessage create(String message) {
        return create(StdIntent.NONE, message);
    }

    /**
     * Creator message for a simple message with one global intent
     * @param intent The global intent
     * @param message The text to be said
     * @return A SemanticMessage
     */
    public static SemanticMessage create(Intent intent, String message) {
        return create(new Token(intent, message));
    }

    /**
     * Create a Semantic message using a mixture of Intents and objects
     * This may perhaps be the most convenient way for users to create one of these. Usage is as follows:
     *
     * SemanticMessage.create("Hello, my name is ", Intent.CITE, "Fracktail", Intent.NONE, ". It's nice to meet you!");
     *
     * Any objects that are not Intents have their toString() method called, or print "null" if null.
     * @param message
     * @return
     */
    public static SemanticMessage create(Object... message) {
        Intent intent = null;
        StringBuilder sb = new StringBuilder();
        List<Token> tokens = new ArrayList<>();
        for(Object obj : message) {
            if(obj instanceof Intent) {
                if(sb.length() > 0) {
                    tokens.add(new Token(ObjectUtils.defaultIfNull(intent, StdIntent.NONE), sb.toString()));
                    sb.setLength(0);
                }
                intent = (Intent) obj;
            } else {
                sb.append(obj);
            }
        }
        if(sb.length() > 0) {
            tokens.add(new Token(ObjectUtils.defaultIfNull(intent, StdIntent.NONE), sb.toString()));
        }
        return new SemanticMessage(tokens);
    }

    /**
     * Create a Semantic message using token objects directly
     * @param tokens The tokens to use
     * @return A SemanticMessage
     */
    public static SemanticMessage create(Token... tokens) {
        return new SemanticMessage(List.of(tokens));
    }

    /**
     * Creator message for a more complex message
     * @param intent The starting intent
     * @param message The message to use
     * @return A builder, to continue adding text
     */
    public static Builder start(Intent intent, String message) {
        Builder builder = new Builder();
        builder.tokens.add(new Token(intent, message));
        return builder;
    }

    /**
     * Creator message for a more complex message
     * @param message The initial message
     * @return A builder, to continue adding text
     */
    public static Builder start(String message) {
        Builder builder = new Builder();
        builder.tokens.add(new Token(StdIntent.NONE, message));
        return builder;
    }

    /**
     * Concatenate two semantic messages together
     * @param message The message to concatenate with this one
     * @return A new SemanticMessage which is the sum of this one and the provided one
     */
    public SemanticMessage concat(SemanticMessage message) {
        List<Token> newTokens = new ArrayList<>(this.tokens);
        newTokens.addAll(message.tokens);
        return new SemanticMessage(newTokens);
    }

    /**
     * Concatenate a semantic message and a plain string
     * The input message is assumed to have no Intent.
     * @param message The message to concatenate with this one.
     * @return A new SemanticMessage which is the sum of this one and the provided message
     */
    public SemanticMessage concat(String message) {
        return concat(StdIntent.NONE, message);
    }

    /**
     * Concatenate a semantic message and a plain string with intent
     * @param intent The intent of the appended message
     * @param message The message to concatenate with this one.
     * @return A new SemanticMessage which is the sum of this one and the provided message
     */
    public SemanticMessage concat(Intent intent, String message) {
        List<Token> newTokens = new ArrayList<>(this.tokens);
        newTokens.add(new Token(intent, message));
        return new SemanticMessage(newTokens);
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        for(Token token : tokens) {
            message.append(token.message);
        }
        return message.toString();
    }

    /**
     * Converts to a string using a specific Semantic ruleset
     * @param semantic The semantics to use
     * @return The created String
     */
    public String toString(SemanticSupport semantic) {
        StringBuilder message = new StringBuilder();
        for(Token token : tokens) {
            Formatting formatting = semantic.forIntent(token.getIntent());
            message.append(formatting.format(token.getMessage()));
        }
        return message.toString();
    }

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        Platform platform = context.getPlatform();
        String message;
        if(platform instanceof SemanticSupport) {
            message = toString((SemanticSupport)platform);
        } else {
            message = toString();
        }
        return context.respond(message);
    }

    /**
     * Represents a token, which is a message coupled with its intent
     */
    @Data
    public static class Token {
        private final Intent intent;
        private final String message;
    }

    /**
     * Builder class, for even more complicated SemanticMessages
     */
    public static class Builder {
        private final List<Token> tokens;

        /**
         * Initialize the builder with no tokens
         */
        public Builder() {
            this.tokens = new ArrayList<>();
        }

        /**
         * Adds a semantic message
         * @param intent The intent of the message
         * @param message The message
         * @return This builder
         */
        public Builder then(Intent intent, String message) {
            tokens.add(new Token(intent, message));
            return this;
        }

        /**
         * Adds a plain message
         * @param message The intent of the message
         * @return This builder
         */
        public Builder then(String message) {
            tokens.add(new Token(StdIntent.NONE, message));
            return this;
        }

        /**
         * Create the SemanticMessage
         * @return The created message
         */
        public SemanticMessage build() {
            return new SemanticMessage(tokens);
        }
    }
}
