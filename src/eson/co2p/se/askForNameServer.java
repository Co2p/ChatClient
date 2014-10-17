package eson.co2p.se;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Tony on 16/10/2014.
 */
public class askForNameServer implements ActionListener {
    JFrame frame1 = new JFrame("Connect to server");
    JPanel textFrame = new JPanel();
    JLabel label = new JLabel("  Please provide a name server and click Connect");
    JTextField Adress = new JTextField("itchy.cs.umu.se");
    JTextField port = new JTextField("1337");
    JButton acceptadress = new JButton("Connect");
    startGui Gui;



    public void askForNameServer(){
    }

    public void CreatNameserverQuestion(){
        acceptadress.addActionListener(this);
        frame1.setLayout(new GridLayout(3, 0));
        textFrame.setLayout(new FlowLayout());
        frame1.setPreferredSize(new Dimension(340, 120));
        label.setPreferredSize(new Dimension(312, 20));
        Adress.setPreferredSize(new Dimension(250, 26));
        port.setPreferredSize(new Dimension(60, 26));

        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = createImageIcon("glorous28.png");

        frame1.setIconImage(icon.getImage());
        frame1.setResizable(false);
        frame1.add(label, 0);
        frame1.getContentPane().add(textFrame, 1);
        textFrame.add(Adress);
        textFrame.add(port);
        frame1.add(acceptadress, 2);
        frame1.pack();
        frame1.setVisible(true);
        frame1.validate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (verifyAddress(Adress.getText())){
            if (port.getText().matches("[0-9]+") && port.getText().length() > 0){
                if(e.getSource() == acceptadress){
                    if(Adress.getText().trim() != ""){
                        try {
                            catalogue.setNameServer(InetAddress.getByName(Adress.getText()), Integer.parseInt(port.getText()));
                        } catch (UnknownHostException e1) {
                            e1.printStackTrace();
                        }
                        Gui = new startGui();
                        frame1.dispose();
                    }
                }
            }
            else {
                label.setText("  Invalid port number");
            }
        }
    }

    private boolean verifyAddress(String address){
        char[] charAdd = address.toCharArray();
        char[] invalid = new char[]{'(', ')', '{', '}', '$', ';', '@', ':', '#', '%', '<', '>', '§'};

        for (int i=0; i<address.length(); i++) {
            for (char anInvalid : invalid) {
                if (charAdd[i] == anInvalid) {
                    label.setText("  Invalid character");
                    return false;
                }
            }
        }
        return true;
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = startGui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
}
