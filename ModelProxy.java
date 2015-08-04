import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * ModelProxy class, handles messaging between the client and server. Outputs
 * client messages using a socket and PrintStream and then has a ReaderThread
 * that reads data from the socket into a scanner and decides what to tell the
 * view based on the command it has parsed
 *
 * @author Nicholas A. Mattis
 * @version 7/22/2015
 */
public class ModelProxy implements ViewListener {

    private Socket socket;
    private PrintStream out;
    private Scanner in;
    private ModelListener modelListener;

    /**
     * Constructor
     *
     * @param socket        connected socket
     * @throws IOException  thrown if an I/O error occurs
     */
    public ModelProxy(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintStream(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
    }

    /**
     * Sets the model listener object for this model proxy.
     *
     * @param modelListener     Model listener
     */
    public void setModelListener(ModelListener modelListener) {
        this.modelListener = modelListener;
        new ReaderThread().start();
    }

    @Override
    public void join(String playername) throws IOException {
        out.printf("join %s\n", playername);
    }

    @Override
    public void playerChose(int choice) throws IOException {
        out.printf("choose %d\n", choice);
    }

    @Override
    public void newRound() throws IOException {
        out.printf("new\n");
    }

    @Override
    public void quit() throws IOException {
        out.printf("quit\n");
    }

    /**
     * Class ReaderThread receives messages from the network, decodes them, and
     * invokes the proper methods to process them.
     *
     * @author Nicholas A. Mattis
     * @version 7/22/2015
     */
    private class ReaderThread extends Thread {
        public void run() {
            try {
                while (in.hasNextLine()) {
                    String message = in.nextLine();
                    Scanner s = new Scanner(message);
                    String cmd = s.next();
                    if (cmd.equals("id")) {
                        int id = s.nextInt();
                        modelListener.getID(id);
                    } else if (cmd.equals("name")) {
                        int id = s.nextInt();
                        String playername = s.next();
                        modelListener.name(id, playername);
                    } else if (cmd.equals("score")) {
                        int id = s.nextInt();
                        int value = s.nextInt();
                        modelListener.score(id, value);
                    } else if (cmd.equals("choice")) {
                        int id = s.nextInt();
                        int animal = s.nextInt();
                        modelListener.choice(id, animal);
                    } else if (cmd.equals("outcome")) {
                        int animal1 = s.nextInt();
                        int verb = s.nextInt();
                        int animal2 = s.nextInt();
                        modelListener.outcome(animal1, verb, animal2);
                    } else if (cmd.equals("new")) {
                        modelListener.newRoundStarted();
                    } else if (cmd.equals("quit")) {
                        modelListener.quit();
                    } else {
                        System.err.println("Bad Message");
                    }
                }
            } catch (IOException e) {
            }
        }
    }
}