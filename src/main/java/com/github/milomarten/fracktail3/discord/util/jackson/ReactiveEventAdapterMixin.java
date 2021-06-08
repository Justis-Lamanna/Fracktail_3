package com.github.milomarten.fracktail3.discord.util.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public abstract class ReactiveEventAdapterMixin {
}
