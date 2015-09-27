import java.io.IOException;

/**
 * SessionManager class, creates a new ModelObject for each two person session
 * and links the viewListener and ModelListeners for each client to that session
 * it does this for every two people. If a person disconnects before they are
 * matched it resets the model for the next client connection.
 *
 * @author Nicholas A. Mattis
 * @version 9/26/2015
 */
public class SessionManager implements ViewListener {

    private boolean clientCount = false;
    private RockPaperScissorsModel model;

    /**
     * Constructor
     */
    public SessionManager() {}

    /**
     * Join the game session.
     *
     * @param playername    name of the player starting the client
     * @param proxy         player's view proxy
     * @throws IOException  thrown if an I/O error occurred
     */
    public synchronized void join(ViewProxy proxy, String playername)
            throws IOException {
        if (clientCount && model.getPlayerCount() != 0) {
            model.addModelListener(1, playername, proxy);
            proxy.setViewListener(model);
            clientCount = false;
        } else {
            model = new RockPaperScissorsModel();
            model.addModelListener(0, playername, proxy);
            proxy.setViewListener(model);
            clientCount = true;
        }
    }

    /**
     * sends the id of the player and what button they clicked
     *
     * @param id            (int) player id
     * @param choice        (int) which choice they made
     * @throws IOException  thrown if an I/O error occurred
     */
    public void playerChose(int id, int choice) throws IOException {
    }

    /**
     * Sent when player starts a new round
     *
     * @throws IOException  thrown if an I/O error occurred
     */
    public void newRound() throws IOException {
    }

    /**
     * Sent when a player closes the window
     *
     * @throws IOException thrown if an I/O error occurred
     */
    public void quit() throws IOException {
    }
}