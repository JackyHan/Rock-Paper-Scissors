import java.io.IOException;

/**
 * Interface ViewListener specifies the interface for an object that is
 * triggered by events from the view object in the MouseCatElephant game.
 *
 * @author Nicholas A. Mattis
 * @version 7/22/2015
 */
public interface ViewListener {

    /**
     * Join the game session.
     *
     * @param playername    name of the player starting the client
     * @throws IOException  thrown if an I/O error occurred
     */
    public void join(String playername) throws IOException;

    /**
     * Send player's choice to server
     *
     * @param choice        0 for mouse, 1 for cat, or 2 for elephant
     * @throws IOException  thrown if an I/O error occurred
     */
    public void playerChose(int choice) throws IOException;

    /**
     * Sent when player starts a new round
     *
     * @throws IOException  thrown if an I/O error occurred
     */
    public void newRound() throws IOException;

    /**
     * Sent when a player closes the window
     *
     * @throws IOException thrown if an I/O error occurred
     */
    public void quit() throws IOException;
}