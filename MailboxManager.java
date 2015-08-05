import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MailboxManager {

    private DatagramSocket mailbox;
    private HashMap<SocketAddress, ViewProxy> proxyMap = new HashMap<SocketAddress, ViewProxy>();
    private byte[] payload = new byte[128];
    private SessionManager sessionManager = new SessionManager();

    public MailboxManager(DatagramSocket mailbox) {
        this.mailbox = mailbox;
    }

    public void receiveMessage() throws IOException {
        DatagramPacket packet = new DatagramPacket(payload, payload.length);
        mailbox.receive(packet);
        SocketAddress clientAddress = packet.getSocketAddress();
        ViewProxy proxy = proxyMap.get(clientAddress);
        if (proxy == null) {
            proxy = new ViewProxy(mailbox, clientAddress);
            proxy.setViewListener(sessionManager);
            proxyMap.put(clientAddress, proxy);
        }
        if (proxy.process(packet)) {
            proxyMap.remove(clientAddress);
        }
    }
}