import java.io.IOException;

/**
 * SessionManager class, creates a new ModelObject for each two person session
 * and links the viewListener and ModelListeners for each client to that session
 * it does this for every two people. If a person disconnects before they are
 * matched it resets the model for the next client connection.
 *
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class SessionManager implements ViewListener {

    private boolean clientCount = false;
    private MouseCatElephantModel model;

    /**
     * Constructor
     */
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