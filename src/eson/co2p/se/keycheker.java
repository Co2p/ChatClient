package eson.co2p.se;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Tony on 22/10/2014.
 */
public class keycheker {

    private static final String key = "ENTER";
    private KeyStroke keyStroke;

    private Action wrapper = new AbstractAction() {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent ae) {
            catalogue.getGui().GetButtons();
            JButton button= catalogue.getGui().GetButtons().get(catalogue.getGui().getSelectedServerTab());
            button.doClick();
        }
    };
    public void enterKeyAction(JTextArea area){
        keyStroke = KeyStroke.getKeyStroke(key);
        Object actionKey = area.getInputMap(JComponent.WHEN_FOCUSED).get(keyStroke);
        area.getActionMap().put(actionKey, wrapper);
    }
}
