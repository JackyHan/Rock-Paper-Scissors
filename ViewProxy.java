import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * ViewProxy class, handles messaging between the server and client. Outputs
 * server messages using UDP datagrams. The Process method reads the
 * datagrams and decides what to tell the view based on the command it has
 * parsed
 *
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class ViewProxy implements ModelListener {

    private DatagramSocket mailbox;
    private SocketAddress clientAddress;
    private ViewListener viewListener;

    /**
     * Constructor
     *
     * @param mailbox       Server's Mailbox.
     * @param clientAddress Client's Mailbox address.
     */
    public ViewProxy(DatagramSocket mailbox, SocketAddress clientAddress) {
        this.mailbox = mailbox;
        this.clientAddress = clientAddress;
    }

    /**
     * Sets the viewlistener for this proxy
     *
     * @param viewListener ViewListener object
     */
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    /**
     * Report the user id the server assigns either 0 or 1
     *
     * @param  idnum    id of player
     * @throws          IOException thrown if I/O error occurred
     */
    public void getID(int idnum) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('I');
        out.writeByte(idnum);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Reports one of the player's names.
     *
     * @param id            id num of player being reported
     * @param playername    the name of the player being reported
     * @throws IOException  thrown if I/O error occurred
     */
    public void name(int id, String playername) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('N');
        out.writeByte(id);
        out.writeUTF(playername);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Sent to each client to report one of the player's scores
     *
     * @param id            id num of player whose score is being reported
     * @param value         the value of the player's score
     * @throws IOException  thrown if I/O error occurred
     */
    public void score(int id, int value) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('S');
        out.writeByte(id);
        out.writeByte(value);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Reports that one of the clients has selected an animal
     *
     * @param id            id num of player who made the choice
     * @param playerchoice  0 for mouse, 1 for cat, or 2 for elephant
     * @throws IOException  thrown if I/O error occurred
     */
    public void choice(int id, int playerchoice) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('C');
        out.writeByte(id);
        out.writeByte(playerchoice);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Reports the outcome of a round.
     *
     * @param animal1       int value of animal
     * @param verb          0 for ties, 1 for frightens, 2 for eats, or 3 for
     *                      stomps
     * @param animal2       int value of animal
     * @throws IOException  thrown if I/O error occurred
     */
    public void outcome(int animal1, int verb, int animal2) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('O');
        out.writeByte(animal1);
        out.writeByte(verb);
        out.writeByte(animal2);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Report that a new round has started
     *
     * @throws IOException  thrown if I/O error occurred
     */
    public void newRoundStarted() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('R');
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Reports when game session has been terminated
     *
     * @throws IOException  thrown if I/O error occurred
     */
    public void quit() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('Q');
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    /**
     * Process a recieved datagram
     *
     * @param data datagram packet
     * @return True to discard the viewproxy, false otherwise.
     * @throws IOException thrown if an I/O error occurred
     */
    public boolean process(DatagramPacket data) throws IOException {
        boolean discard = false;
        DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(data.getData(), 0, data.getLength()));
        String playername;
        int id, choice;
        byte cmd = in.readByte();
        switch (cmd) {
            case 'J':
                playername = in.readUTF();
                viewListener.join(ViewProxy.this, playername);
                break;
            case 'P':
                id = in.readByte();
                choice = in.readByte();
                viewListener.playerChose(id, choice);
                break;
            case 'R':
                viewListener.newRound();
                break;
            case 'Q':
                viewListener.quit();
                discard = true;
                break;
            default:
                System.err.println("Bad message");
                break;
        }
        return discard;
    }
}