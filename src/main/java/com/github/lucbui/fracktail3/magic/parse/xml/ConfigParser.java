package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.xsd.DTDConfiguration;
import com.github.lucbui.fracktail3.xsd.DTDDiscordConfiguration;

public interface ConfigParser {
    GlobalConfiguration globalFromXml(BotSpec botSpec, DTDConfiguration xml);
    DiscordConfiguration discordFromXml(BotSpec botSpec, DTDDiscordConfiguration xml);
}
