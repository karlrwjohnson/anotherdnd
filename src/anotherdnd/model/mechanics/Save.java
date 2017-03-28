package anotherdnd.model.mechanics;

public enum Save {
    FORT ("Fortitude", Ability.CON),
    REFL ("Reflex",    Ability.DEX),
    WILL ("Will",      Ability.WIS);

    public final String name;
    public final Ability ability;

    Save(String name, Ability ability) {
        this.name = name;
        this.ability = ability;
    }

//    @Override
//    public String toString() { return name; }
}
