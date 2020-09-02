package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.xsd.DTDBot;

public interface BehaviorListParser {
    BehaviorList fromXml(BotSpec botSpec, DTDBot xml);
}
