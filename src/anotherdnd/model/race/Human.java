package anotherdnd.model.race;

import anotherdnd.model.race.FloatingPlusTwoRace.FloatingPlusTwoRaceImpl;

@Description("The most common race, humans are diverse and flexible.")
public class Human extends FloatingPlusTwoRaceImpl {
    static { Race.races.add(Human.class); }
}
