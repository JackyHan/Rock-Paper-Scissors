import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MouseCatElephantServer {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            usage();
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress(host, port));

        MailboxManager manager = new MailboxManager(mailbox);

        for (;;) {
            manager.receiveMessage();
        }
    }

    private static void usage() {
        System.err.println("Usage: java MouseCatElephantServer <host> <port>");
        System.exit(1);
    }
}