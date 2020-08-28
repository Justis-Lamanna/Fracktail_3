package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.xsd.DTDBot;

public interface BotParser {
    BotSpec fromXml(DTDBot xml);
}
