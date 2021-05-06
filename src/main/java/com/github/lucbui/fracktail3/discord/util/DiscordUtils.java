package com.github.lucbui.fracktail3.discord.util;

import discord4j.core.spec.MessageCreateSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DiscordUtils {
    public static void createSpec(MessageCreateSpec spec, String content, File... attachments) {
        spec.setContent(content);
        try {
            for (File file : attachments) {
                spec.addFile(file.getName(), new FileInputStream(file));
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("Unknown file", ex);
        }
    }
}
