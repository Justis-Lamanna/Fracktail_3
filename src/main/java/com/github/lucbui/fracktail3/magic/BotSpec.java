package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.role.Roleset;
import com.github.lucbui.fracktail3.magic.role.Rolesets;

import javax.annotation.Nullable;
import java.util.Optional;

public class BotSpec {
    private GlobalConfiguration globalConfig;
    private DiscordConfiguration discordConfig;
    private Rolesets rolesets;
    private CommandList commandList;

    public Optional<GlobalConfiguration> getGlobalConfiguration() {
        return Optional.ofNullable(globalConfig);
    }

    public void setGlobalConfig(@Nullable GlobalConfiguration globalConfig) {
        this.globalConfig = globalConfig;
    }

    public Optional<DiscordConfiguration> getDiscordConfiguration() {
        return Optional.ofNullable(discordConfig);
    }

    public void setDiscordConfig(@Nullable DiscordConfiguration discordConfig) {
        this.discordConfig = discordConfig;
    }

    public Optional<Rolesets> getRolesets() {
        return Optional.ofNullable(rolesets);
    }

    public Optional<Roleset> getRoleset(String name) {
        return getRolesets().flatMap(r -> r.getRoleset(name));
    }

    public void setRolesets(@Nullable Rolesets rolesets) {
        this.rolesets = rolesets;
    }

    public CommandList getCommandList() {
        return commandList;
    }

    public void setCommandList(CommandList commandList) {
        this.commandList = commandList;
    }

    public void validate() throws BotConfigurationException {
        rolesets.validate();
        commandList.validate(this);
    }
}
