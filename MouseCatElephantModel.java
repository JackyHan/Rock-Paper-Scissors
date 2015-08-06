import java.io.IOException;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MouseCatElephantModel implements ViewListener {

    private int playerCount = 0;
    private PlayerInfo p1;
    private PlayerInfo p2;

    private class PlayerInfo {

        private int id;
        private int score;
        private int choice = -1;
        private String playername;
        private ModelListener modelL;

        public PlayerInfo (int id, int score, String playername,
                           ModelListener modelL) {
            this.id = id;
            this.playername = playername;
            this.score = score;
            this.modelL = modelL;
        }

        public void setChoice(int choice) {
            this.choice = choice;
        }

        public int getChoice() {
            return this.choice;
        }

        public int getID() {
            return this.id;
        }

        public int getScore() {
            return this.score;
        }

        public void addScore() { this.score++; }

        public String getPlayername() {
            return this.playername;
        }

        public ModelListener getModelL() {
            return this.modelL;
        }
    }

    public synchronized int getPlayerCount() {
        return playerCount;
    }

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

    @Override
    public void join(ViewProxy proxy, String playername) throws IOException {
    }

    @Override
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

    @Override
    public synchronized void newRound() throws IOException {

    }

    @Override
    public synchronized void quit() throws IOException {
        if (p1 == null) {
            p2.getModelL().quit();
        } else {
            p1.getModelL().quit();
            playerCount--;
        }
    }
}