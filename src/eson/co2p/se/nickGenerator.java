package eson.co2p.se;

import java.util.Random;

/**
 * Generates a random nickname
 *
 * @author Gordon, Isidor, Tony 24 October 2014
 */
public class nickGenerator {
    private static String[] colorNicks = new String[] {"Red", "Blue", "Green", "Purple", "Orange", "Turquoise", "Orange",
            "Cyan", "Pink", "Brown", "Black", "White", "Hot", "Cold", "Sour", "Sweet", "Slippery", "Big", "Small", "Master",
            "Enormous", "Fine", "Pulsating", "Hacking", "Freaky", "Epic", "Awesome", "Super", "Flash", "Growing",
            "Changing", "Sonic", "Holy", "Drippery", "Fucking", "Ganja", "420", "Horny", "Slobbering"};

    private static String[] nicks = new String[] {"Panther", "Carrot", "Cactus", "Sea", "Tiger", "Cat", "Dog", "Warthog",
            "Leopard", "Flower", "Circuit", "Sun", "Star", "Galaxy", "Kangaroo", "Pig", "Cow", "Frog", "Toad", "Master",
            "Possum", "Corpse", "Hat", "Punch", "Calculus", "Signal", "Pond", "Brother", "Sister", "Bandit", "Satanic",
            "Dick", "Anal", "Penis", "Ass", "Kånkelbär", "Dildo", "Cock", "Weed", "Ganja", "Aina", "Knatch"};

    private static int[] offset = new int[] {0, 0};

    /**
     *  Generates a random nick
     */
    public static String getNew(){
        int seed = ((int) System.currentTimeMillis());
        return (colorNicks[new Random(seed).nextInt(colorNicks.length-offset[0])] +
                nicks[new Random(seed).nextInt(nicks.length-offset[1])]);
    }

    public static void makeClean(boolean c){
        if (c){
            offset[0]=6;
            offset[1]=11;
        }
        else {
            offset[0]=0;
            offset[1]=0;
        }
    }
}
