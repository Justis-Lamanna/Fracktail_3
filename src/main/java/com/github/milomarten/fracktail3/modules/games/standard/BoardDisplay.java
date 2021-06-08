package com.github.milomarten.fracktail3.modules.games.standard;

import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Type;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardDisplay {
    private static final String[] EMOJI_NUMBERS = {
            "0️⃣",
            "1️⃣",
            "2️⃣",
            "3️⃣",
            "4️⃣",
            "5️⃣",
            "6️⃣",
            "7️⃣",
            "8️⃣",
            "9️⃣"};
    private static final String[] EMOJI_LETTERS = {
            ":regional_indicator_a:",
            ":regional_indicator_b:",
            ":regional_indicator_c:",
            ":regional_indicator_d:",
            ":regional_indicator_e:",
            ":regional_indicator_f:",
            ":regional_indicator_g:",
            ":regional_indicator_h:",
            ":regional_indicator_i:"};

    public static String display(Checkerboard board) {
        return IntStream.range(0, board.getHeight())
                .mapToObj(row -> IntStream.range(0, board.getWidth())
                        .mapToObj(col -> board.getPiece(new Position(row, col))
                                .map(piece -> {
                                    if(piece.getColor() == Color.RED) {
                                        return piece.getType() == Type.KING ? "\uD83D\uDFE5" : "\uD83D\uDD34";
                                    } else {
                                        return piece.getType() == Type.KING ? "\uD83D\uDFE6" : "\uD83D\uDD35";
                                    }
                                })
                                .orElseGet(() -> "⬜️"))
                        .collect(Collectors.joining("", getEmojiNumberFor(row), ""))
                )
                .collect(Collectors.joining("\n",
                        IntStream.range(0, board.getWidth()).mapToObj(BoardDisplay::getEmojiLetterFor).collect(Collectors.joining("", "❎", "\n")),
                        ""));
    }

    private static String getEmojiNumberFor(int number) {
        number++;
        if(number < 0 || number >= EMOJI_NUMBERS.length) {
            return "❎";
        } else {
            return EMOJI_NUMBERS[number];
        }
    }

    private static String getEmojiLetterFor(int number) {
        if(number < 0 || number >= EMOJI_LETTERS.length) {
            return "❎";
        } else {
            return EMOJI_LETTERS[number];
        }
    }
}
