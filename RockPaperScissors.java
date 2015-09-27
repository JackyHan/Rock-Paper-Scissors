import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * RockPaperScissors is the main program, it starts the connection to the server
 * and initializes the model proxy and view
 *
 * Usage: java RockPaperScissors <I>serverhost</I> <I>serverport</I>
 *          <I>clienthost</I> <I>clientport</I> <I>playername</I>
 *
 * @author Nicholas A. Mattis
 * @version 9/26/2015
 */
public class RockPaperScissors {

    /**
     * Main method that executes entire program
     *
     * @param args          server host & port, client host & port, playername
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{

        if (args.length != 5) {
            usage();
        }
        String serverhost = args[0];
        int serverport = Integer.parseInt(args[1]);
        String clienthost = args[2];
        int clientport = Integer.parseInt(args[3]);
        String playername = args[4];

        try {
            DatagramSocket mailbox = new DatagramSocket(
                    new InetSocketAddress(clienthost, clientport));
            RockPaperScissorsUI view = RockPaperScissorsUI.create(playername);
            final ModelProxy proxy = new ModelProxy(mailbox,
                    new InetSocketAddress(serverhost, serverport));
            proxy.setModelListener(view);
            view.setViewListener(proxy);
            proxy.join(null, playername);
        } catch (BindException e) {
            System.err.println("The host or port is already in use.");
            System.exit(1);
        }
    }

    /**
     * Print usage message and exit.
     */
    private static void usage() {
        System.err.println("Usage: java RockPaperScissors <serverhost> " +
                "<serverport> <clienthost> <clientport> <playername>");
        System.exit(1);
    }
}