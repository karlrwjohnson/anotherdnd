package anotherdnd.model.race;

@Description("The most common race, humans are diverse and flexible.")
public class Human extends AbstractRace {
    static { Race.races.add(Human.class); }
}
