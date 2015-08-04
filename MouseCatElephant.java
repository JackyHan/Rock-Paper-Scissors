import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * MouseCatElephant is the main program, it starts the connection to the server
 * and initializes the model proxy and view
 *
 * Usage: java MouseCatElephant <I>host</I> <I>port</I> <I>playername</I>
 *
 * @author Nicholas A. Mattis
 * @version 7/20/2015
 */
public class MouseCatElephant {

    /**
     * Main method that executes entire program
     *
     * @param args          host to connect, port number, and player name
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{

        if (args.length != 3) {
            usage();
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String playername = args[2];

        Socket socket = new Socket();
        try {
            socket.connect (new InetSocketAddress(host, port));
        } catch (IOException e) {
            System.err.println("Could not connect to the host " + host +
                    " on port " + port);
            System.exit(1);
        }
        MouseCatElephantUI view = MouseCatElephantUI.create(playername);
        ModelProxy proxy = new ModelProxy(socket);
        proxy.setModelListener(view);
        view.setViewListener(proxy);
        proxy.join(playername);
    }

    /**
     * Print usage message and exit.
     */
    private static void usage() {
        System.err.println("Usage: java MouseCatElephant <host> " +
                "<port> <playername>");
        System.exit(1);
    }
}