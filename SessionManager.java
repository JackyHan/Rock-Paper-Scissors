import java.io.IOException;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class SessionManager implements ViewListener {

    private int clientCount = 0;
    private MouseCatElephantModel model;

    public SessionManager() {}

    @Override
    public synchronized void join(ViewProxy proxy, String playername)
            throws IOException {
        if (clientCount == 1) {
            model.addModelListener(1, proxy);
            proxy.setViewListener(model);
            clientCount = 0;
        }
        model = new MouseCatElephantModel();
        model.addModelListener(0, proxy);
        proxy.setViewListener(model);
        clientCount++;
    }

    @Override
    public void playerChose(int id, int choice) throws IOException {
    }

    @Override
    public void newRound() throws IOException {
    }

    @Override
    public void quit() throws IOException {
    }
}