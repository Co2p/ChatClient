package eson.co2p.se;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


/**
 * Created by Tony on 07/10/2014.
 * no comments
 */
public class startGui extends JFrame{

    JPanel jPanel1 = new JPanel();
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea frame = new JTextArea ("Started chat client GUI");

    // Skapa konstruktor
    public startGui(){
        createFrame();
    };

    private void createFrame() {
        frame.setBackground(new Color(222, 225, 188));
        frame.setForeground(new Color(117, 9, 73));
        frame.setBorder(BorderFactory.createLoweredBevelBorder());
        frame.setToolTipText("lol2");
        frame.setEditable(false);
        frame.setColumns(42);
        frame.setRows(15);
        this.setTitle("An chat server");
        frame.setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                this_windowClosed(e);
            }
        });
        jScrollPane1.getViewport().add(frame);
        jPanel1.add(jScrollPane1);
        jPanel1.setBackground(new Color(209, 248, 250));
        this.getContentPane().add(jPanel1, BorderLayout.EAST);
        //start the server
        //startServer();
        this.setSize(485, 600);
        this.setResizable(false);
        this.setVisible(true);
        this.validate();
    }
    void this_windowClosed(WindowEvent e) {
        System.exit(1);
    }
    /*
    public void createFrame() {
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(224, 95, 225));
        frame.setForeground(new Color(42, 35, 255));
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        //start the server
        //startServer();
    }
    */

}