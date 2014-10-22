package eson.co2p.se;
/** Checksum.java
 * Generates a basic checksum
 * @author Per Nordlinder (per@cs.umu.se), Jon Hollstr�m (jon@cs.umu.se)
 */

public class Checksum {

  /* Namn: calc
   * Syfte: Ber�knar checksumma p� en byte-array.
   * Argument: buf   - Datat som checksumman skall ber�knas p�.
   *           count - Det antal bytes som checksumman skall ber�knas p�.
   * Returnerar: checksumman som en byte.
   */
   public static byte calc(byte[] buf, int count) {
      int sum = 0;
      int i = 0;

      while((count--) != 0) {
         sum += (buf[i] & 0x000000FF);
         i++;
         if((sum & 0x00000100) != 0) {
            sum &= 0x000000FF;
            sum++;
         }     
      }

      return (byte)~(sum & 0xFF);
   }
}

