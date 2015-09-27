import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * RockPaperScissorsServer is the main program that starts the server.
 *
 * Usage: java RockPaperScissors <I>serverhost</I> <I>serverport</I>
 *
 * @author Nicholas A. Mattis
 * @version 9/26/2015
 */
public class RockPaperScissorsServer {

    /**
     * Main method executes the entire program
     *
     * @param args          host and port number
     * @throws Exception    thrown if I/O error occurs
     */
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            usage();
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            DatagramSocket mailbox = new DatagramSocket(
                    new InetSocketAddress(host, port));
            MailboxManager manager = new MailboxManager(mailbox);
            for (;;) {
                manager.receiveMessage();
            }
        } catch (BindException e) {
            System.err.println("The host or port is already in use or cannot " +
                    "be connected to.");
            System.exit(1);
        }
    }

    /**
     * Print usage message and exit.
     */
    private static void usage() {
        System.err.println("Usage: java RockPaperScissorsServer <host> <port>");
        System.exit(1);
    }
}