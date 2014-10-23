package eson.co2p.se;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * The main gui for the chat, handles selection of servers, setting the cryptography of the messages etc
 * @author Tony on 15/10/2014.
 */
public class startGui extends JFrame implements ActionListener {

    JPanel panelOne;
    //boolean Manual_Server = false;
    ServerList Server;
    ArrayList serverlist;
    final JFrame frame = new JFrame("Glorious Chat");

    static Point mouseDownCompCoords;
    JTabbedPane tabbedPane;
    JComponent Servers;
    //a list of all the added server
    ArrayList<ArrayList> serverPlanes = new ArrayList<ArrayList>();
    ArrayList<ArrayList> serverJTextField = new ArrayList<ArrayList>();
    ArrayList<JButton> Buttons = new ArrayList<JButton>();
    ArrayList<JTextArea> ActionPlanes = new ArrayList<JTextArea>();

    ArrayList<ArrayList> Client_Content = new ArrayList<ArrayList>();
    ArrayList<ArrayList> chekboxes = new ArrayList<ArrayList>();
    ArrayList<JTextPane> outputAreaList = new ArrayList<JTextPane>();

    int[] TABID = new int[256];
    int CurrentTab = 0;


    //Arraylist of all tabs
    ArrayList<JPanel> tabs = new ArrayList<JPanel>();


    /**
     * Initiates the servers and starts the gui
     */
    public startGui(boolean bol, String ip, int Port){
        //Manual_Server = bol;
        catalogue.setmanualServer(bol);
        if(!bol){
            getServerNames();
        }
        else{
            Server = new ServerList(ip, Port);
            serverlist = Server.getServerList();
        }
        StartGui();
        if(bol){
            ManualyServer(ip, Port);
        }
    }

    /**
     * Retrives the active servers and adds them to serverlist
     */
    public ArrayList<JButton> GetButtons(){
        return Buttons;
    }
    private void getServerNames(){
        try{
            Server = UDPServerConnection.getUdpServerlist();
            serverlist = Server.getServerList();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Excepted when trying to recive data: " + e);
        }
    }

    /**
     * Adds a message to the chat log/Output
     * @param TabID The tab index that will be updated
     * @param time The time that the message was received, if this is null the message will not include a time element
     * @param userName The user that sent the message, set this to null is it is a system message
     * @param message The message to display, required
     * @param type Sets whether the message was 1=encrypted, 2=compressed, 3=both, 0=none, changes the highlight colour
     * @param originType Sets the type of message 0=normal, 1=warning, 2=succesful, 3=information, changes text colour
     */
    public void UpdateTabByID2(int TabID, String time, String userName, String message, int type, int originType){
        JTextPane OutputArea = outputAreaList.get(TabID);
        OutputArea.setFont(new Font("Courier New", Font.BOLD, 12));

        StyledDocument doc = OutputArea.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        try{

            StyleConstants.setBackground(keyWord, colors.textHighlight(type));
            //Prints the clock
            if(time != null) {
                StyleConstants.setForeground(keyWord, Color.LIGHT_GRAY);
                doc.insertString(doc.getLength(), "\n[kl. " + time + "] ", keyWord);
            }else{
                doc.insertString(doc.getLength(), "\n", keyWord);
            }
            //If there exists a username, print it with a color based on the name
            StyleConstants.setBold(keyWord, true);
            if (userName != null && userName.length() > 0) {
                StyleConstants.setForeground(keyWord, colors.colorFromString(userName));
                doc.insertString(doc.getLength(), userName + ": ", keyWord);
            }
            switch(originType) {
                case 0:
                    //Easteregg
                    if (userName != null && userName.contains("420")) {
                        colors.color420(doc, keyWord, message);
                    } else {
                        StyleConstants.setBold(keyWord, false);
                        StyleConstants.setForeground(keyWord, Color.BLACK);
                        doc.insertString(doc.getLength(), message, keyWord);
                    }
                    break;
                case 1:
                    StyleConstants.setForeground(keyWord, new Color(165, 40, 46));
                    doc.insertString(doc.getLength(), message, keyWord);
                    break;
                case 2:
                    StyleConstants.setForeground(keyWord, new Color(95, 163, 33));
                    doc.insertString(doc.getLength(), message, keyWord);
                    break;
                case 3:
                    StyleConstants.setForeground(keyWord, new Color(10, 116, 192));
                    doc.insertString(doc.getLength(), message, keyWord);
                    break;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        ChangeColor(panelOne, TabID);
        OutputArea.setCaretPosition(OutputArea.getDocument().getLength());
    }

    /**
     * Returns the currently active tab
     * @return The index of the active tab
     */
    public int getSelectedServerTab(){
        int Index = tabbedPane.getSelectedIndex();
        ChangeColor(panelOne, Index);
        return Index;
    }

    /**
     * Get the InetAddress from the selected tab
     * @return The InetAdress from the selected tab
     */
    public InetAddress getSelectedServerAdress(){
        return Server.getServer((String)serverlist.get(getSelectedServerTab())).getIp();
    }

    /**
     * Get the port from the selected tab
     * @return The port from the selected tab
     */
    public int getSelectedServerPort(){
        return Server.getServer((String)serverlist.get(getSelectedServerTab())).getPort();
    }

    /**
     * Sets the background of a JPanel to the matching one for the tabindex
     * @param panel A JPanel to change
     * @param tabindex The index to fetch colour from
     */
    public void ChangeColor(JPanel panel, int tabindex){
        panel.setBackground(new Color(Loop254(tabindex, 0), Loop254(tabindex, 1), Loop254(tabindex, 2)));
        panel.updateUI();
        panel.validate();
    }

    /**
     * Creates the elements in the gui and specifies their properties
     * @return The GUI
     */
    public JPanel CreatGui(){

        int NumberOfServer = serverlist.size();

        tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("glorous28.png");
        for(int i =0; i < NumberOfServer; i++){
            TABID[CurrentTab] = i;
            CurrentTab += 1;

            ArrayList<JTextArea> serverTextObject = new ArrayList<JTextArea>();
            ArrayList<JTextPane> serverObjects = new ArrayList<JTextPane>();

            JPanel tempPanel2 = new JPanel();
            tempPanel2.setLayout(new GridLayout(2, 0));
            tempPanel2.setVisible(true);
            tempPanel2.setPreferredSize(new Dimension(480, 300));
            JComponent panel;
            if(!catalogue.getmanualServer()) {
                panel = makeTextPanel("Server: " + Server.getServer((String) serverlist.get(i)).getName() + " IP:" +
                        Server.getServer((String) serverlist.get(i)).getIp() + " Port: " + Server.getServer((String)
                        serverlist.get(i)).getPort());
            }
            else{
                panel = makeTextPanel("Server: ");
            }
            panel.setPreferredSize(new Dimension(300, 50));
            panel.setBackground(new Color(Loop254(i, 0), Loop254(i, 1), Loop254(i, 2)));

            JPanel tempPanel3 = new JPanel();
            tempPanel3.setLayout(new GridLayout(3, 0));
            tempPanel3.add(panel);
            tempPanel2.add(tempPanel3);

            final JTextArea inputArea = defineInputarea();
            JScrollPane jScrollPane2 = new JScrollPane();
            serverTextObject.add(inputArea);
            jScrollPane2.getViewport().add(inputArea);
            inputArea.setPreferredSize(new Dimension(330, 120));
            inputArea.setLineWrap(true);
            inputArea.setFont(new Font("Courier New", Font.BOLD, 12));
            inputArea.setWrapStyleWord(true);


            JButton sendMessage = new JButton("Send");
            JButton connectButton = new JButton("Connect");
            JButton disConnectButton = new JButton("Disconnect");
            JButton setkey = new JButton("Set key");
            JCheckBox Compress = new JCheckBox("Compress");
            JCheckBox Encrypted = new JCheckBox("Encrypted");


            connectButton.setPreferredSize(new Dimension(150, 30));
            disConnectButton.setPreferredSize(new Dimension(150, 30));
            setkey.setPreferredSize(new Dimension(90, 20));
            disConnectButton.setEnabled(false);
            setkey.setEnabled(false);

            JTextPane Key = new JTextPane();
            Key.setPreferredSize(new Dimension(120, 20));

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
            tempencryptplane.add(setkey);
            tempPanel3.add(tempencryptplane);

            sendMessage.addActionListener(this);
            connectButton.addActionListener(this);
            disConnectButton.addActionListener(this);
            setkey.addActionListener(this);

            Buttons.add(sendMessage);
            ActionPlanes.add(inputArea);

            JPanel tempPanel5 = new JPanel();
            tempPanel5.setLayout(new FlowLayout());

            JPanel tempPanel4 = new JPanel();
            tempPanel4.setPreferredSize(new Dimension(480, 300));
            tempPanel5.add(jScrollPane2);
            tempPanel5.add(sendMessage);
            tempPanel5.add(tempPanel4);
            tempPanel2.add(tempPanel5);

            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new GridLayout(2, 0));
            JTextPane outputArea = defineOutputarea();
            serverObjects.add(outputArea);
            outputAreaList.add(outputArea);
            outputArea.setPreferredSize(new Dimension(18, 10));

            JScrollPane jScrollPane1 = new JScrollPane();
            jScrollPane1.getViewport().add(outputArea);
            jScrollPane1.setPreferredSize(new Dimension(18, 10));

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
            serverJTextField.add(serverTextObject);

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

            JButton[] buttonList = new JButton[]{connectButton, disConnectButton, sendMessage, setkey};
            JCheckBox[] boxesList = new JCheckBox[]{Compress,Encrypted};
            JTextPane[] textAreas = new JTextPane[]{Key};
            JTextArea[] textAreasfields = new JTextArea[]{inputArea};

            ArrayList<Object> ClientContent = new ArrayList<Object>();

            ClientContent.add(buttonList);
            ClientContent.add(boxesList);
            ClientContent.add(textAreas);
            ClientContent.add(textAreasfields);

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    new keycheker().enterKeyAction(inputArea);
                }
            };
            EventQueue.invokeLater(r);

            Client_Content.add(ClientContent);
            if(catalogue.getmanualServer()){
                i = NumberOfServer;
            }


        }
        panelOne.add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        return panelOne;
    }

    /**
     * Generates a colour from the ip of the active tab
     * @param valu the active tab index
     * @param index offset in the colour from the last value
     * @return the amount of colour for RGB
     */
    private int Loop254(int valu, int index){
        String ip;
        if(!catalogue.getmanualServer()) {
            ip = Server.getServer((String)serverlist.get(valu)).getPort() + Server.getServer((String) serverlist.get
                    (valu)).getIp().toString();
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
    }

    /**
     * Creates a chat log
     * @return The chat log
     */
    public JTextPane defineOutputarea(){
        JTextPane outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setText("Chat logg goes here....");
        outputArea.validate();
        return outputArea;
    }

    /**
     * Creates a input area
     * @return  The input JTextArea Object
     */
    public JTextArea defineInputarea(){
        JTextArea outputArea = new JTextArea ();
        outputArea.setEditable(true);

        outputArea.setText("EnterText");
        outputArea.validate();
        return outputArea;
    }

    /**
     * Initiates the elements of the gui
     */
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
        ImageIcon icon = createImageIcon("glorous28.png");
        frame.setIconImage(icon.getImage());
    }

    /**
     * Acts on events
     * @param e Event identifier
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Object> activeClientObjects = Client_Content.get(getSelectedServerTab());
        JButton Connect = null;
        JButton Disconect = null;
        JButton SetKey = null;
        JCheckBox crypt = null;
        JCheckBox compr = null;
        String Key = "";
        //get connect/disconnect buttons
        int IndexVal;
        boolean FoundBoxes = false;
        boolean Foundbuttons = false;
        boolean FoundKey= false;
        for(Object Target : activeClientObjects) {
            if (Target.getClass() == JButton[].class) {
                JButton[] Buttonsd = (JButton[]) Target;
                Connect = Buttonsd[0];
                Disconect = Buttonsd[1];
                SetKey = Buttonsd[3];
                Foundbuttons = true;
            }
            else if (Target.getClass() == JCheckBox[].class) {
                JCheckBox[] boxes = (JCheckBox[]) Target;
                compr = boxes[0];
                crypt = boxes[1];
                FoundBoxes = true;
            }
            else if (Target.getClass() == JTextPane[].class) {
                JTextPane[] textArea = (JTextPane[]) Target;
                if(textArea[0].getText().length() > 2 && !textArea[0].getText().equals(" ")){
                    Key = textArea[0].getText();
                }
                else{
                    Key = "foobar";
                }
                FoundKey= true;
            }
            if(FoundBoxes && Foundbuttons && FoundKey){
                break;
            }
        }
        if (SetKey.equals(e.getSource())){
            if(!catalogue.GetCryptKey(getSelectedServerTab()).equals(Key)){
                catalogue.SetcryptKey(getSelectedServerTab(), Key);
                //UpdateTabByID(getSelectedServerTab(),"\nKey is now: " + Key, 1);
                UpdateTabByID2(getSelectedServerTab(), null, null, "Key is now: '"+Key + "'", 0, 2);
            }
            else{
                //UpdateTabByID(getSelectedServerTab(),"\nKey where alredy: " + Key, 0);
                UpdateTabByID2(getSelectedServerTab(), null, null, "Key is alredy: '"+Key + "'", 0, 1);

            }
        }
        //check the send button
        int Index_b = 0;
        for (JButton s : Buttons) {
            JTextArea ap = ActionPlanes.get(Index_b);
            Index_b ++;
            if (s.equals(e.getSource()) || ap.equals(e.getSource())) {
                while (true){
                    if(catalogue.MessageInUse == false){
                        catalogue.SetClientMessage (getInputText(), getSelectedServerTab(), compr.isSelected(), crypt.isSelected());

                        removeInputText();
                        System.out.println("adding message");
                        break;
                    }
                }
                System.out.println("loop broken");
                //addToOutputFromInput();
            }
        }
        //handeling disable/enable (connect/disconnect)
        if (Connect.equals(e.getSource()) || Disconect.equals(e.getSource())) {
            for(Object Target : activeClientObjects){
                if (Connect != null && Disconect != null) {
                    if (Target.getClass() == JButton[].class) {
                        JButton[] Buttonss = (JButton[]) Target;
                        for (JButton target : Buttonss) {
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }
                    }
                    else if (Target.getClass() == JCheckBox[].class) {
                        JCheckBox[] checkBox = (JCheckBox[]) Target;
                        for (JCheckBox target : checkBox) {
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }
                    }
                    else if (Target.getClass() == JTextPane[].class) {
                        JTextPane[] textArea = (JTextPane[]) Target;
                        for (JTextPane target : textArea) {
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }
                    }
                    else if (Target.getClass() == JTextArea[].class) {
                        JTextArea[] textArea = (JTextArea[]) Target;
                        for (JTextArea target : textArea) {
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
        if (Connect.equals(e.getSource()) && !catalogue.getmanualServer()) {
            ClientThread.startThread(ID,Server,serverlist);
        }
        if (Disconect.equals(e.getSource()) && !catalogue.getmanualServer()){
            ClientThread.endThread(ID);
        }
    }

    /**
     * Sets the server when the name server has been bypassed
     * @param ip IP to the server
     * @param Port Port to the server
     */
    public void ManualyServer(String ip, int Port){
        int ID = getSelectedServerTab();
        ClientThread.startThreadManualy(ID,ip,Port);
        //Manual_Server = true;
        catalogue.setmanualServer(true);
    }

    /**
     * Clears the content in the output pane
     */
    public void clearOutputWindow(){
        int Index = getSelectedServerTab();
        ArrayList<JTextPane> TempTarget = serverPlanes.get(Index);
        JTextPane Output = TempTarget.get(0);
        Output.setText("");
    }

    /**
     * Takes a string and adds it to the chat log
     * @param Text The string that will be added
     */
    public void addOutputText(String Text){
        int Index = getSelectedServerTab();
        ArrayList<JTextPane> TempTarget = serverPlanes.get(Index);
        JTextPane Output = TempTarget.get(0);
        Output.setText(Output.getText() + "\n" + Text);
    }

    /**
     * Takes the text from the active input pane and returns it
     * @return the text from the active input pane, null if there is none
     */
    public String getInputText(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverJTextField.get(Index);
        JTextArea Input = TempTarget.get(0);
        String inp = Input.getText().trim();
        if (!inp.equals("")){
            return inp;
        }
        else{
            return null;
        }
    }

    /**
     *Takes the message from the active Input pane and adds it to the output pane
     */
    public void addToOutputFromInput(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> InputArray = serverJTextField.get(Index);
        ArrayList<JTextPane> OutputArray = serverPlanes.get(Index);
        JTextArea Input = InputArray.get(0);
        String inputString = Input.getText().trim();
        if (!inputString.equals("")){
            JTextPane Output = OutputArray.get(0);
            Output.setText(Output.getText() + "\n" + Input.getText());
            removeInputText();
        }
    }

    /**
     * Romoves the text in the input field
     */
    public void removeInputText(){
        int Index = getSelectedServerTab();
        ArrayList<JTextArea> TempTarget = serverJTextField.get(Index);
        JTextArea Input = TempTarget.get(0);
        Input.setText("");
    }

    /**
     * This is a function that CAN stop unwanted words or characters
     * @param text Meddelandet som ska filtreras
     * @return Det filtrerade meddelandet
     */
    public String FilterString(String text){
        String OriginalText = text;
        String Modified = "";
        int Lengt = text.getBytes().length;

        return Modified;
    }

    /**
     * Creates a panel with a chatlog and a label that contains a string that describes the server
     * @param text Text that describes the server
     * @return The panel
     */
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /**
     * Takes a path to a image and creates a ImageIcon object
     * @param path The image
     * @return ImageIcon object
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