package anotherdnd.view.temp;

import anotherdnd.model.util.Maybe;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.Events.onFocus;
import static anotherdnd.view.util.Events.onMouseOver;

public class InfoPanel extends JPanel {
    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static JsonNode node = Maybe.of(() -> mapper.readTree(new File("resources/strings.yaml"))).orElseThrow();

    private final JLabel infoTitle = new JLabel() {{
        setFont(getFont().deriveFont(18.0f));
        setBorder(new MatteBorder(0, 0, 1, 0, new Color(0x666666)));
        setVisible(false);
    }};

    private final JLabel infoContent = new JLabel();

    private JsonNode stringMap;

    private Component shownComponent;

    public InfoPanel (String namespace) {
        super(new GridBagLayout());

        stringMap = node.get(namespace);

        setBorder(new EmptyBorder(0, 2*MARGIN, 0, 0)); // padding

        add(infoTitle, gbc(wx(1), align(-1, 0), fill()));
        add(infoContent, gbc(gy(1), wx(1), wy(1), align(-1, -1), fillHorizontal()));
    }

    public void showForKey(String key) {
        String strings[];

//        System.out.println(stringMap); System.out.println(key);
        JsonNode entry = stringMap.get(key);
//        System.out.println(entry);

        infoTitle.setVisible(true);
        infoTitle.setText(entry.get("title").asText());
        infoContent.setText(entry.get("content").asText());
    }

    public void showForComponent(Component component, String name) {
        Runnable show = () -> showForKey(name);
        component.addFocusListener(onFocus(show));
        component.addMouseListener(onMouseOver(show));
    }

    public Component getShownComponent() {
        return shownComponent;
    }
}
