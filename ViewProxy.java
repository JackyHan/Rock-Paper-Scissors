import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class ViewProxy implements ModelListener {

    private DatagramSocket mailbox;
    private SocketAddress clientAddress;
    private ViewListener viewListener;

    public ViewProxy (DatagramSocket mailbox, SocketAddress clientAddress) {
        this.mailbox = mailbox;
        this.clientAddress = clientAddress;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void getID(int idnum) throws IOException {

    }

    @Override
    public void name(int id, String playername) throws IOException {

    }

    @Override
    public void score(int id, int value) throws IOException {

    }

    @Override
    public void choice(int id, int playerchoice) throws IOException {

    }

    @Override
    public void outcome(int animal1, int verb, int animal2) throws IOException {

    }

    @Override
    public void newRoundStarted() throws IOException {

    }

    @Override
    public void quit() throws IOException {

    }

    public boolean process(DatagramPacket datagramPacket) throws IOException {
        boolean discard = false;
        return discard;
    }
}