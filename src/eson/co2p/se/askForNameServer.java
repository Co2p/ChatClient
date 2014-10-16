package eson.co2p.se;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tony on 16/10/2014.
 */
public class askForNameServer implements ActionListener {
    JFrame frame1 = new JFrame("NameServerAdress");
    JTextField Adress = new JTextField();
    JButton acceptadress = new JButton("Accept");
    String nameServerAdres = null;
    startGui Gui;


    public void askForNameServer(){
    }

    public void CreatNameserverQuestion(){
        acceptadress.addActionListener(this);
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(250, 100));
        frame1.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = createImageIcon("glorous28.png");
        frame1.setIconImage(icon.getImage());
        frame1.setResizable(false);
        frame1.setLayout(new GridLayout(2, 0));
        frame1.add(Adress,0);
        frame1.add(acceptadress,1);
        frame1.pack();
        frame1.setVisible(true);

    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == acceptadress){
            if(Adress.getText().trim() != ""){
                nameServerAdres = Adress.getText();
                Gui = new startGui();
                frame1.dispose();
            }
        }
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
