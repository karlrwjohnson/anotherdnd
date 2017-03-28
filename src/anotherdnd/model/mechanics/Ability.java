package anotherdnd.model.mechanics;

public enum Ability {
    STR ("Strength"),
    DEX ("Dexterity"),
    CON ("Constitution"),
    INT ("Intelligence"),
    WIS ("Wisdom"),
    CHA ("Charisma");

    public final String name;

    Ability(String name) {
        this.name = name;
    }
}
