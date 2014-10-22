package eson.co2p.se;
/**
 * Encrypts and decrypts a bytearray using a key
 * @author Per Nordlinder (per@cs.umu.se), Jon Hollstr�m (jon@cs.umu.se)
 */

public class Crypt {

   /* Namn:       {en, de}crypt 
    * Purpose:    Krypterar eller dekrypterar data 
    * Argument:   src - Buffert med datan som ska behandlas 
    *             srclen - L�ngden i bytes p� src 
    *             key - Krypteringsnyckel som skall anv�ndas
    *             keylen - L�ngden i bytes p� krypteringsnyckeln
    * Returnerar: Ingenting 
    */ 
    
   public static void encrypt(byte[] src, int srclen, byte[] key, int keylen) {
      
      for(int i=0; i<srclen; i++) 
         src[i] ^= key[i%keylen]; 
   }
   
  	public static void decrypt(byte[] src, int srclen, byte[] key, int keylen) {
      encrypt(src, srclen, key, keylen);
  	}

}
