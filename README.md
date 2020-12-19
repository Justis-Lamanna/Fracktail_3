# Fracktail_3
Fracktail, v3, now with no XML at all.

The intention of this project is to make it easy to provide a customizable bot framework that can run on multiple
websites. This project currently consists of two layers, as outlined below:
* Base - This is contained in the `.magic` package, and contains a basic ability to create commands.
* Enhanced - This is contained in the `.spring` package, and leverages Spring to allow for more convenient command
creation, such as injecting and converting parameters automatically.

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
3. Run program. The `token` environment variable should contain your Discord bot's token.
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
The annotations `@Parameter` and `@ParameterRange` can be used to retrieve parameters used, and inject them into the method
invocation. Conversion is automatic, depending on the method parameter type. If the specified parameter does not exist,
a suitable default will be used (eventually, suitable annotations will be used to provide custom defaults and enforce parameter values)

### Returning a Value
By default, the following return types will be handled explicitly:
* `void` - Nothing visible happens.
* `Mono<?>\Flux<?>` - Permits an asynchronous action to occur before completing.
* `String` - Respond with the String returned by the method.
* `FormattedString` - Respond with the evaluated FormattedString.
* `BotResponse` - Allows a custom type to specify how to respond.

### Throwing an Exception
Exception handlers will be specified by annotations, such as `@OnExceptionRespond`, which responds with a custom message
in the case of a thrown exception. Multiple handlers can be specified, but only one handler can be specified per specific 
exception class. If `Handler A` is associated with the exception's exact class, and `Handler B` is associated with a parent,
then the closest relative will be selected `Handler A`.

### Using a Field
The `@Command` annotation can support fields as well as methods. The field value returns upon method invocation (typically
as a simple response). This functions identically to a method annotated with `@Command` with no parameters.
