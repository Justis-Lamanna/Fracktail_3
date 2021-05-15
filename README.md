# Fracktail v3
Fracktail, v3, now with no XML at all.

The intention of this project is to make it easy to provide a customizable bot framework that can run on multiple
websites. This project currently consists of two layers, as outlined below:
* Base - This is contained in the `.magic` package, and contains a basic ability to create commands.
* Enhanced - This is contained in the `.spring` package, and leverages Spring to allow for more convenient command
creation, such as injecting and converting parameters automatically.

In addition, the `.discord` package contains relevant code to make the bot work on Discord. In the future, additional
packages will be provided for other platforms.

`.modules` contains some actual commands, which can be used as an example of your own commands.

## Quickstart
1. Add the following bean:
    ```java
    @Component
    public class Commands {
        @Command
        public String hello() {
            return "Hello, world!";
        }   
    }
    ```
3. Run program. The `discord.token` environment variable should contain your Discord bot's token.
The bot will log in to Discord using the provided token. One command, `!hello`, will be created.
When `!hello` is used, the bot will respond with `Hello, world!`

## Slowstart
A command can be defined in one of the following ways:
* Creating a `Command`-type bean directly, such as via `@Component` or `@Bean`.
* Creating a `CommandAction`-type bean directly, such as via `@Component` or `@Bean`.
* Annotating a method or field inside a bean with `@Command`.

### Specifying Command Names
A command can have one or more names, which activate the command, for example, `help`. A prefix may or may not
be necessary, depending on your platform. These can be specified with an `@Name` annotation.

When omitted, the name will be taken from the bean (for `CommandAction`-type commands), method, or field.

### Specifying Command Usage
A command may have usage data attached to it, which describes how the command can be used. This can be pulled by plugins,
for example, in response to a `!help` command, or after an incorrect command usage.

### Specifying Parameters
The annotations `@Parameter` can be used to retrieve parameters used, and inject them into the method
invocation. Conversion is automatic, depending on the method parameter type. If the specified parameter does not exist,
a suitable default will be used (eventually, suitable annotations will be used to provide custom defaults and enforce parameter values).

Additionally, the `@Parameters` annotation can be used on the method or field level, to specify any parameters
that do not map specifically to a method parameter. This is especially useful in simple field-based commands which otherwise
cannot have parameters.

### Returning a Value
By default, the following return types will be handled explicitly:
* `void` - Nothing visible happens.
* `Mono<?>\Flux<?>` - Permits an asynchronous action to occur before completing. Note that these asynchronously perform some
action, depending on the type. For instance, returning a `Mono<String>` will cause the bot to respond when the Mono completes with
some text.
* `String` - Respond with the String returned by the method. Note that, for a method-based command, the response is the raw
string, but for a field-based command, the response can be formatted. See below for more info. 
* `CommandAction` - Execute the provided CommandAction. Allows the most flexibility, since the Context is directly exposed.

### Throwing an Exception
Exception handlers will be specified by annotations, such as `@OnExceptionRespond`, which responds with a custom message
in the case of a thrown exception. Multiple handlers can be specified, but only one handler can be specified per specific 
exception class. If `Handler A` is associated with the exception's exact class, and `Handler B` is associated with a parent,
then the closest relative will be selected `Handler A`.

### Using a Field
The `@Command` annotation can support fields as well as methods. The field value returns upon method invocation (typically
as a simple response). This functions identically to a method annotated with `@Command` with no parameters. (See `@Parameters`
on how a Field-based command can have parameters injected).

### Using Formatting
For a field-based command that simply returns a String, formatting can be applied by using SpEL. By enclosing an expression
inside of `#{}`, any field in the Context can be retrieved. Methods can also be called. See 
[the documentation](https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html) for details.

## Spring Actuator
Basic Spring Actuator is supported, allowing admins to retrieve information on the bot via endpoints. The configured `/info` endpoint
returns information on the platforms, the commands, and scheduled actions. Additional info may be provided for each platform.

## Using Discord
Discord is, so far, the only platform I've created so far. Configuration uses a `DiscordConfiguration` object, which can be
injected directly as a bean, or assumed using the environment:
- The `discord.token` environment variable should have your bot's token, and is required.
- The `discord.prefix` environment variable should have your bot's prefix. If no prefix exists, `!` is used.
- Any ReactiveEventAdapter beans will be injected, using the bean name as their ID.
- The default presence is Online, with no status
- The default command type is `LEGACY`
- The default reply style is `REPLY`

### Reply Style
Supported reply styles are:
- `PLAIN` - A response will be generated in the same channel as the command, with no formatting.
- `REPLY` - A response will be generated in the same channel as the command, using the Discord reply feature.
- `DM` - A response will be generated in the command user's DMs.

### Command Type
Supported command types are:
- `LEGACY` - Commands are processed in the legacy way: scanning each message for commands, and responding accordingly.
- `SLASH` - Commands are processed using Discord's slash support. Note that this bot cannot register slash commands, and
expects it to be handled by you using the API directly. This only needs to be done once per command creation/update.
- `BOTH` - Commands are processed in both ways. The caveat to `SLASH` still applies.

### Annotations
Discord can be configured to use the following annotations:
- `@DiscordReply` - Allows the reply style to be changed from the default.

### Custom Return Objects
- `EmbedResponse` - Allows a response using a Discord embed object. For other platforms, nothing happens.

### Custom Hooks
All Discord events can be hooked into, either via the configuration or at runtime through the `DiscordPlatform` object.
The simplest way is to simply define a Bean which subclasses `ReactiveEventAdapter`. Hooks can be disabled via the
`DiscordPlatform` object, or by throwing a `CancelHookException` in the hook itself.

### Person Format
The following Person IDs are supported by `DiscordPlatform.getPerson`. Multiple people can be retrieved by separating
the IDs with a semicolon. When a number is supplied, it is treated as if it was a lookup by User ID. Any other formats
will return a `NonePerson`.
- `member:<guild snowflake>:<user snowflake>` - Retrieve a member of the guild via Guild and User IDs. `getName()` returns the user's display name (nickname, if applicable).
- `user:<user snowflake>` - Retrieves a user by their ID. `getName()` returns the user's official name.
- `role:<guild snowflake>:<role snowflake>` - Retrieves everyone with a specific role in the guild. `getName()` returns the role name.
- `role:<guild snowflake>:*` - Retrieves everyone in the guild. `getName()` returns `everyone`.
- `owner` - Retrieves the owner of the bot.
- `self` - Retrieves the bot as a user.

### Place Format
The following Place IDs are supported by `DiscordPlatform.getPlace`. Multiple places can be retrieved by separating
the IDs with a semicolon. When a number is supplied, it is treated as if it was a lookup by Channel ID. Any other formats
will return a `NonePlace`.

Note: To DM a person, or group of persons, use the `DiscordPlatform.getPerson` method to retrieve a person, and use that
to retrieve a place to send messages.
- `guild:<guild snowflake>` - Retrieve a guild by its ID. `getName()` returns the guild name. Messaging the guild places
the message in the guild's registered System Channel; However, the message feed shows all messages in all channels of the Guild.
- `guild:*` - Retrieve all guilds the bot is in. `getName()` returns `Everywhere`. Messaging the guild places the message in
all guild's registered System Channel; as such, exercise caution!
- `channel:<channel snowflake>` - Retrieves a channel by its ID. It is assumed to be a TextChannel, so referencing a Voice
Channel will fail.

### Spring Actuator
The Discord Platform supplies some additional info via Spring Actuator:
- `/health` includes these states for the Discord platform:
    - `OUT_OF_SERVICE` if the bot was not started for some reason.
    - `DOWN` if the bot could not connect to Gateway. `reason` contains the exception, and `message` contains the exception message.
    - `UP` if the bot is connected to Gateway, `id` contains the Bot's snowflake, `name` contains the bot's name, `discriminator`
    contains the bot's name, and `tag` combines the `name` and `discriminator` field.
- `/info` includes this information specifically for Discord:
    - Under `.discord`, the same information as the `/health` `UP` state.
    - The configuration used. The token is *not* present in this information.
- `/metrics` and `/prometheus` includes the following:
    - `discord.events` contains a number of counters, one for each type of event received. Note that a counter will only
    be created once the event is encountered.