package com.github.milomarten.fracktail3.modules.cc;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CCRedemption {
    GIVE_100("give100"),
    GIVE_1000("give1000"),
    TAKE_100("take100"),
    TAKE_1000("take1000"),
    CLEAR_LOAN("zeroLoan"),
    GO_BACK_ONE_MONTH("minusOneMonth"),
    GO_BACK_TO_START("resetDate"),
    FORCE_WIN("forceWin"),
    MAKE_SUNNY("forceWeather0"),
    MAKE_PARTLY_CLOUDY("forceWeather1"),
    MAKE_CLOUDY("forceWeather2"),
    MAKE_RAIN("forceWeather3"),
    MAKE_STORM("forceWeather4"),
    MAKE_THUNDERSTORM("forceWeather5"),
    MAKE_SNOW("forceWeather6"),
    MAKE_SNOWSTORM("forceWeather7"),
    MAKE_BLIZZARD("forceWeather8"),
    MAKE_RANDOM("forceRandomWeather"),
    FREEZE_WEATHER("freezeWeather"),
    FIX_ALL_RIDES("fixAllRides"),
    FAST_CHAIN_LIFT("fastChainLift"),
    SLOW_CHAIN_LIFT("slowChainLift"),
    RECOLOR_PEEPS("peepRecolor"),
    FEED_PEEPS("peepFeed"),
    HUNGRY_PEEPS("peepUnfeed"),
    QUENCH_PEEPS("peepDrink"),
    THIRSTY_PEEPS("peepUndrink"),
    FILL_PEEP_BLADDERS("peepFillBladder"),
    EMPTY_PEEP_BLADDERS("peepEmptyBladder"),
    GIVE_PEEPS_MONEY("peepGiveMoney"),
    TAKE_PEEPS_MONEY("peepTakeMoney"),
    GIVE_PEEPS_BALLOONS("peepGiveBalloon"),
    CLEAN_PATH("cleanPaths"),
    MOW_GRASS("mowGrass"),
    UNMOW_GRASS("unmowGrass"),
    WATER_PLANTS("waterPlants"),
    KILL_PLANTS("burnPlants"),
    SMASH_SCENERY("smashScenery"),
    FIX_SCENERY("fixScenery"),
    MAKE_DUCKS("spawnDucks"),
    KILL_DUCKS("clearDucks"),
    POPUPS("openRandomWindows"),
    CLOSE_WINDOWS("closeAllWindows");

    private final String effect;

    CCRedemption(String effect) {
        this.effect = effect;
    }

    @JsonValue
    public String getEffect() {
        return effect;
    }
}
