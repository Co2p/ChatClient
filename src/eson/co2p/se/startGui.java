package eson.co2p.se;

/**
 * Created by Tony on 15/10/2014.
 *
 * The main gui for the chat, handles selection of servers, setting the cryptography of the messages etc
 */
import javax.swing.*;
import java.awt.Point;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class startGui extends JFrame implements ActionListener{

    JPanel panelOne;
    ServerList Server;
    ArrayList serverlist;
    final JFrame frame = new JFrame("Glorious Chat");


    /**
     * Initiates the servers and starts the gui
     */
    public startGui(){
        getServerNames();
        StartGui();
    }

    static Point mouseDownCompCoords;
    JTabbedPane tabbedPane;
    JComponent Servers;
    //a list of all the added server
    ArrayList<ArrayList> serverPlanes = new ArrayList<ArrayList>();
    ArrayList<JButton> Buttons = new ArrayList<JButton>();

    ArrayList<ArrayList> Client_Content = new ArrayList<ArrayList>();
    ArrayList<JTextArea> outputAreaList = new ArrayList<JTextArea>();

    int[] TABID = new int[256];
    int CurrentTab = 0;


    //Arraylist of all tabs
    ArrayList<JPanel> tabs = new ArrayList<JPanel>();

    /**
     * Retrives the active servers and adds them to serverlist
     */
    private void getServerNames(){
        try{
            Server = UDPServerConnection.getUdpServerlist();
            serverlist = Server.getServerList();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Excepted when trying to recive data: " + e);
        }
    }

    public void UpdateTabByID(int TabID,String TesxObject){
        JTextArea OutputArea = outputAreaList.get(TabID);
        OutputArea.setText(OutputArea.getText() +"\n"+ TesxObject);
    }

    public int getSelectedServerTab(){
        int Index = tabbedPane.getSelectedIndex();
        ChangeColor( panelOne, Index);
        return Index;
    }

    public InetAddress getSelectedServerAdress(){
        return Server.getServer((String)serverlist.get(getSelectedServerTab())).getIp();
    }

    public int getSelectedServerPort(){
        return Server.getServer((String)serverlist.get(getSelectedServerTab())).getPort();
    }

    public void ChangeColor(JPanel panelOne, int tabindex){
        panelOne.setBackground(new Color(Loop254(tabindex, 0), Loop254(tabindex, 1), Loop254(tabindex, 2)));
        panelOne.updateUI();
        panelOne.validate();
    }

    public JPanel CreatGui(){

        int NumberOfServer = serverlist.size();

        tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("glorous28.png");
        for(int i =0; i < NumberOfServer; i++){

            TABID[CurrentTab] = i;
            CurrentTab += 1;


            ArrayList<JTextArea> serverObjects = new ArrayList<JTextArea>();

            JPanel tempPanel2 = new JPanel();
            tempPanel2.setLayout(new GridLayout(2, 0));
            tempPanel2.setVisible(true);
            tempPanel2.setPreferredSize(new Dimension(480, 300));

            JComponent panel = makeTextPanel("Server: "+ Server.getServer((String)serverlist.get(i)).getName() + " Ip:" +Server.getServer((String)serverlist.get(i)).getIp() + " Port: " + Server.getServer((String)serverlist.get(i)).getPort() );
            panel.setPreferredSize(new Dimension(300, 50));
            panel.setBackground(new Color(Loop254(i, 0), Loop254(i, 1), Loop254(i, 2)));

            JPanel tempPanel3 = new JPanel();
            tempPanel3.setLayout(new GridLayout(3, 0));
            tempPanel3.add(panel);
            tempPanel2.add(tempPanel3);


            JTextArea inputArea = defineInputarea();
            JScrollPane jScrollPane2 = new JScrollPane();
            serverObjects.add(inputArea);
            jScrollPane2.getViewport().add(inputArea);


            JButton sendMessage = new JButton("Send");
            JButton connectButton = new JButton("Connect");
            JButton disConnectButton = new JButton("Disconnect");
            JCheckBox Compress = new JCheckBox("Compress");
            JCheckBox Encrypted = new JCheckBox("Encrypted");

            connectButton.setPreferredSize(new Dimension(150, 30));
            disConnectButton.setPreferredSize(new Dimension(150, 30));
            disConnectButton.setEnabled(false);

            JTextArea Key = new JTextArea("encryption key");
            Key.setPreferredSize(new Dimension(150, 20));

            JPanel tempPanel6 = new JPanel();
            JPanel tempBlankPanel = new JPanel();
            JPanel tempencryptplane = new JPanel();

            tempencryptplane.setLayout(new FlowLayout());
            tempPanel6.setLayout(new FlowLayout());

            tempPanel6.add(tempBlankPanel);
            tempPanel6.add(connectButton);
            tempPanel6.add(disConnectButton);

            tempPanel3.add(tempPanel6);
            tempencryptplane.add(Compress);
            tempencryptplane.add(Encrypted);
            tempencryptplane.add(Key);
            tempPanel3.add(tempencryptplane);

            sendMessage.addActionListener(this);
            connectButton.addActionListener(this);
            disConnectButton.addActionListener(this);

            Buttons.add(sendMessage);


            JPanel tempPanel5 = new JPanel();
            tempPanel5.setLayout(new FlowLayout());

            JPanel tempPanel4 = new JPanel();
            tempPanel4.setPreferredSize(new Dimension(480, 300));
            tempPanel5.add(jScrollPane2);
            tempPanel5.add(sendMessage);
            tempPanel5.add(tempPanel4);
            //tempPanel5.setBackground(new Color(Loop254(50 * i), Loop254(20 * i), Loop254(40 * i)));


            tempPanel2.add(tempPanel5);


            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new GridLayout(2, 0));
            JTextArea outputArea = defineOutputarea();
            serverObjects.add(outputArea);
            outputAreaList.add(outputArea);


            JScrollPane jScrollPane1 = new JScrollPane();
            jScrollPane1.getViewport().add(outputArea);

            //outputArea.setPreferredSize(new Dimension(400, 500));
            tempPanel.add(jScrollPane1);
            tempPanel.add(tempPanel2);

            //build layout
            String serverName = Server.getServer((String)serverlist.get(i)).getName();
            if(serverName.length()> 5){
                serverName = serverName.substring(0,5) + "..";
            }

            tabbedPane.addTab(serverName, icon, tempPanel, "Name: " + Server.getServer((String)serverlist.get(i)).getName());
            tabs.add(tempPanel);
            tabbedPane.setMnemonicAt(i,KeyEvent.VK_ADD);
            serverPlanes.add(serverObjects);

            //##############################################################################
            //Creat all arraylists needed (to be able to get the objects later on)
            // inputArea,disConnectButton,sendMessage,Compress,Encrypted,Key
            // connectButton
            //##############################################################################

            inputArea.setEnabled(false);
            disConnectButton.setEnabled(false);
            sendMessage.setEnabled(false);
            Compress.setEnabled(false);
            Encrypted.setEnabled(false);
            Key.setEnabled(false);

            JButton[] buttonList = new JButton[]{connectButton, disConnectButton, sendMessage};
            JCheckBox[] boxesList = new JCheckBox[]{Compress,Encrypted};
            JTextArea[] textAreas = new JTextArea[]{Key,inputArea};
            ArrayList<Object> ClientContent = new ArrayList<Object>();

            ClientContent.add(buttonList);
            ClientContent.add(boxesList);
            ClientContent.add(textAreas);


            Client_Content.add(ClientContent);


        }
        panelOne.add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        return panelOne;
    }

    /**
     * Generates a colour from the ip of the active tab
     * @param valu the active tab index
     * @param index the requested colour (or index) R=0, G=1, B=2
     * @return the amount of colour for RGB
     */
    private int Loop254(int valu, int index){
        if (index>3){
            return 254;
        }
        String ip = Server.getServer((String)serverlist.get(valu)).getIp().toString();
        ip = ip.replaceAll("[/.]+", "");
        valu = Integer.parseInt(ip.substring(ip.length()-(index+3), ip.length()-(index)));

        while (valu>254){
            valu-=254;
            if (valu<80){
                valu+=80;
            }
        }
        return valu;
    }

    public JTextArea defineOutputarea(){
        JTextArea outputArea = new JTextArea(10, 25);
        outputArea.setEditable(false);
        outputArea.setText("Chat logg goes here....");
        outputArea.validate();
        return outputArea;
    }

    public JTextArea defineInputarea(){
        JTextArea outputArea = new JTextArea(8, 25);
        outputArea.setEditable(true);
        outputArea.setText("EnterText");
        outputArea.validate();
        return outputArea;
    }

    public void StartGui(){

        panelOne = new JPanel();
        panelOne.setVisible(true);
        panelOne.setBackground(new Color(50, 20, 4));

        //frame.setUndecorated(true);
        frame.setBackground(new Color(24, 23, 24));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(CreatGui(),0);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(80, 80, 490, 666);
        //frame.setBackground(new Color(0,255,0,0));
        panelOne.addMouseListener(new MouseListener(){
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseClicked(MouseEvent e) {
            }
        });

        panelOne.addMouseMotionListener(new MouseMotionListener(){
            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });
    }


    public void UpdateWindows(JTextArea inputArea, JTextArea outputArea){
        outputArea.setText(outputArea.getText()+"\n"+ inputArea.getText());
        inputArea.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Object> activeClientObjects = Client_Content.get(getSelectedServerTab());
        JButton Connect = null;
        JButton Disconect = null;
        //get connect/disconnect buttons
        int IndexVal;
        for(Object Target : activeClientObjects) {
            if (Target.getClass() == JButton[].class) {
                JButton[] Buttons = (JButton[]) Target;
                Connect = Buttons[0];
                Disconect = Buttons[1];
                break;

            }
        }
        //chek the send button
        for (JButton s : Buttons) {
            if (s.equals(e.getSource())) {
                while (true){
                    if(catalogue.MessageInUse == false){
                        catalogue.SetClientMessage (getInputText(), getSelectedServerTab());
                        System.out.println("adding message");
                        break;
                    }
                }
                System.out.println("loop broken");
                addToOutputFromInput();
            }
        }
        //getting the needed objects
        if (Connect.equals(e.getSource()) || Disconect.equals(e.getSource())) {
            for(Object Target : activeClientObjects){
                if (Connect != null && Disconect != null) {
                    if (Target.getClass() == JButton[].class) {
                        JButton[] Buttons = (JButton[]) Target;
                        for (int i = 0; i < Buttons.length; i++) {
                            JButton target = Buttons[i];
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }

                    }
                    else if (Target.getClass() == JCheckBox[].class) {
                        JCheckBox[] checkBox = (JCheckBox[]) Target;
                        for (int i = 0; i < checkBox.length; i++) {
                            JCheckBox target = checkBox[i];
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }
                    }
                    else if (Target.getClass() == JTextArea[].class) {
                        JTextArea[] textArea = (JTextArea[]) Target;
                        for (int i = 0; i < textArea.length; i++) {
                            JTextArea target = textArea[i];
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }

                    }
                }
            }
        }

        //add an new server thread
        int ID = getSelectedServerTab();
        if (Connect.equals(e.getSource())) {
             ClientThread.startThread(ID,Server,serverlist);
        }
        if (Disconect.equals(e.getSource())){
            ClientThread.endThread(ID);
        }
    }

    public void clearOutputWindow(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverPlanes.get(Index);
        JTextArea Output = TempTarget.get(1);
        Output.setText("");
    }

    public void addOutputText(String Text){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverPlanes.get(Index);
        JTextArea Output = TempTarget.get(1);
        Output.setText(Output.getText() + "\n" + Text);
    }
    public String getInputText(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverPlanes.get(Index);
        JTextArea Input = TempTarget.get(0);
        String inp = Input.getText().trim();
        if (!inp.equals("")){
            return inp;
        }
        else{
            return null;
        }
    }
    public void addToOutputFromInput(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverPlanes.get(Index);
        JTextArea Input = TempTarget.get(0);
        String inp = Input.getText().trim();
        if (!inp.equals("")){
            JTextArea Output = TempTarget.get(1);
            Output.setText(Output.getText() + "\n" + Input.getText());
            removeInputText();
        }
    }

    public void removeInputText(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverPlanes.get(Index);
        JTextArea Input = TempTarget.get(0);
        Input.setText("");
    }

    public String FilterString(String text){
        String OriginalText = text;
        String Modified = "";
        int Lengt = text.getBytes().length;
        //TODO FilterString is suppose to filter the output string
        return Modified;
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
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