import java.io.IOException;

/**
 * Interface ModelListener specifies the interface for an object that is
 * triggered by events from the model object in the MouseCatElephant game.
 *
 * @author Nicholas A. Mattis
 * @version 7/22/2015
 */
public interface ModelListener {

    /**
     * Report the user id the server assigns either 0 or 1
     *
     * @throws IOException thrown if I/O error occurred
     */
    public void getID(int idnum) throws IOException;

    /**
     * Reports one of the player's names.
     *
     * @param id            id num of player being reported
     * @param playername    the name of the player being reported
     * @throws IOException  thrown if I/O error occurred
     */
    public void name(int id, String playername) throws IOException;

    /**
     * Sent to each client to report one of the player's scores
     *
     * @param id            id num of player whose score is being reported
     * @param value         the value of the player's score
     * @throws IOException  thrown if I/O error occurred
     */
    public void score(int id, int value) throws IOException;

    /**
     * Reports that one of the clients has selected an animal
     *
     * @param id            id num of player who made the choice
     * @param playerchoice  0 for mouse, 1 for cat, or 2 for elephant
     * @throws IOException  thrown if I/O error occurred
     */
    public void choice(int id, int playerchoice) throws IOException;

    /**
     * Reports the outcome of a round.
     *
     * @param animal1       int value of animal
     * @param verb          0 for ties, 1 for frightens, 2 for eats, or 3 for
     *                      stomps
     * @param animal2       int value of animal
     * @throws IOException  thrown if I/O error occurred
     */
    public void outcome(int animal1, int verb, int animal2) throws IOException;

    /**
     * Report that a new round has started
     *
     * @throws IOException  thrown if I/O error occurred
     */
    public void newRoundStarted() throws IOException;

    /**
     * Reports when game session has been terminated
     *
     * @throws IOException  thrown if I/O error occurred
     */
    public void quit() throws IOException;
}