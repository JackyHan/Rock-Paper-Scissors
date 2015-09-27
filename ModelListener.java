import java.io.IOException;

/**
 * Interface ModelListener specifies the interface for an object that is
 * triggered by events from the model object in the RockPaperScissors game.
 *
 * @author Nicholas A. Mattis
 * @version 9/26/2015
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
     * Reports that one of the clients has selected an objects
     *
     * @param id            id num of player who made the choice
     * @param playerchoice  0 for rock, 1 for paper, or 2 for scissors
     * @throws IOException  thrown if I/O error occurred
     */
    public void choice(int id, int playerchoice) throws IOException;

    /**
     * Reports the outcome of a round.
     *
     * @param object1       int value of object
     * @param verb          0 for ties, 1 for breaks, 2 for cuts, or 3 for
     *                      covers
     * @param object2       int value of object
     * @throws IOException  thrown if I/O error occurred
     */
    public void outcome(int object1, int verb, int object2) throws IOException;

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