package com.github.lucbui.fracktail3.modules.dnd.harrowbot;

import com.github.lucbui.fracktail3.modules.dnd.AbilityScore;
import com.github.lucbui.fracktail3.modules.dnd.Alignment;

/**
 * Describes one of 54 harrow cards, for fortune telling
 */
public enum HarrowCard {
    PALADIN(AbilityScore.STRENGTH, Alignment.LAWFUL_GOOD, "The Paladin", "Standing resolutely against trouble, without backing down"),
    KEEP(AbilityScore.STRENGTH, Alignment.NEUTRAL_GOOD, "The Keep", "Quiet, unshakeable strength that withstands any hardship"),
    BIG_SKY(AbilityScore.STRENGTH, Alignment.CHAOTIC_GOOD, "The Big Sky", "Momentous change, as when all the slaves of a nation are emancipated"),
    FORGE(AbilityScore.STRENGTH, Alignment.NEUTRAL_GOOD, "The Forge", "Strength through diversity, or strength from many sources united against a single trial"),
    BEAR(AbilityScore.STRENGTH, Alignment.TRUE_NEUTRAL, "The Bear", "Pure strength that cannot be truly tamed or trained"),
    UPRISING(AbilityScore.STRENGTH, Alignment.NEUTRAL_EVIL, "The Uprising", "Overwhelming strength that catches the subject up in something much more powerful"),
    FIEND(AbilityScore.STRENGTH, Alignment.CHAOTIC_GOOD, "The Fiend", "Deaths of many in a disaster"),
    BEATING(AbilityScore.STRENGTH, Alignment.CHAOTIC_NEUTRAL, "The Beating", "An assault from all quarters, or the mental dissolution of the self"),
    CYCLONE(AbilityScore.STRENGTH, Alignment.CHAOTIC_EVIL, "The Cyclone", "An unstoppable, destructive force unleashed through the plots of intelligent creatures"),

    DANCE(AbilityScore.DEXTERITY, Alignment.LAWFUL_GOOD, "The Dance", "A complicated framework that requires the cooperation of all to avoid collapse"),
    CRICKET(AbilityScore.DEXTERITY, Alignment.NEUTRAL_GOOD, "The Cricket", "The grig, quick travel, and the reward at the end of a journey"),
    JUGGLER(AbilityScore.DEXTERITY, Alignment.CHAOTIC_GOOD, "The Juggler", "Destiny, deities, and those who play with the fates of others"),
    LOCKSMITH(AbilityScore.DEXTERITY, Alignment.NEUTRAL_GOOD, "The Locksmith", "The keys the subject needs to unlock his fate"),
    PEACOCK(AbilityScore.DEXTERITY, Alignment.TRUE_NEUTRAL, "The Peacock", "A great beauty that can only be preserved if petrified and frozen in time for eternity"),
    RABBIT_PRINCE(AbilityScore.DEXTERITY, Alignment.NEUTRAL_EVIL, "The Rabbit Prince", "The quirky vicissitudes of melee combat"),
    AVALANCHE(AbilityScore.DEXTERITY, Alignment.CHAOTIC_GOOD, "The Avalanche", "Disaster: an unthinking panic and destruction that overruns all"),
    CROWS(AbilityScore.DEXTERITY, Alignment.CHAOTIC_NEUTRAL, "The Crows", "Murder, theft, and the violent loss of that which is loved"),
    DEMONS_LANTERN(AbilityScore.DEXTERITY, Alignment.CHAOTIC_EVIL, "The Demon's Lantern", "An impossible situation of traps, mind tricks, and sleight of hand"),

    TRUMPET(AbilityScore.CONSTITUTION, Alignment.LAWFUL_GOOD, "The Trumpet", "An archon who dives aggressively and righteously into danger"),
    SURVIVOR(AbilityScore.CONSTITUTION, Alignment.NEUTRAL_GOOD, "The Survivor", "A creature that has managed to survive a terrible ordeal, when everyone thought him lost"),
    DESERT(AbilityScore.CONSTITUTION, Alignment.CHAOTIC_GOOD, "The Desert", "An environment too difficult for anyone to survive without help"),
    BRASS_DWARF(AbilityScore.CONSTITUTION, Alignment.NEUTRAL_GOOD, "The Brass Dwarf", "A creature invulnerable to a current threat"),
    TEAMSTER(AbilityScore.CONSTITUTION, Alignment.TRUE_NEUTRAL, "The Teamster", "An external force that drives the subject on"),
    MOUNTAIN_MAN(AbilityScore.CONSTITUTION, Alignment.NEUTRAL_EVIL, "The Mountain Man", "An encounter with a physical power outside of the subject's control"),
    TANGLED_BRIAR(AbilityScore.CONSTITUTION, Alignment.CHAOTIC_GOOD, "The Tangled Briar", "A historical thing or creature that will have some influence on the question"),
    SICKNESS(AbilityScore.CONSTITUTION, Alignment.CHAOTIC_NEUTRAL, "The Sickness", "Corruption, famine, plague, pestilence, and disease"),
    WAXWORKS(AbilityScore.CONSTITUTION, Alignment.CHAOTIC_EVIL, "The Waxworks", "A place of torture, imprisonment, helplessness, and paralysis"),

    HIDDEN_TRUTH(AbilityScore.INTELLIGENCE, Alignment.LAWFUL_GOOD, "The Hidden Truth", "The discovery of the greater truth within"),
    WANDERER(AbilityScore.INTELLIGENCE, Alignment.NEUTRAL_GOOD, "The Wanderer", "A centaur collector who appreciates things others discard as junk"),
    JOKE(AbilityScore.INTELLIGENCE, Alignment.CHAOTIC_GOOD, "The Joke", "The value of humor in circumventing difficult people"),
    INQUISITOR(AbilityScore.INTELLIGENCE, Alignment.NEUTRAL_GOOD, "The Inquisitor", "An immutable object that cannot be deceived or influenced"),
    FOREIGN_TRADER(AbilityScore.INTELLIGENCE, Alignment.TRUE_NEUTRAL, "The Foreign Trader", "Spies, merchants, and those who trade in information"),
    VISION(AbilityScore.INTELLIGENCE, Alignment.NEUTRAL_EVIL, "The Vision", "Esoteric and arcane knowledge, or madness"),
    RAKSHASA(AbilityScore.INTELLIGENCE, Alignment.CHAOTIC_GOOD, "The Rakshasa", "Domination, mental control, and slavery"),
    IDIOT(AbilityScore.INTELLIGENCE, Alignment.CHAOTIC_NEUTRAL, "The Idiot", "Folly, greed, blackmail, bribery, or hubris"),
    SNAKEBITE(AbilityScore.INTELLIGENCE, Alignment.CHAOTIC_EVIL, "The Snakebite", "Poison, venom, assassination, and discord"),

    WINGED_SERPENT(AbilityScore.WISDOM, Alignment.LAWFUL_GOOD, "The Winged Serpent", "The bridge of understanding between the towers of knowledge and judgement"),
    MIDWIFE(AbilityScore.WISDOM, Alignment.NEUTRAL_GOOD, "The Midwife", "The enabler or conduit of new creation, information, or arrivals"),
    PUBLICAN(AbilityScore.WISDOM, Alignment.CHAOTIC_GOOD, "The Publican", "Fellowship and refuge"),
    QUEEN_MOTHER(AbilityScore.WISDOM, Alignment.NEUTRAL_GOOD, "The Queen Mother", "The personification of knowledge, who is fond of the powerless, the underclass, and those who will show her obeisance"),
    OWL(AbilityScore.WISDOM, Alignment.TRUE_NEUTRAL, "The Owl", "The harsh wisdom of the natural order"),
    CARNIVAL(AbilityScore.WISDOM, Alignment.NEUTRAL_EVIL, "The Carnival", "Illusions and false dreams"),
    ECLIPSE(AbilityScore.WISDOM, Alignment.CHAOTIC_GOOD, "The Eclipse", "Loss of faith and purpose, and the subject's doubt about his abilities or prospects"),
    MUTE_HAG(AbilityScore.WISDOM, Alignment.CHAOTIC_NEUTRAL, "The Mute Hag", "A purveyor of blood pacts, treacherous secrets, and discord"),
    LOST(AbilityScore.WISDOM, Alignment.CHAOTIC_EVIL, "The Lost", "The permanently insane, lost among lunatics and psychopaths in insane asylums"),

    EMPTY_THRONE(AbilityScore.CHARISMA, Alignment.LAWFUL_GOOD, "The Empty Throne", "Those who are gone, or a ghost of the past that has taught important lessons"),
    THEATER(AbilityScore.CHARISMA, Alignment.NEUTRAL_GOOD, "The Theater", "Prophecy, with a true prophet acting a part as the puppet of the gods"),
    UNICORN(AbilityScore.CHARISMA, Alignment.CHAOTIC_GOOD, "The Unicorn", "One who generously offers up that which is sought"),
    MARRIAGE(AbilityScore.CHARISMA, Alignment.NEUTRAL_GOOD, "The Marriage", "A union of peoples, ideas, or kingdoms that might be equally productive or ruinous"),
    TWIN(AbilityScore.CHARISMA, Alignment.TRUE_NEUTRAL, "The Twin", "The duality of purpose or identity, or indecision and fence-sitting for the subject"),
    COURTESAN(AbilityScore.CHARISMA, Alignment.NEUTRAL_EVIL, "The Courtesan", "Political intrigue and the superficiality of social niceties"),
    TYRANT(AbilityScore.CHARISMA, Alignment.CHAOTIC_GOOD, "The Tyrant", "A ruler who harms those he rules"),
    BETRAYAL(AbilityScore.CHARISMA, Alignment.CHAOTIC_NEUTRAL, "The Betrayal", "Selfishness and envy"),
    LIAR(AbilityScore.CHARISMA, Alignment.CHAOTIC_EVIL, "The Liar", "Destructive, treacherous love"),
    ;

    private final AbilityScore score;
    private final Alignment alignment;
    private final String name;
    private final String description;

    HarrowCard(AbilityScore score, Alignment alignment, String name, String description) {
        this.score = score;
        this.alignment = alignment;
        this.name = name;
        this.description = description;
    }

    public AbilityScore getScore() {
        return score;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
