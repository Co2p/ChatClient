package eson.co2p.se;

/**
 * Defines the possible commands that the user can call in the chat
 * @author Isidor fucking NYGREN on 2014-10-22.
 */
public class Commands {
    public static byte[] getCommand(String command, startGui GUI, int Tabid) {
        byte[] returnArr = null;
        String commands[] = command.split(" ", 2);
        if (commands[0].equalsIgnoreCase("§nick")) {
            if (commands.length > 1) {
                returnArr = Message.changeNick(commands[1]);
                System.out.println("newNick = '" + commands[1] + "'");
            } else {
                System.out.println("Too short username");
                GUI.UpdateTabByID(Tabid, "ERROR: Too short username", 0);
            }
        } else if (commands[0].equalsIgnoreCase("§nickgen")) {
            returnArr = Message.changeNick(nickGenerator.getNew());
            System.out.println("newNick = '" + nickGenerator.getNew() + "'");
        }else if (commands[0].equalsIgnoreCase("§help")) {
            String message = "";
            for (int i = 0; i < GetComandList().length; i++) {
                message = message + "\n" + GetComandList()[i] + "\n" + GetExplanation()[i] + "\n";
            }
            GUI.UpdateTabByID(Tabid, message, 2);
        } else if(commands[0].equals("§KillServer")){
            catalogue.SetDosReq(Tabid);
        }
        else{  //  If no command found, print commandNotFound
            GUI.UpdateTabByID(Tabid, "Command not found", 0);
        }
        return returnArr;
    }

    /**
     * Returns a explanations of the commands in a array of Strings
     * @return Explanations
     */
    private static String[] GetExplanation(){
        return new String[]{"Change the username\nusage: §nick <new name>",
                "Generate a new username\nusage: §nickgen",
                "Give command info\nusage: §Help",
                "ddos the current server\nWarning DON'T DO IT!\nusage: §KillServer"};
    }

    /**
     * Returns names of the commands in a array of Strings
     * @return the names
     */
    private static String[] GetComandList(){
        return new String[]{"§nick", "§nickgen", "§help","§KillServer"};
    }
}
