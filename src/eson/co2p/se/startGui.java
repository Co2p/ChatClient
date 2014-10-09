package eson.co2p.se;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


/**
 * Created by Tony on 07/10/2014.
 * The
 */
public class startGui extends JFrame implements ActionListener {

    public String lastMessage = "";
    JPanel panelOne = new JPanel();
    JPanel panelTwo = new JPanel();
    JPanel panelThree = new JPanel();
    JTextArea userInput = new JTextArea(10, 20);
    JTextArea outputArea = new JTextArea(10, 20);
    JButton sendMessage;


    JPanel jPanel1 = new JPanel();
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea frame = new JTextArea ("Started chat client GUI");
    //JTextField userInput = new JTextField();

    private JLabel anLable;




    // Skapa konstruktor
    public startGui(){
        super();
        createFrame();
    };

    public void outputWindow(String userText) {
        if (userText!="" || userText!=null) {
            outputArea.append(userText);
        }
    }

    private void createFrame() {
        sendMessage = new JButton("Send");
        sendMessage.addActionListener(this);


        sendMessage.setPreferredSize(new Dimension(40, 50));
        //sendMessage.setSize(20,20);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //JComponent[] fields = new JComponent[2];
        //panelOne.setLayout(new GridLayout(3,0));
        panelThree.setLayout(new GridLayout(2, 0));
        userInput.setColumns(40);
        userInput.setText("enter message....");
        userInput.validate();
        outputArea.setEditable(false);
        //outputArea.setSize(500, 800);
        outputArea.setText("Chat logg goes here....");
        outputArea.validate();
        jScrollPane1.getViewport().add(outputArea);

       // getContentPane().setSize(500, 500);
        getContentPane().setVisible(true);
        //getContentPane().setLayout(new GridLayout(1, 0));
        getContentPane().setBackground(new Color(225, 29, 45));
        getContentPane().setPreferredSize(new Dimension(400, 500));
        getContentPane().validate();
        getContentPane().add(panelOne, "Center");
        getContentPane().add(panelTwo, "North");
        getContentPane().add(panelThree, "South");
        panelOne.setBackground(new Color(222, 225, 188));
        panelTwo.setBackground(new Color(58, 72, 107));
        panelThree.setBackground(new Color(72, 146, 75));
        anLable = new JLabel("Basic Client..(lolz)");
        panelOne.add(anLable);
        panelTwo.add(jScrollPane1);
        panelThree.add(userInput);
        panelThree.add(sendMessage);
        pack();
        setVisible(true);
    }

    // specific action listener
    private String buttonPresed(ActionEvent e){
        String Action = "";
        if(e.getSource() == sendMessage ){
            Action = userInput.getText();
            outputArea.setText(Action +"\n" + outputArea.getText());// need to be edited..
            userInput.setText("");
        }
        return Action;
    }
    // overnued action listener, need to be extended to be specifik (as done above)
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionRetValu = buttonPresed(e);
        if (actionRetValu != ""){
            lastMessage = actionRetValu;
            //ass this function cant have an return ( have to be void) an asignment is made
            //inplace of an return, getting the string actionRetValu vill acct as an return
        }
    }
        /*
        this.setLayout(null);
        jPanel1.setLayout(null);
        frame.setBackground(new Color(222, 225, 188));
        frame.setForeground(new Color(117, 9, 73));
        frame.setBorder(BorderFactory.createLoweredBevelBorder());
        frame.setToolTipText("lol2");
        frame.setEditable(false);
        frame.setColumns(42);
        frame.setRows(15);
        frame.setLocation(100,100);
        this.setTitle("An chat server");
        frame.setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                this_windowClosed(e);
            }
        });
        jScrollPane1.getViewport().add(frame);
        jPanel1.add(jScrollPane1);
        add(jScrollPane1);
        jScrollPane1.setLocation(50, 300);
        userInput.setVisible(true);
        userInput.setColumns(20);
        //userInput.setLocation(400,0);
        //userInput.setAlignmentY(50);
        jPanel1.add(userInput);
        add(userInput);
        userInput.setLocation(50,300);
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
    */
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