package eson.co2p.se;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.Random;

/**
 * Handles some of the colour generating algorithms for startGui
 * @see eson.co2p.se.startGui
 * @author Gordon, Isidor, Tony 23 October 2014
 */
public class colors {

    /**
     * Returns the colour that corresponds with the different message types, as defined by MsgTypes
     * @see eson.co2p.se.MsgTypes
     * @param type Message type
     * @return A Color object
     */
    public static Color textHighlight(int type){

            //if the message is encrypted, compressed or both, add different backgrounds
            switch(type){
                case 1:
                    //compress
                    return new Color(206, 255, 185);
                case 2:
                    //crypt
                    return new Color(255, 252, 130);
                case 3:
                    //compress + crypt
                    return new Color(255, 169, 170);
            }
        return new Color(255, 255, 255);
    }

    /**
     * Easter Egg
     */
    public static void color420(StyledDocument doc, SimpleAttributeSet set, String message){
        Random random = new Random();
        for (int i = 0; i < message.length(); i++) {
            char character = message.charAt(i);
            //SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setForeground(set,
                    new Color(random.nextInt(256), random.nextInt(256),
                            random.nextInt(256)));
            StyleConstants.setFontSize(set, random.nextInt(12) + 12);
            StyleConstants.setBold(set, random.nextBoolean());
            try {
                doc.insertString(doc.getLength(), character + "", set);
            }catch(BadLocationException e){
                e.printStackTrace();
            }
        }
    }

    public static Color colorFromString(String nick){
        String color;
        if(nick.length() <4){
            color = String.format("#%X", (nick + "homo").hashCode());
        }else {
            color = String.format("#%X", nick.hashCode());
        }
        return new Color(
                Integer.valueOf( color.substring( 1, 3 ), 16 ),
                Integer.valueOf( color.substring( 3, 5 ), 16 ),
                Integer.valueOf( color.substring( 5, 7 ), 16 ) );
    }

}
