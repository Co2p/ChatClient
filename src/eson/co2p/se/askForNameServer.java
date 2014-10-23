package eson.co2p.se;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A GUI that asks for a name server.
 * @author  Gordon, Isidor, Tony 23 October 2014
 */
public class askForNameServer implements ActionListener {
    //  NickArray added for randomization of name
    JFrame frame1 = new JFrame("Connect to server");
    JPanel textFrame = new JPanel();
    JPanel nickPanel = new JPanel();
    JPanel checkPanel = new JPanel();
    //  Standard names for gui/input
    JLabel label = new JLabel("  Please provide a name server and nick, then Connect");
    JTextField Adress = new JTextField("itchy.cs.umu.se");
    JTextField port = new JTextField("1337");
    JTextField nick = new JTextField();
    JButton changeNick = new JButton("Change");
    JButton acceptadress = new JButton("Connect");
    JCheckBox Manualconnect = new JCheckBox("Connect directly to this server");
    startGui Gui;


    /**
     * Starts the gui
     */
    public void CreatNameserverQuestion(){
        acceptadress.addActionListener(this);
        changeNick.addActionListener(this);
        frame1.setLayout(new GridLayout(5, 0));
        textFrame.setLayout(new FlowLayout());
        nickPanel.setLayout(new FlowLayout());
        checkPanel.setLayout(new FlowLayout());
        frame1.setPreferredSize(new Dimension(360, 180));
        label.setPreferredSize(new Dimension(312, 20));
        Adress.setPreferredSize(new Dimension(250, 26));
        port.setPreferredSize(new Dimension(100, 26));
        nick.setPreferredSize(new Dimension(250, 26));
        changeNick.setPreferredSize(new Dimension(100, 26));
        nickGenerator.makeClean(true); //<-- Removes explicit names
        nick.setText(nickGenerator.getNew());

        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = createImageIcon("glorous28.png");

        frame1.setIconImage(icon.getImage());
        frame1.setResizable(false);
        frame1.add(label, 0);
        frame1.getContentPane().add(textFrame, 1);
        textFrame.add(Adress);
        textFrame.add(port);
        frame1.getContentPane().add(nickPanel, 2);
        nickPanel.add(nick);
        nickPanel.add(changeNick);
        frame1.add(acceptadress, 3);
        frame1.add(checkPanel, 4);
        checkPanel.add(Manualconnect);
        frame1.pack();
        frame1.setVisible(true);
        frame1.validate();

    }

    /**
     * Catches the button presses
     * @param e event id
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == acceptadress){
            accept();
        }

        if (e.getSource() == changeNick){
            nick.setText(nickGenerator.getNew());
        }
    }

    /**
     * Checks that the given address is valid before setting the server and starting the main gui
     */
    private void accept(){
        if (verifyAddress(Adress.getText())){
            if (port.getText().matches("[0-9]+") && port.getText().length() > 0){
                    if(!Adress.getText().trim().equals("")){
                        try {
                            catalogue.setNameServer(InetAddress.getByName(Adress.getText()), Integer.parseInt(port.getText()));
                            catalogue.setName(nick.getText());
                        } catch (UnknownHostException e1) {
                            e1.printStackTrace();
                        }
                        if(Manualconnect.isSelected()){
                            Gui = new startGui(true,Adress.getText(),Integer.parseInt(port.getText()));
                            System.out.println("adawdawdhere");
                        }
                        else{
                            Gui = new startGui(false,Adress.getText(),Integer.parseInt(port.getText()));
                        }
                        catalogue.setGui(Gui);
                        frame1.dispose();
                    }
                }
            }
            else {
                label.setText("  Invalid port number");
            }

    }

    /**
     * Looks for invalid characters
     * @param address the address that will be checked
     * @return true if the address is ok otherwise false
     */
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

    /**
     * Sets the icon
     * @param path path to the icon
     * @return the icon, null if it can't be found
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = startGui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
}
