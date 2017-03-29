package anotherdnd.view.temp;

import anotherdnd.model.mechanics.Ability;
import anotherdnd.view.temp.Wizard.WizardScreen;

import javax.swing.*;
import java.awt.*;

public class CharacterBuilderScreen2 extends JPanel implements WizardScreen {
    @Override public JPanel       getPanel()    { return this; }
    @Override public String       getTitle()    { return "Abilities"; }
    @Override public boolean      hasPrevious() { return true; }
    @Override public WizardScreen getPrevious() { return previous; }
    @Override public boolean      hasNext()     { return false; }
    @Override public WizardScreen getNext()     { return null; }

    private final CharacterBuilderScreen1   previous;


    public CharacterBuilderScreen2(CharacterBuilderScreen1 previous) {
        super(new GridBagLayout());
        this.previous = previous;
        
        for (Ability ability : Ability.values()) {
            
        }
    }

}
