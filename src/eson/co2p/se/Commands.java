package eson.co2p.se;

/**
 * Defines the possible commands that the user can call in the chat
 * @author Isidor, Gordon, Tony 23 October 2014
 */
public class Commands {

    /**
     * Executes the given command, returns a list of commands if it was not found
     * @param command The users command
     * @param GUI The gui object
     * @param Tabid The active tab in the gui
     * @return A message to print out in the gui
     */
    public static byte[] getCommand(String command, startGui GUI, int Tabid) {
        byte[] returnArr = null;
        String commands[] = command.split(" ", 2);

        if (commands[0].equalsIgnoreCase("§nick")) {
            if (commands.length > 1) {
                returnArr = Message.changeNick(commands[1]);
            } else {
                System.out.println("Too short username entered");
                GUI.UpdateTabByID2(Tabid, null, null, "ERROR: Too short username", 0, 1);
            }
        } else if (commands[0].equalsIgnoreCase("§nickgen")) {
            returnArr = Message.changeNick(nickGenerator.getNew());
        } else if(commands[0].equalsIgnoreCase("§clear")) {
            GUI.clearOutputWindow();
        } else if (commands[0].equalsIgnoreCase("§nicks")) {
            GUI.UpdateTabByID2(Tabid, null, null, "Currently connected users: " + catalogue.getNicknames(Tabid), 0, 3);
        } else if (commands[0].equalsIgnoreCase("§help")) {
            GUI.UpdateTabByID2(Tabid, null, null, helpList(), 0, 3);
        } else if(commands[0].equals("§KillServer")){
            catalogue.SetDosReq(Tabid);
        } else if(commands[0].equals("§quit")){
            returnArr = Message.quitServer();
        }
        else{  //  If no command found, print a list of commands
            GUI.UpdateTabByID2(Tabid, null, null, helpList(), 0, 3);
        }
        return returnArr;
    }

    /**
     * Returns a list with all of the possible commands
     * @return A list with all of the possible commands
     */
    private static String helpList(){
        String message = "";
        for (int i = 0; i < GetExplanation().length; i++) {
            message = message + "\n" + GetExplanation()[i] + "\n";
        }
        return message;
    }

    /**
     * Returns a explanations of the commands in a array of Strings
     * @return Explanations to the commands
     */
    private static String[] GetExplanation(){
        return new String[]{"§nick\nChange the username\nusage: §nick <new name>",
                "§nickgen\nGenerate a new username\nusage: §nickgen",
                "§clear\nClears the chat log\nusage: §clear",
                "§nicks\nDisplays a list of all connected users\nusage: §nicks",
                "§help\nGive command info\nusage: §help",
                "§quit\nquits the server\nusage: §quit"};
    }
}
