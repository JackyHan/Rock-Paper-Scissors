import java.io.IOException;

/**
 * MouseCatElephantModel class. Runs and stores all information that is needed
 * to play the game.
 *
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MouseCatElephantModel implements ViewListener {

    private int playerCount = 0;
    private PlayerInfo p1;
    private PlayerInfo p2;

    /**
     * PlayerInfo stores the information for each player in the session.
     */
    private class PlayerInfo {

        private int id;
        private int score;
        private int choice = -1;
        private String playername;
        private ModelListener modelL;

        /**
         * Constructor
         *
         * @param id            (int) id of player
         * @param score         (int) score of player
         * @param playername    (String) name
         * @param modelL        ModelListener for player
         */
        public PlayerInfo (int id, int score, String playername,
                           ModelListener modelL) {
            this.id = id;
            this.playername = playername;
            this.score = score;
            this.modelL = modelL;
        }

        /**
         * Set the choice that the player made
         *
         * @param choice    (int) player's choice
         */
        public void setChoice(int choice) {
            this.choice = choice;
        }

        /**
         * Get the players choice
         *
         * @return  integer value of player's choice
         */
        public int getChoice() {
            return this.choice;
        }

        /**
         * Get the players id number
         *
         * @return      (int) id number
         */
        public int getID() {
            return this.id;
        }

        /**
         * Get the players current score.
         *
         * @return      (int) score
         */
        public int getScore() {
            return this.score;
        }

        /**
         * increment the players score by 1
         */
        public void addScore() {
            this.score++;
        }

        /**
         * get the players name
         *
         * @return      (String) players name
         */
        public String getPlayername() {
            return this.playername;
        }

        /**
         * Get the players ModelListener
         *
         * @return      ModelListener object
         */
        public ModelListener getModelL() {
            return this.modelL;
        }
    }

    /**
     * Get the current number of players in the session.
     *
     * @return      (int) total players in session
     */
    public synchronized int getPlayerCount() {
        return playerCount;
    }

    /**
     * Create a new PlayerInfo object when someone joins and send the messages
     * to the client to correctly display the information on the gui.
     *
     * @param id                player's id
     * @param playername        player's name
     * @param modelListener     player's modelListener
     * @throws IOException      thrown if an I/O occurs
     */
    public synchronized void addModelListener(int id, String playername,
                                              ModelListener modelListener)
            throws IOException {
        if (p1 == null) {
            p1 = new PlayerInfo(id, 0, playername, modelListener);
            p1.getModelL().getID(p1.getID());
            p1.getModelL().name(p1.getID(), p1.getPlayername());
            p1.getModelL().score(p1.getID(), p1.getScore());
            playerCount++;
        } else {
            p2 = new PlayerInfo(id, 0, playername, modelListener);
            p2.getModelL().getID(p2.getID());
            p2.getModelL().name(p2.getID(), p2.getPlayername());
            p2.getModelL().score(p2.getID(), p2.getScore());
            p1.getModelL().name(p2.getID(), p2.getPlayername());
            p1.getModelL().score(p2.getID(), p2.getScore());
            p2.getModelL().name(p1.getID(), p1.getPlayername());
            p2.getModelL().score(p1.getID(), p1.getScore());
        }
    }

    /**
     * Join the game session.
     *
     * @param playername    name of the player starting the client
     * @param proxy         viewproxy object for player
     * @throws IOException  thrown if an I/O error occurred
     */
    public void join(ViewProxy proxy, String playername) throws IOException {
    }

    /**
     * sends the id of the player and what button they clicked
     *
     * @param id            (int) player id
     * @param choice        (int) which choice they made
     * @throws IOException  thrown if an I/O error occurred
     */
    public synchronized void playerChose(int id, int choice)
            throws IOException {
        if (p1.getID() == id) {
            p1.setChoice(choice);
            p1.getModelL().choice(id, choice);
            p2.getModelL().choice(id, choice);
        } else {
            p2.setChoice(choice);
            p1.getModelL().choice(id, choice);
            p2.getModelL().choice(id, choice);
        }
        if (p1.getChoice() != -1 && p2.getChoice() != -1) {
            if (p1.getChoice() == 0) {
                if (p2.getChoice() == 0) {
                    p1.getModelL().outcome(p1.getChoice(), 0, p2.getChoice());
                    p2.getModelL().outcome(p1.getChoice(), 0, p2.getChoice());
                } else if (p2.getChoice() == 1) {
                    p1.getModelL().outcome(p2.getChoice(), 2, p1.getChoice());
                    p2.getModelL().outcome(p2.getChoice(), 2, p1.getChoice());
                    p2.addScore();
                    p1.getModelL().score(p2.getID(), p2.getScore());
                    p2.getModelL().score(p2.getID(), p2.getScore());
                } else if (p2.getChoice() == 2) {
                    p1.getModelL().outcome(p1.getChoice(), 1, p2.getChoice());
                    p2.getModelL().outcome(p1.getChoice(), 1, p2.getChoice());
                    p1.addScore();
                    p1.getModelL().score(p1.getID(), p1.getScore());
                    p2.getModelL().score(p1.getID(), p1.getScore());
                }
            } else if (p1.getChoice() == 1) {
                if (p2.getChoice() == 0) {
                    p1.getModelL().outcome(p1.getChoice(), 2, p2.getChoice());
                    p2.getModelL().outcome(p1.getChoice(), 2, p2.getChoice());
                    p1.addScore();
                    p1.getModelL().score(p1.getID(), p1.getScore());
                    p2.getModelL().score(p1.getID(), p1.getScore());
                } else if (p2.getChoice() == 1) {
                    p1.getModelL().outcome(p2.getChoice(), 0, p1.getChoice());
                    p2.getModelL().outcome(p2.getChoice(), 0, p1.getChoice());
                } else if (p2.getChoice() == 2) {
                    p1.getModelL().outcome(p2.getChoice(), 3, p1.getChoice());
                    p2.getModelL().outcome(p2.getChoice(), 3, p1.getChoice());
                    p2.addScore();
                    p1.getModelL().score(p2.getID(), p2.getScore());
                    p2.getModelL().score(p2.getID(), p2.getScore());
                }
            } else if (p1.getChoice() == 2) {
                if (p2.getChoice() == 0) {
                    p1.getModelL().outcome(p2.getChoice(), 1, p1.getChoice());
                    p2.getModelL().outcome(p2.getChoice(), 1, p1.getChoice());
                    p2.addScore();
                    p1.getModelL().score(p2.getID(), p2.getScore());
                    p2.getModelL().score(p2.getID(), p2.getScore());
                } else if (p2.getChoice() == 1) {
                    p1.getModelL().outcome(p1.getChoice(), 3, p2.getChoice());
                    p2.getModelL().outcome(p1.getChoice(), 3, p2.getChoice());
                    p1.addScore();
                    p1.getModelL().score(p1.getID(), p1.getScore());
                    p2.getModelL().score(p1.getID(), p1.getScore());
                } else if (p2.getChoice() == 2) {
                    p1.getModelL().outcome(p2.getChoice(), 0, p1.getChoice());
                    p2.getModelL().outcome(p2.getChoice(), 0, p1.getChoice());
                }
            }
        }
    }

    /**
     * Sent when player starts a new round
     *
     * @throws IOException  thrown if an I/O error occurred
     */
    public synchronized void newRound() throws IOException {
        p1.setChoice(-1);
        p2.setChoice(-1);
        p1.getModelL().newRoundStarted();
        p2.getModelL().newRoundStarted();
    }

    /**
     * Sent when a player closes the window
     *
     * @throws IOException thrown if an I/O error occurred
     */
    public synchronized void quit() throws IOException {
        if (p1 != null) {
            p1.getModelL().quit();
        }
        if (p2 != null) {
            p2.getModelL().quit();
        }
        playerCount--;
    }
}