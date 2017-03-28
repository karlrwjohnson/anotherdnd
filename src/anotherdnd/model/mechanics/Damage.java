package anotherdnd.model.mechanics;

public class Damage {

    private static Damage NO_DAMAGE = new Damage();

    public static Damage nothing() {
        return NO_DAMAGE;
    }




    /*
        Examples of damage:
        Long bow -- 1d8 (base weapon)

        Short sword -- 1d6 + Strength Piercing
        +1-enchanted Short sword -- (1d6 + Strength + 1 (enhancement bonus))  Piercing & Magic
        Flaming Short Sword -- everything above, plus 1d6 Fire & Magic

        Add to this nonlethal damage.

        Size modifiers.

        Channeled spells are doubled when a weapon crits, but not sneak attack dice.
        (they're "extra damage". But ability damage does get multiplied? Also, what is a sneak attack but a free critical hit? This game makes less and less sense the more I think about it.)
        (Also, shouldn't Knowledge Devotion be equivalent to a Ranger's Favored Enemy bonus? Like, you know where
        to hit to do extra damage? I guess that's why Pathfinder doesn't have it...)


     */

    // At the end of this, I'll have a GUI that can calculate all the damage modifiers in real-time, huh?
    // Like, you'll have a set of attacks to choose from, and you'll see exactly what each one does.

    public static enum DamageDescriptor {

        Slashing,
        Piercing,
        Bludgeoning,

        Nonlethal,

        Fire,
        Cold,
        Acid,
        Electrical,
        Sonic,

        Magic,

        PositiveEnergy,
        NegativeEnergy,

    //        static Stream<DamageDescriptor> valueStream() {
    //            return Arrays.stream(DamageDescriptor.class.getFields())
    //                .filter(field -> DamageDescriptor.class.isAssignableFrom(field.getType()))
    //                .map(carefully(field -> (DamageDescriptor) field.get(null)));
    //        }
    //
    //        static DamageDescriptor[] values() {
    //            return (DamageDescriptor[]) valueStream().toArray();
    //        }
    }
}
