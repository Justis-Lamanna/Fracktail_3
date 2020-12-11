package com.github.lucbui.fracktail3.modules.dnd.moneybot;

public class MoneySplit {
    private final MoneyAmount forEach;
    private final MoneyAmount leftover;

    public MoneySplit(MoneyAmount forEach, MoneyAmount leftover) {
        this.forEach = forEach;
        this.leftover = leftover;
    }

    public MoneyAmount getForEach() {
        return forEach;
    }

    public MoneyAmount getLeftover() {
        return leftover;
    }
}
