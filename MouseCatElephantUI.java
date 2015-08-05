//******************************************************************************
//
// File:    MouseCatElephantUI.java
// Package: ---
// Unit:    Class MouseCatElephantUI.java
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 3 of the License, or (at your option) any
// later version.
//
// This Java source file is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// You may obtain a copy of the GNU General Public License on the World Wide Web
// at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

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
 * Class MouseCatElephantUI provides the user interface for the Mouse Cat
 * Elephant network game.
 *
 * @author Alan Kaminsky
 * @author Nicholas A. Mattis
 * @version 8/5/2015
 */
public class MouseCatElephantUI implements ModelListener {

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
    private JButton mouseButton;
    private JButton catButton;
    private JButton elephantButton;

    private int myID;
    private int otherChoice;

    private String Animals[] = {"Mouse", "Cat", "Elephant"};
    private String Verbs[] = {"ties", "frightens", "eats", "stomps"};

    private ViewListener viewListener;

// Hidden constructors.

    /**
     * Construct a new Mouse Cat Elephant UI.
     */
    private MouseCatElephantUI
    (String name) {
        // Set up window.
        frame = new JFrame("Mouse Cat Elephant -- " + name);
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
            @Override
            public void actionPerformed(ActionEvent e) {
                newRound();
            }
        });

        panel.add(Box.createVerticalStrut(GAP));

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel.add(panel4);
        ClassLoader loader = getClass().getClassLoader();
        mouseButton = new JButton(new ImageIcon
                (loader.getResource("mouse.png")));
        panel4.add(mouseButton);
        catButton = new JButton(new ImageIcon
                (loader.getResource("cat.png")));
        panel4.add(catButton);
        elephantButton = new JButton(new ImageIcon
                (loader.getResource("elephant.png")));
        panel4.add(elephantButton);

        mouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeChoice(myID, 0);
            }
        });
        mouseButton.setEnabled(false);

        catButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeChoice(myID, 1);
            }
        });
        catButton.setEnabled(false);

        elephantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeChoice(myID, 2);
            }
        });
        elephantButton.setEnabled(false);

        // Display window.
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitProgram();
                System.exit(0);
            }
        });
    }

    /**
     * set the ViewListener for MouseCatElephantUI
     *
     * @param viewListener View Listener
     */
    public synchronized void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public synchronized void getID(int idnum) throws IOException {
        this.myID = idnum;
    }

    @Override
    public synchronized void name(int id, String playername)
            throws IOException {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                if (id == myID) {
                    myName.setText(playername);
                } else {
                    theirName.setText(playername);
                    mouseButton.setEnabled(true);
                    catButton.setEnabled(true);
                    elephantButton.setEnabled(true);
                }
            }
        });
    }

    @Override
    public synchronized void score(int id, int value) throws IOException {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                if (id == myID) {
                    myScore.setText(Integer.toString(value));
                } else {
                    theirScore.setText(Integer.toString(value));
                }
            }
        });
    }

    @Override
    public synchronized void choice(int id, int playerchoice)
            throws IOException {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                if (id == myID) {
                    myChoice.setText(Animals[playerchoice]);
                    mouseButton.setEnabled(false);
                    catButton.setEnabled(false);
                    elephantButton.setEnabled(false);
                } else {
                    otherChoice = playerchoice;
                    theirChoice.setText("XXXX");
                }
            }
        });
    }

    @Override
    public synchronized void outcome(int animal1, int verb, int animal2)
            throws IOException {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                String a1 = Animals[animal1];
                String v = Verbs[verb];
                String a2 = Animals[animal2];
                theirChoice.setText(Animals[otherChoice]);
                outcomeField.setText(a1 + " " + v + " " + a2.toLowerCase());
            }
        });
    }

    @Override
    public synchronized void newRoundStarted() throws IOException {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                myChoice.setText("");
                theirChoice.setText("");
                outcomeField.setText("");
                mouseButton.setEnabled(true);
                catButton.setEnabled(true);
                elephantButton.setEnabled(true);
            }
        });
    }

    @Override
    public synchronized void quit() throws IOException {
        onSwingThreadDo(new Runnable() {
            @Override
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
     * Player chooses animal, disables all buttons and sends choice message to
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
     * An object holding a reference to a Mouse Cat Elephant UI.
     */
    private static class UIRef {
        public MouseCatElephantUI ui;
    }

    /**
     * Construct a new Mouse Cat Elephant UI.
     */
    public static MouseCatElephantUI create
    (String name) {
        final UIRef ref = new UIRef();
        onSwingThreadDo(new Runnable() {
            public void run() {
                ref.ui = new MouseCatElephantUI(name);
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