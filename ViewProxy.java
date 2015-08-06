import java.io.*;
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

    /**
     * Constructor
     *
     * @param mailbox           Server's Mailbox.
     * @param clientAddress     Client's Mailbox address.
     */
    public ViewProxy (DatagramSocket mailbox, SocketAddress clientAddress) {
        this.mailbox = mailbox;
        this.clientAddress = clientAddress;
    }

    /**
     * Sets the viewlistener for this proxy
     *
     * @param viewListener      ViewListener object
     */
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void newRoundStarted() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeByte('R');
        out.close();
        byte[] payload = baos.toByteArray();
        mailbox.send(
                new DatagramPacket(payload, payload.length, clientAddress));
    }

    @Override
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
     * @param data          datagram packet
     * @return              True to discard the viewproxy, false otherwise.
     * @throws IOException  thrown if an I/O error occurred
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