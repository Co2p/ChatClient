package eson.co2p.se;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.*;

/**
 * Created by gordon on 08/10/14.
 *
 * Handles the conversion to bytestreams, and adds padding
 */
public class byteConverter {

    public static void headerBuilder(ArrayList<Integer> format, ArrayList<String> content){
        // Go trough the format-Array which contains the length of each element in the
        // array "content" The string is expected to be in UTF-8.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        for(int i = 0;i < format.size(); i++){
            //skapar en ny byte med bytelängden som finns i formatArrayen
            byte[] stringByte;
            //Lägger in infot som finns i contentArrayen
            try {
                stringByte = content.get(i).getBytes("UTF-8");
                System.out.print(" ");
                outputStream.write(stringByte);
            }catch(Exception e){
                System.out.println("Här gick det lite fel");
            }
        }
        byte totBytes[] = outputStream.toByteArray();
        //testskrivut bytesen
        System.out.println("Printing bytes:");
        for (byte b : totBytes) {
            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
        System.out.println("\nPrinting done!");
    }
}
