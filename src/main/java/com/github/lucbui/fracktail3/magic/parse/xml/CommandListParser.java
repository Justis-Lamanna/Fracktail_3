package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.xsd.DTDBot;

public interface CommandListParser {
    CommandList fromXml(Bot bot, DTDBot xml);
}
