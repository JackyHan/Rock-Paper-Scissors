import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * MouseCatElephant is the main program, it starts the connection to the server
 * and initializes the model proxy and view
 *
 * Usage: java MouseCatElephant <I>serverhost</I> <I>serverport</I>
 *          <I>clienthost</I> <I>clientport</I> <I>playername</I>
 *
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MouseCatElephant {

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

        DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress(clienthost, clientport));

        MouseCatElephantUI view = MouseCatElephantUI.create(playername);
        final ModelProxy proxy = new ModelProxy(mailbox, new InetSocketAddress(serverhost, serverport));
        proxy.setModelListener(view);
        view.setViewListener(proxy);
        proxy.join(null, playername);
    }

    /**
     * Print usage message and exit.
     */
    private static void usage() {
        System.err.println("Usage: java MouseCatElephant <serverhost> " +
                "<serverport> <clienthost> <clientport> <playername>");
        System.exit(1);
    }
}