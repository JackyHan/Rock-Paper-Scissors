import java.io.IOException;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class SessionManager implements ViewListener {

    private boolean clientCount = false;
    private MouseCatElephantModel model;

    public SessionManager() {}

    @Override
    public synchronized void join(ViewProxy proxy, String playername)
            throws IOException {
        if (clientCount && model.getPlayerCount() != 0) {
            model.addModelListener(1, playername, proxy);
            proxy.setViewListener(model);
            clientCount = false;
        } else {
            model = new MouseCatElephantModel();
            model.addModelListener(0, playername, proxy);
            proxy.setViewListener(model);
            clientCount = true;
        }
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