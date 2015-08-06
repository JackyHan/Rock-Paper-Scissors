import javax.swing.text.View;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * ModelProxy class, handles messaging between the client and server. Outputs
 * client messages using a socket and PrintStream and then has a ReaderThread
 * that reads data from the socket into a scanner and decides what to tell the
 * view based on the command it has parsed
 *
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class ModelProxy implements ViewListener {

    private DatagramSocket mailbox;
    private SocketAddress destination;
    private ModelListener modelListener;

    /**
     * Constructor
     *
     * @param mailbox       Mailbox
     * @param destination   Destination Mailbox Address
     *
     * @throws IOException  Thrown if I/O error occurred
     */
    public ModelProxy(DatagramSocket mailbox, SocketAddress destination)
            throws IOException {
        this.mailbox = mailbox;
        this.destination = destination;
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
    public void join(ViewProxy proxy, String playername) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('J');
        out.writeUTF(playername);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(new DatagramPacket(payload, payload.length, destination));
    }

    @Override
    public void playerChose(int id, int choice) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('P');
        out.writeByte(id);
        out.writeByte(choice);
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(new DatagramPacket(payload, payload.length, destination));
    }

    @Override
    public void newRound() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('R');
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(new DatagramPacket(payload, payload.length, destination));
    }

    @Override
    public void quit() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('Q');
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(new DatagramPacket(payload, payload.length, destination));
    }

    /**
     * Class ReaderThread receives messages from the network, decodes them, and
     * invokes the proper methods to process them.
     *
     * @author Nicholas A. Mattis
     * @version 8/5/2015
     */
    private class ReaderThread extends Thread {
        public void run() {
            byte[] payload = new byte[128];
            try {
                for (;;) {
                    DatagramPacket packet = new
                            DatagramPacket(payload, payload.length);
                    mailbox.receive(packet);
                    DataInputStream in =
                            new DataInputStream(new
                                    ByteArrayInputStream(payload, 0,
                                    packet.getLength()));
                    byte cmd = in.readByte();
                    String pname;
                    int id, value, a1, v, a2;
                    switch (cmd) {
                        case 'I':
                            id = in.readByte();
                            modelListener.getID(id);
                            break;
                        case 'N':
                            id = in.readByte();
                            pname = in.readUTF();
                            modelListener.name(id, pname);
                            break;
                        case 'S':
                            id = in.readByte();
                            value = in.readByte();
                            modelListener.score(id, value);
                            break;
                        case 'C':
                            id = in.readByte();
                            a1 = in.readByte();
                            modelListener.choice(id, a1);
                            break;
                        case 'O':
                            a1 = in.readByte();
                            v = in.readByte();
                            a2 = in.readByte();
                            modelListener.outcome(a1, v, a2);
                            break;
                        case 'R':
                            modelListener.newRoundStarted();
                        case 'Q':
                            modelListener.quit();
                        default:
                            System.err.println("Bad message");
                            break;
                    }
                }
            } catch (IOException e) {
            }
            finally {
                mailbox.close();
            }
        }
    }
}