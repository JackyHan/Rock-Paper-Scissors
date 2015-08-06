import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MouseCatElephantModel implements ViewListener {

    private PlayerInfo p1;
    private PlayerInfo p2;

    private class PlayerInfo {

        private int id;
        private int score;
        private int choice;
        private String playername;
        private ModelListener modelL;

        public PlayerInfo (int id, int score, String playername, ModelListener modelL) {
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

        public String getPlayername() {
            return this.playername;
        }

        public ModelListener getModelL() {
            return this.modelL;
        }
    }

    public synchronized void addModelListener(int id, String playername,
                                              ModelListener modelListener) {
        if (p1 == null) {
            p1 = new PlayerInfo(id, 0, playername, modelListener);
            try {
                p1.getModelL().getID(p1.getID());
                p1.getModelL().name(p1.getID(), p1.getPlayername());
                p1.getModelL().score(p1.getID(), p1.getScore());
            } catch (IOException e) {
            }
        } else {
            p2 = new PlayerInfo(id, 0, playername, modelListener);
            try {
                p2.getModelL().getID(p2.getID());
                p2.getModelL().name(p2.getID(), p2.getPlayername());
                p2.getModelL().score(p2.getID(), p2.getScore());
                p1.getModelL().name(p2.getID(), p2.getPlayername());
                p1.getModelL().score(p2.getID(), p2.getScore());
                p2.getModelL().name(p1.getID(), p1.getPlayername());
                p2.getModelL().score(p1.getID(), p1.getScore());
            } catch (IOException e) {
            }
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
        } else {
            p2.setChoice(choice);
        }
        p1.getModelL().choice(id, choice);
        p2.getModelL().choice(id, choice);
    }

    @Override
    public synchronized void newRound() throws IOException {
        
    }

    @Override
    public synchronized void quit() throws IOException {

    }
}