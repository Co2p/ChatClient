package eson.co2p.se;

/**
 * Defines OP codes and what they mean
 */
public class OpCodes {

		/**
		 * nameserver codes:
		 **/
		public static final int REG = 0;
		public static final int ACK = 1;
		public static final int ALIVE = 2;
		public static final int GETLIST = 3;
		public static final int SLIST = 4;
		/*public static final int CHTOPIC = 5;*/

		/**
		 * errorcodes nameserver:
		 **/
		public static final int NOTREG = 100;
		public static final int UNKNOWNOP = 101;

		/**
		 * chatklient kodes:
		 **/
		public static final int MESSAGE = 10;
		public static final int QUIT = 11;
		public static final int JOIN = 12;
		public static final int CHNICK = 13;

		public static final int UJOIN = 16;
		public static final int ULEAVE = 17;
		public static final int UCNICK = 18;
		public static final int NICKS = 19;

}
