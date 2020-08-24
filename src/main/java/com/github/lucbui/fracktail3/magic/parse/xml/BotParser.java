package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.xsd.DTDBot;

public interface BotParser {
    Bot fromXml(DTDBot xml);
}
