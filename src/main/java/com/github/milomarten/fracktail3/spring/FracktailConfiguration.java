package com.github.milomarten.fracktail3.spring;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.BotSpec;
import com.github.milomarten.fracktail3.magic.command.CommandList;
import com.github.milomarten.fracktail3.magic.platform.Platform;
import com.github.milomarten.fracktail3.magic.platform.context.BasicParameterParser;
import com.github.milomarten.fracktail3.magic.platform.context.ParameterParser;
import com.github.milomarten.fracktail3.spring.service.BasicRoleService;
import com.github.milomarten.fracktail3.spring.service.CompositeRoleService;
import com.github.milomarten.fracktail3.spring.service.PlatformSpecificRoleService;
import com.github.milomarten.fracktail3.spring.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FracktailConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(FracktailConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public CommandList commandList() {
        return CommandList.empty();
    }

    @Bean
    @ConditionalOnMissingBean
    public BotSpec botSpec(List<Platform> platforms, CommandList commandList) {
        return new BotSpec(
                platforms,
                commandList
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public Bot bot(BotSpec botSpec) {
        return new Bot(botSpec);
    }

    @Bean
    @ConditionalOnMissingBean
    public ParameterParser parameterParser() {
        return new BasicParameterParser();
    }

    @Bean
    public RoleService roleService(List<PlatformSpecificRoleService<?>> services) {
        if(services.isEmpty()) {
            return new BasicRoleService();
        } else {
            LOGGER.info("Using Platform Specific Role Services: {}", services);
            return new CompositeRoleService(services);
        }
    }
}
