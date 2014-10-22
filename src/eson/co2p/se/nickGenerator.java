package eson.co2p.se;

import java.util.Random;

/**
 * Created by gordon on 22/10/14.
 */
public class nickGenerator {
    private static String[] colorNicks = new String[] {"Red", "Blue", "Green", "Purple", "Orange", "Turquoise", "Orange", "Cyan", "Pink", "Brown", "Black", "White", "Hot", "Cold", "Sour", "Sweet", "Slippery", "Big", "Small",
            "Enormous", "Fine", "Pulsating", "Slobbering", "Hacking", "Horny", "Freaky", "Epic", "Awesome", "Super", "Flash", "Growing", "Changing", "Sonic", "Drippery", "Fucking", "Ganja", "420",};
    private static String[] nicks = new String[] {"Panther", "Carrot", "Cactus", "Sea", "Tiger", "Cat", "Dog", "Warthog", "Leopard", "Flower", "Circuit", "Sun", "Star", "Galaxy", "Kangaroo", "Pig", "Cow", "Frog", "Toad",
            "Dick", "Anal", "Possum", "Corpse", "Sanic", "Penis", "Ass", "Kånkelbär", "Dildo", "Cock", "Weed", "Ganja", "Aina", "Knatch", "Hat", "Punch", "Calculus", "Signal", "Pond", "Brother", "Sister", "Bandit"};

    /**
     *  Generates a random nick
     */
    public static String getNew(){
        int seed = ((int) System.currentTimeMillis());
        return (colorNicks[new Random(seed).nextInt(colorNicks.length)] + nicks[new Random(seed).nextInt(nicks.length)]);
    }
}
