# Fracktail_3
Fracktail, v3, now with no XML at all.

## Quickstart
1. Add the following bean to your configuration file:
    ```java
    @Configuration
    @EnableScheduling
    public class Config {
        @Bean
        public Platform discord(@Value("${token}") String token) {
            return new DiscordPlatform.Builder()
                .withConfiguration(new DiscordConfigurationBuilder(token))
                .build();
        }
    
        @Command
        public String hello() {
            return "Hello, world";
        }   
    }
    ```
2. Create a CommandLineRunner:
    ```java
    @Component
    public class DiscordBotRunner implements CommandLineRunner {
        @Autowired
        private Bot bot;
    
        @Override
        public void run(String... args) throws Exception {
            bot.start().block();
        }
    
        @PreDestroy
        public void destroy() throws Exception {
            bot.stop().block();
        }
    }
   ```
3. Run program. The bot will log in to Discord using the provided token, 
