package anotherdnd.view.util;

import java.awt.*;

import static java.awt.GridBagConstraints.*;

public interface EZGridBag {
    int MARGIN = 4;
    Insets MARGIN_INSETS = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
    Insets NO_INSETS = new Insets(0, 0, 0, 0);
    GridBagConstraint _NO_INSETS = new InsetsConstraint(NO_INSETS);

    static GridBagConstraints gbc(
        GridBagConstraint... constraints
    ) {
        GridBagConstraints ret = new GridBagConstraints(
            0, 0,                       // Grid X/Y
            1, 1,                       // Grid span width/height
            0.0, 0.0,                   // Grid weight x/y
            GridBagConstraints.CENTER,  // Align
            GridBagConstraints.NONE,    // Fill
            MARGIN_INSETS,              // Insets
            0, 0                        // "ipad" = interior padding. Kinda redundant with Insets
        );
        for (GridBagConstraint constraint : constraints) {
            if (constraint instanceof GridXConstraint)      ret.gridx      = ((GridXConstraint)      constraint).gridx;      else
            if (constraint instanceof GridYConstraint)      ret.gridy      = ((GridYConstraint)      constraint).gridy;      else
            if (constraint instanceof GridWidthConstraint)  ret.gridwidth  = ((GridWidthConstraint)  constraint).gridwidth;  else
            if (constraint instanceof GridHeightConstraint) ret.gridheight = ((GridHeightConstraint) constraint).gridheight; else
            if (constraint instanceof WeightXConstraint)    ret.weightx    = ((WeightXConstraint)    constraint).weightx;    else
            if (constraint instanceof WeightYConstraint)    ret.weighty    = ((WeightYConstraint)    constraint).weighty;    else
            if (constraint instanceof AnchorConstraint)     ret.anchor     = ((AnchorConstraint)     constraint).anchor;     else
            if (constraint instanceof FillConstraint)       ret.fill       = ((FillConstraint)       constraint).fill;       else
            if (constraint instanceof InsetsConstraint)     ret.insets     = ((InsetsConstraint)     constraint).insets;
        }
        return ret;
    }

    interface GridBagConstraint {}

    class GridXConstraint      implements GridBagConstraint { public final int gridx;      public GridXConstraint      (int gridx)      { this.gridx      = gridx;      } }
    class GridYConstraint      implements GridBagConstraint { public final int gridy;      public GridYConstraint      (int gridy)      { this.gridy      = gridy;      } }
    class GridWidthConstraint  implements GridBagConstraint { public final int gridwidth;  public GridWidthConstraint  (int gridwidth)  { this.gridwidth  = gridwidth;  } }
    class GridHeightConstraint implements GridBagConstraint { public final int gridheight; public GridHeightConstraint (int gridheight) { this.gridheight = gridheight; } }
    class WeightXConstraint    implements GridBagConstraint { public final double weightx; public WeightXConstraint    (double weightx) { this.weightx    = weightx;    } }
    class WeightYConstraint    implements GridBagConstraint { public final double weighty; public WeightYConstraint    (double weighty) { this.weighty    = weighty;    } }
    class AnchorConstraint     implements GridBagConstraint { public final int anchor;     public AnchorConstraint     (int anchor)     { this.anchor     = anchor;     } }
    class FillConstraint       implements GridBagConstraint { public final int fill;       public FillConstraint       (int fill)       { this.fill       = fill;       } }
    class InsetsConstraint     implements GridBagConstraint { public final Insets insets;  public InsetsConstraint     (Insets insets)  { this.insets     = insets;     } }

    static GridBagConstraint gridx      (int value)    { return new GridXConstraint      (value); }
    static GridBagConstraint gridy      (int value)    { return new GridYConstraint      (value); }
    static GridBagConstraint gridwidth  (int value)    { return new GridWidthConstraint  (value); }
    static GridBagConstraint gridheight (int value)    { return new GridHeightConstraint (value); }
    static GridBagConstraint weightx    (double value) { return new WeightXConstraint    (value); }
    static GridBagConstraint weighty    (double value) { return new WeightYConstraint    (value); }
    static GridBagConstraint anchor     (int value)    { return new AnchorConstraint     (value); }
    static GridBagConstraint fill       (int value)    { return new FillConstraint       (value); }
    static GridBagConstraint insets     (Insets value) { return new InsetsConstraint     (value); }

    // Aliases
    static GridBagConstraint gx (int value)    { return gridx      (value); }
    static GridBagConstraint gy (int value)    { return gridy      (value); }
    static GridBagConstraint gw (int value)    { return gridwidth  (value); }
    static GridBagConstraint gh (int value)    { return gridheight (value); }
    static GridBagConstraint wx (double value) { return weightx    (value); }
    static GridBagConstraint wy (double value) { return weighty    (value); }

    static GridBagConstraint fill() { return fill(BOTH); }

    static GridBagConstraint insets (int top, int left, int bottom, int right) { return insets(new Insets(top, left, bottom, right)); }
    static GridBagConstraint insets (int all) { return insets(new Insets(all, all, all, all)); }
    static GridBagConstraint noInsets () { return _NO_INSETS; }

    // Re-name of insets(int) so as not to conflict with JPanel::insets(int)
    static GridBagConstraint padding (Insets insets) { return insets(insets); }
    static GridBagConstraint padding (int top, int left, int bottom, int right) { return insets(top, left, bottom, right); }
    static GridBagConstraint padding (int all) { return insets(all); }

    static GridBagConstraint align(int halign, int valign) {
        //@formatter:off
        return (valign < 0) ? /* Top    */ (halign < 0) ? /* Left   */ anchor(FIRST_LINE_START)  :
                                           (halign > 0) ? /* Right  */ anchor(FIRST_LINE_END)    :
                                                          /* Center */ anchor(PAGE_START)        :
               (valign > 0) ? /* Bottom */ (halign < 0) ? /* Left   */ anchor(LAST_LINE_START)   :
                                           (halign > 0) ? /* Right  */ anchor(LAST_LINE_END)     :
                                                          /* Center */ anchor(PAGE_END)          :
                              /* Middle */ (halign < 0) ? /* Left   */ anchor(BASELINE_LEADING)  :
                                           (halign > 0) ? /* Right  */ anchor(BASELINE_TRAILING) :
                                                          /* Center */ anchor(BASELINE);
        //@formatter:on
    }
}
