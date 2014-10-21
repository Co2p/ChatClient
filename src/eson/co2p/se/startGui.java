package eson.co2p.se;

/**
 * The main gui for the chat, handles selection of servers, setting the cryptography of the messages etc
 *
 * Created by Tony on 15/10/2014.
 */
import javax.swing.*;
import javax.swing.text.*;
import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class startGui extends JFrame implements ActionListener {

    JPanel panelOne;
    boolean Manual_Server = false;
    ServerList Server;
    ArrayList serverlist;
    final JFrame frame = new JFrame("Glorious Chat");


    /**
     * Initiates the servers and starts the gui
     */
    public startGui(boolean bol, String ip, int Port){
        Manual_Server = bol;
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

    static Point mouseDownCompCoords;
    JTabbedPane tabbedPane;
    JComponent Servers;
    //a list of all the added server
    ArrayList<ArrayList> serverPlanes = new ArrayList<ArrayList>();
    ArrayList<ArrayList> serverJTextField = new ArrayList<ArrayList>();
    ArrayList<JButton> Buttons = new ArrayList<JButton>();
    ArrayList<JTextField> ActionPlanes = new ArrayList<JTextField>();

    ArrayList<ArrayList> Client_Content = new ArrayList<ArrayList>();
    ArrayList<ArrayList> chekboxes = new ArrayList<ArrayList>();
    ArrayList<JTextPane> outputAreaList = new ArrayList<JTextPane>();

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

    public void UpdateTabByID(int TabID,String message, int type){
        JTextPane OutputArea = outputAreaList.get(TabID);

        StyledDocument doc = OutputArea.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        switch(type) {
            case 0:
                StyleConstants.setForeground(keyWord, Color.RED);
                break;
            case 1:
                StyleConstants.setForeground(keyWord, Color.GREEN);
                break;
            case 2:
                StyleConstants.setForeground(keyWord, Color.BLUE);
                break;
        }
        StyleConstants.setBold(keyWord, true);
        try{
            doc.insertString(doc.getLength(), "\n"+message, keyWord );
        }catch(Exception e) {
            System.out.println(e);
        }
        OutputArea.setCaretPosition(OutputArea.getDocument().getLength());
    }

    public void UpdateTabByID2(int TabID, String time, String userName, String message, int type, int originType){
        JTextPane OutputArea = outputAreaList.get(TabID);

        StyledDocument doc = OutputArea.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        try{
            //if the message is encrypted, compressed or both, add different backgrounds
            switch(type){
                case 1:
                    //compress
                    StyleConstants.setBackground(keyWord, new Color(206, 255, 185));
                    break;
                case 2:
                    //crypt
                    StyleConstants.setBackground(keyWord, Color.YELLOW);
                    break;
                case 3:
                    //compress + crypt
                    StyleConstants.setBackground(keyWord, new Color(255, 169, 170));
                    break;
            }
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
                StyleConstants.setForeground(keyWord, colorFromString(userName));
                doc.insertString(doc.getLength(), userName + ": ", keyWord);
            }
            switch(originType) {
                case 0:
                    //Easteregg
                    if (userName.contains("420")) {
                        color420(doc, keyWord, message);
                    } else {
                        StyleConstants.setBold(keyWord, false);
                        StyleConstants.setForeground(keyWord, Color.BLACK);
                        doc.insertString(doc.getLength(), message, keyWord);
                    }
                    break;
                case 1:
                    StyleConstants.setForeground(keyWord, Color.RED);
                    doc.insertString(doc.getLength(), message, keyWord);
                    break;
                case 2:
                    StyleConstants.setForeground(keyWord, Color.GREEN);
                    doc.insertString(doc.getLength(), message, keyWord);
                    break;
                case 3:
                    StyleConstants.setForeground(keyWord, Color.BLUE);
                    doc.insertString(doc.getLength(), message, keyWord);
                    break;
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        OutputArea.setCaretPosition(OutputArea.getDocument().getLength());
    }

    private void color420(StyledDocument doc, SimpleAttributeSet set, String message){
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

    private Color colorFromString(String nick){
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


            ArrayList<JTextField> serverTextObject = new ArrayList<JTextField>();
            ArrayList<JTextPane> serverObjects = new ArrayList<JTextPane>();

            JPanel tempPanel2 = new JPanel();
            tempPanel2.setLayout(new GridLayout(2, 0));
            tempPanel2.setVisible(true);
            tempPanel2.setPreferredSize(new Dimension(480, 300));
            JComponent panel;
            if(!Manual_Server) {
                panel = makeTextPanel("Server: " + Server.getServer((String) serverlist.get(i)).getName() + " Ip:" + Server.getServer((String) serverlist.get(i)).getIp() + " Port: " + Server.getServer((String) serverlist.get(i)).getPort());
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


            JTextField  inputArea = defineInputarea();
            JScrollPane jScrollPane2 = new JScrollPane();
            serverTextObject.add(inputArea);
            jScrollPane2.getViewport().add(inputArea);
            inputArea.setPreferredSize(new Dimension(330, 120));


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
            inputArea.addActionListener(this);

            Buttons.add(sendMessage);
            ActionPlanes.add(inputArea);


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
            JTextField[] textAreasfields = new JTextField[]{inputArea};
            //JCheckBox[] Chekboxes = new JCheckBox[]{Compress,Encrypted};

            ArrayList<Object> ClientContent = new ArrayList<Object>();

            ClientContent.add(buttonList);
            ClientContent.add(boxesList);
            ClientContent.add(textAreas);
            ClientContent.add(textAreasfields);
            //ClientContent.add(Chekboxes);


            Client_Content.add(ClientContent);
            if(Manual_Server){
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
        if(!Manual_Server) {
            ip = Server.getServer((String) serverlist.get(valu)).getIp().toString();
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

    public JTextPane defineOutputarea(){
        JTextPane outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setText("Chat logg goes here....");
        outputArea.validate();
        return outputArea;
    }

    public JTextField  defineInputarea(){
        JTextField  outputArea = new JTextField ();
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


    public void UpdateWindows(JTextPane inputArea, JTextPane outputArea){
        outputArea.setText(outputArea.getText()+"\n"+ inputArea.getText());
        inputArea.setText("");
    }

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
                UpdateTabByID(getSelectedServerTab(),"\nKey is now: " + Key, 1);
            }
            else{
                UpdateTabByID(getSelectedServerTab(),"\nKey where alredy: " + Key, 0);
            }
        }
        //check the send button
        int Index_b = 0;
        for (JButton s : Buttons) {
            JTextField ap = ActionPlanes.get(Index_b);
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
                        for (int i = 0; i < Buttonss.length; i++) {
                            JButton target = Buttonss[i];
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
                    else if (Target.getClass() == JTextPane[].class) {
                        JTextPane[] textArea = (JTextPane[]) Target;
                        for (int i = 0; i < textArea.length; i++) {
                            JTextPane target = textArea[i];
                            if (target.isEnabled()) {
                                target.setEnabled(false);
                            } else {
                                target.setEnabled(true);
                            }
                        }
                    }
                    else if (Target.getClass() == JTextField[].class) {
                        JTextField[] textArea = (JTextField[]) Target;
                        for (int i = 0; i < textArea.length; i++) {
                            JTextField target = textArea[i];
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
        if (Connect.equals(e.getSource()) && !Manual_Server) {
             ClientThread.startThread(ID,Server,serverlist);
        }
        if (Disconect.equals(e.getSource()) && !Manual_Server){
            ClientThread.endThread(ID);
        }
    }

    public void ManualyServer(String ip, int Port){
        int ID = getSelectedServerTab();
        ClientThread.startThreadManualy(ID,ip,Port);
        Manual_Server = true;
    }
    public void clearOutputWindow(){
        int Index = getSelectedServerTab();
        ArrayList<JTextPane> TempTarget = serverPlanes.get(Index);
        JTextPane Output = TempTarget.get(0);
        Output.setText("");
    }

    public void addOutputText(String Text){
        int Index = getSelectedServerTab();
        ArrayList<JTextPane> TempTarget = serverPlanes.get(Index);
        JTextPane Output = TempTarget.get(0);
        Output.setText(Output.getText() + "\n" + Text);
    }
    public String getInputText(){
        int Index = getSelectedServerTab();
        ArrayList<JTextField> TempTarget = serverJTextField.get(Index);
        JTextField Input = TempTarget.get(0);
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
        ArrayList<JTextField> TempTarget = serverJTextField.get(Index);
        ArrayList<JTextPane> TempTarget2 = serverPlanes.get(Index);
        JTextField Input = TempTarget.get(0);
        String inp = Input.getText().trim();
        if (!inp.equals("")){
            JTextPane Output = TempTarget2.get(0);
            Output.setText(Output.getText() + "\n" + Input.getText());
            removeInputText();
        }
    }

    public void removeInputText(){
        int Index = getSelectedServerTab();
        ArrayList<JTextField> TempTarget = serverJTextField.get(Index);
        JTextField Input = TempTarget.get(0);
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