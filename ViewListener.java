import java.io.IOException;

/**
 * Interface ViewListener specifies the interface for an object that is
 * triggered by events from the view object in the RockPaperScissors game.
 *
 * @author Nicholas A. Mattis
 * @version 9/26/2015
 */
public interface ViewListener {

    /**
     * Join the game session.
     *
     * @param playername    name of the player starting the client
     * @throws IOException  thrown if an I/O error occurred
     */
    public void join(ViewProxy proxy, String playername) throws IOException;

    /**
     * sends the id of the player and what button they clicked
     *
     * @param id            (int) player id
     * @param choice        (int) which choice they made
     * @throws IOException  thrown if an I/O error occurred
     */
    public void playerChose(int id, int choice) throws IOException;

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