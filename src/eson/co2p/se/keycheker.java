package eson.co2p.se;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens to specific key presses in the chatwindow and acts on them
 * @author Tony on 22/10/2014.
 */
public class keycheker {

    private static final String key = "ENTER";
    private KeyStroke keyStroke = KeyStroke.getKeyStroke(key);

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
        Object actionKey = area.getInputMap(JComponent.WHEN_FOCUSED).get(keyStroke);
        area.getActionMap().put(actionKey, wrapper);
    }
}
