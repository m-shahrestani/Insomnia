package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * A class to design and manage insomnia GUI.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class InsomniaGUI {
    //frame
    private JFrame frame;
    //menuBar
    private MenuBar menuBar;
    //leftPanel
    private LeftPanel leftPanel;
    //middlePanel
    private MiddlePanel middlePanel;
    // rightPanel
    private RightPanel rightPanel;
    //leftSplit
    private JSplitPane leftSplit;
    //rightSplit
    private JSplitPane rightSplit;

    /**
     * Create a new InsomniaGUI.
     *
     */
    public InsomniaGUI() {
        frame = new JFrame();
        menuBar = new MenuBar(frame);
        rightPanel = new RightPanel();
        middlePanel = new MiddlePanel(rightPanel, menuBar, frame);
        leftPanel = new LeftPanel(frame, middlePanel);
        //look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        //frame
        frame.setTitle("Insomnia");
        frame.setSize(1100, 600);
        frame.setLocation(100, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(40, 41, 37));

        //Icon
        ImageIcon icon = new ImageIcon("./images/insomnia-icon.png");
        frame.setIconImage(icon.getImage());
        //menubar
        frame.setJMenuBar(menuBar);
        //split
        leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        leftSplit.setResizeWeight(0.1);
        leftSplit.setDividerSize(1);
        leftSplit.setBorder(BorderFactory.createLineBorder(new Color(71, 72, 69), 1));
        leftSplit.setContinuousLayout(true);
        rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, rightPanel);
        rightSplit.setResizeWeight(0.5);
        rightSplit.setDividerSize(1);
        rightSplit.setBorder(BorderFactory.createLineBorder(new Color(71, 72, 69), 1));
        rightSplit.setContinuousLayout(true);
        frame.add(rightSplit);
        frame.setVisible(true);
    }
}