import java.awt.event.*;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class RockPaperScissorsUI provides the user interface for the Rock Paper
 * Scissors network game.
 *
 * @author Nicholas A. Mattis
 * @version 9/26/2015
 */
public class RockPaperScissorsUI implements ModelListener {

// Hidden data members.

    private static final int GAP = 10;
    private static final int COL = 10;

    private JFrame frame;
    private JTextField myName;
    private JTextField myScore;
    private JTextField myChoice;
    private JTextField theirName;
    private JTextField theirScore;
    private JTextField theirChoice;
    private JTextField outcomeField;
    private JButton newRoundButton;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;

    private int myID;
    private int otherChoice;

    private String Objects[] = {"Rock", "Paper", "Scissors"};
    private String Verbs[] = {"ties", "breaks", "covers", "cut"};

    private ViewListener viewListener;

// Hidden constructors.

    /**
     * Construct a new Rock Paper Scissors UI.
     */
    private RockPaperScissorsUI
    (String name) {
        // Set up window.
        frame = new JFrame("Rock Paper Scissors -- " + name);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel.add(panel1);
        myName = new JTextField(COL);
        myName.setEditable(false);
        myName.setHorizontalAlignment(JTextField.CENTER);
        panel1.add(myName);
        myScore = new JTextField(COL);
        myScore.setEditable(false);
        myScore.setHorizontalAlignment(JTextField.CENTER);
        panel1.add(myScore);
        myChoice = new JTextField(COL);
        myChoice.setEditable(false);
        myChoice.setHorizontalAlignment(JTextField.CENTER);
        panel1.add(myChoice);

        panel.add(Box.createVerticalStrut(GAP));

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel.add(panel2);
        theirName = new JTextField(COL);
        theirName.setEditable(false);
        theirName.setHorizontalAlignment(JTextField.CENTER);
        panel2.add(theirName);
        theirScore = new JTextField(COL);
        theirScore.setEditable(false);
        theirScore.setHorizontalAlignment(JTextField.CENTER);
        panel2.add(theirScore);
        theirChoice = new JTextField(COL);
        theirChoice.setEditable(false);
        theirChoice.setHorizontalAlignment(JTextField.CENTER);
        panel2.add(theirChoice);

        panel.add(Box.createVerticalStrut(GAP));

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel.add(panel3);
        outcomeField = new JTextField(COL);
        outcomeField.setEditable(false);
        outcomeField.setHorizontalAlignment(JTextField.CENTER);
        panel3.add(outcomeField);
        newRoundButton = new JButton("New Round");
        panel3.add(newRoundButton);

        newRoundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newRound();
            }
        });

        panel.add(Box.createVerticalStrut(GAP));

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel.add(panel4);
        ClassLoader loader = getClass().getClassLoader();
        rockButton = new JButton(new ImageIcon
                (loader.getResource("rock.png")));
        panel4.add(rockButton);
        paperButton = new JButton(new ImageIcon
                (loader.getResource("paper.png")));
        panel4.add(paperButton);
        scissorsButton = new JButton(new ImageIcon
                (loader.getResource("scissor.png")));
        panel4.add(scissorsButton);

        rockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeChoice(myID, 0);
            }
        });
        rockButton.setEnabled(false);

        paperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeChoice(myID, 1);
            }
        });
        paperButton.setEnabled(false);

        scissorsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeChoice(myID, 2);
            }
        });
        scissorsButton.setEnabled(false);

        // Display window.
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitProgram();
                System.exit(0);
            }
        });
    }

    /**
     * set the ViewListener for RockPaperScissorsUI
     *
     * @param viewListener View Listener
     */
    public synchronized void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    /**
     * Report the user id the server assigns either 0 or 1
     *
     * @param  idnum        player id
     * @throws IOException thrown if I/O error occurred
     */
    public synchronized void getID(int idnum) throws IOException {
        this.myID = idnum;
    }

    /**
     * Reports one of the player's names.
     *
     * @param id            id num of player being reported
     * @param playername    the name of the player being reported
     * @throws IOException  thrown if I/O error occurred
     */
    public synchronized void name(int id, String playername)
            throws IOException {
        onSwingThreadDo(new Runnable() {
            /**
             * run method for name
             */
            public void run() {
                if (id == myID) {
                    myName.setText(playername);
                } else {
                    theirName.setText(playername);
                    rockButton.setEnabled(true);
                    paperButton.setEnabled(true);
                    scissorsButton.setEnabled(true);
                }
            }
        });
    }

    /**
     * Sent to each client to report one of the player's scores
     *
     * @param id            id num of player whose score is being reported
     * @param value         the value of the player's score
     * @throws IOException  thrown if I/O error occurred
     */
    public synchronized void score(int id, int value) throws IOException {
        onSwingThreadDo(new Runnable() {
            /**
             * run method for score
             */
            public void run() {
                if (id == myID) {
                    myScore.setText(Integer.toString(value));
                } else {
                    theirScore.setText(Integer.toString(value));
                }
            }
        });
    }

    /**
     * Reports that one of the clients has selected an object
     *
     * @param id            id num of player who made the choice
     * @param playerchoice  0 for rock, 1 for paper, or 2 for scissors
     * @throws IOException  thrown if I/O error occurred
     */
    public synchronized void choice(int id, int playerchoice)
            throws IOException {
        onSwingThreadDo(new Runnable() {
            /**
             * run method for choice
             */
            public void run() {
                if (id == myID) {
                    myChoice.setText(Objects[playerchoice]);
                    rockButton.setEnabled(false);
                    paperButton.setEnabled(false);
                    scissorsButton.setEnabled(false);
                } else {
                    otherChoice = playerchoice;
                    theirChoice.setText("XXXX");
                }
            }
        });
    }

    /**
     * Reports the outcome of a round.
     *
     * @param object1       int value of object
     * @param verb          0 for ties, 1 for breaks, 2 for cuts, or 3 covers
     * @param object2       int value of object
     * @throws IOException  thrown if I/O error occurred
     */
    public synchronized void outcome(int object1, int verb, int object2)
            throws IOException {
        onSwingThreadDo(new Runnable() {
            /**
             * run method for outcome
             */
            public void run() {
                String a1 = Objects[object1];
                String v = Verbs[verb];
                String a2 = Objects[object2];
                theirChoice.setText(Objects[otherChoice]);
                outcomeField.setText(a1 + " " + v + " " + a2.toLowerCase());
            }
        });
    }

    /**
     * Report that a new round has started
     *
     * @throws IOException  thrown if I/O error occurred
     */
    public synchronized void newRoundStarted() throws IOException {
        onSwingThreadDo(new Runnable() {
            /**
             * run method for new round
             */
            public void run() {
                myChoice.setText("");
                theirChoice.setText("");
                outcomeField.setText("");
                rockButton.setEnabled(true);
                paperButton.setEnabled(true);
                scissorsButton.setEnabled(true);
            }
        });
    }

    /**
     * Reports when game session has been terminated
     *
     * @throws IOException  thrown if I/O error occurred
     */
    public synchronized void quit() throws IOException {
        onSwingThreadDo(new Runnable() {
            /**
             * run method for quit
             */
            public void run() {
                try {
                    viewListener.quit();
                    System.exit(0);
                } catch (IOException e) {
                }
            }
        });
    }

    /**
     * Player chooses object, disables all buttons and sends choice message to
     * server
     *
     * @param buttonChoice  (int) choice that was made by the user
     */
    private synchronized void makeChoice(int id, int buttonChoice) {
        try {
            viewListener.playerChose(id, buttonChoice);
        } catch (IOException e) {
        }
    }

    /**
     * if the user hits the new round button re-enable the choice buttons and
     * send the newRound message
     */
    private synchronized void newRound() {
        try {
            viewListener.newRound();
        } catch (IOException e) {
        }
    }

    /**
     * If the user exits the application by closing it let the server know by
     * sending the quit message
     */
    private synchronized void quitProgram() {
        try {
            viewListener.quit();
        } catch (IOException e) {
        }
    }

// Exported operations.

    /**
     * An object holding a reference to a Rock Paper Scissors UI.
     */
    private static class UIRef {
        public RockPaperScissorsUI ui;
    }

    /**
     * Construct a new Rock Paper Scissors UI.
     */
    public static RockPaperScissorsUI create
    (String name) {
        final UIRef ref = new UIRef();
        onSwingThreadDo(new Runnable() {
            public void run() {
                ref.ui = new RockPaperScissorsUI(name);
            }
        });
        return ref.ui;
    }

    // Hidden operations.

    /**
     * Execute the given runnable object on the Swing thread.
     */
    private static void onSwingThreadDo
    (Runnable task) {
        try {
            SwingUtilities.invokeAndWait(task);
        } catch (Throwable exc) {
            exc.printStackTrace(System.err);
            System.exit(1);
        }
    }

}