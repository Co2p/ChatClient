package eson.co2p.se;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.Random;

/**
 * @author Gordon, Isidor, Tony 23 October 2014
 */
public class colors {

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

    public static void textColor(StyledDocument doc, SimpleAttributeSet keyWord, String message, String userName,
                                  int type) throws BadLocationException {

        switch(type) {
            case 0:
                //Easteregg
                if (userName != null && userName.contains("420")) {
                    color420(doc, keyWord, message);
                } else {
                    StyleConstants.setBold(keyWord, false);
                    StyleConstants.setForeground(keyWord, new Color(0, 0, 0));
                    doc.insertString(doc.getLength(), message, keyWord);
                }
                break;
            case 1:
                StyleConstants.setForeground(keyWord, new Color(197, 20, 22));
                doc.insertString(doc.getLength(), message, keyWord);
                break;
            case 2:
                StyleConstants.setForeground(keyWord, new Color(20, 197, 22));
                doc.insertString(doc.getLength(), message, keyWord);
                break;
            case 3:
                StyleConstants.setForeground(keyWord, new Color(20, 22, 197));
                doc.insertString(doc.getLength(), message, keyWord);
                break;
        }

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

    public static void ChangeColor(JPanel panelOne, int tabindex){
        //panelOne.setBackground(new Color(Loop254(tabindex, 0), Loop254(tabindex, 1), Loop254(tabindex, 2)));
        panelOne.updateUI();
        panelOne.validate();
    }

    /**
     * Generates a colour from the ip of the active tab
     * @param valu the active tab index
     * @param index offset in the colour from the last value
     * @return the amount of colour for RGB
     */
    /*public static int Loop254(int valu, int index){
        String ip;
        if(!catalogue.getManual_Server()) {
            ip = startGui.Server.getServer((String)startGui.serverlist.get(valu)).getPort() + startGui.Server
                    .getServer(
                    (String) startGui.serverlist.get(valu)).getIp().toString();
        }
        else{
            ip = "111.111.111.111";
        }
        ip = ip.replaceAll("[^0-9]", "");
        if (index>3 || ip.length()<9){
            return 254;
        }
        valu = Integer.parseInt(ip.substring(ip.length()-(index+3), ip.length()-(index)));

        while (valu>254){
            valu-=254;
            if (valu<80){
                valu+=80;
            }
        }
        return valu;
    }*/

}
