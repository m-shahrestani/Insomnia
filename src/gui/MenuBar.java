package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A class to design and manage MenuBar.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class MenuBar extends JMenuBar {
    //frame
    private JFrame frame;
    //leftPanel
    private LeftPanel leftPanel;
    //frame icon image
    private final ImageIcon icon = new ImageIcon("./images/insomnia-icon.png");
    //applicationMenu
    private JMenu applicationMenu;
    //editMenu
    private JMenu editMenu;
    //viewMenu
    private JMenu viewMenu;
    //windowMenu
    private JMenu windowMenu;
    //toolsMenu
    private JMenu toolsMenu;
    //helpMenu
    private JMenu helpMenu;
    //condition of redirect
    private Checkbox redirectBox;
    //condition system tray
    private Checkbox trayBox;
    //count of tray
    boolean isTray = false;
    //a String for save address
    private static final String OPT_PATH = "./cache/opt.bin";

    /**
     * Create a new MenuBar.
     *
     * @param frame frame.
     */
    public MenuBar(JFrame frame) {
        this.frame = frame;
        applicationMenu = new JMenu("Application");
        Font font = new Font("Times New Roman (Headings CS)", Font.PLAIN, 12);
        applicationMenu.setFont(font);
        editMenu = new JMenu("Edit");
        editMenu.setFont(font);
        viewMenu = new JMenu("View");
        viewMenu.setFont(font);
        windowMenu = new JMenu("Window");
        windowMenu.setFont(font);
        toolsMenu = new JMenu("Tools");
        toolsMenu.setFont(font);
        helpMenu = new JMenu("Help");
        helpMenu.setFont(font);
        makeApplicationMenu();
        makeEditMenu();
        makeViewMenu();
        makeWindowMenu();
        makeToolsMenu();
        makeHelpMenu();
        add(applicationMenu);
        add(editMenu);
        add(viewMenu);
        add(windowMenu);
        add(toolsMenu);
        add(helpMenu);
    }

    /**
     * make Application Menu.
     *
     * The method makes Application Menu for MenuBar.
     */
    private void makeApplicationMenu(){

        JMenuItem option = new JMenuItem("Preferences");
        option.setFont(new Font("Arial", Font.PLAIN, 12));
        option.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));

        Color optionColor = new Color(40,41,37);

        JPanel option1Panel = new JPanel();
        option1Panel.setLayout(new BoxLayout(option1Panel,BoxLayout.X_AXIS));
        option1Panel.setBackground(optionColor);
        option1Panel.setBorder(BorderFactory.createLineBorder(optionColor));
        JLabel redirect = new JLabel("follow redirect");
        redirect.setForeground(Color.white);
        redirect.setHorizontalAlignment(JTextField.CENTER);
        redirectBox = new Checkbox();
        redirectBox.setBackground(optionColor);
        redirectBox.setForeground(optionColor);
        option1Panel.add(redirect, BorderLayout.CENTER);
        option1Panel.add(redirectBox);

        JPanel option2Panel = new JPanel();
        option2Panel.setLayout(new BoxLayout(option2Panel,BoxLayout.X_AXIS));
        option2Panel.setBackground(optionColor);
        option2Panel.setBorder(BorderFactory.createLineBorder(optionColor));
        JLabel systemTray = new JLabel("System Tray ");
        systemTray.setForeground(Color.white);
        systemTray.setHorizontalAlignment(JTextField.CENTER);
        trayBox = new Checkbox();
        trayBox.setBackground(optionColor);
        trayBox.setForeground(optionColor);
        option2Panel.add(systemTray, BorderLayout.CENTER);
        option2Panel.add(trayBox);

        option.addActionListener(e -> {
            JFrame optionFrame = new JFrame();
            JPanel frame = new JPanel(new BorderLayout());
            //Icon
            ImageIcon icon = new ImageIcon("./images/insomnia-icon.png");
            optionFrame.setIconImage(icon.getImage());
            frame.setBackground(optionColor);
            optionFrame.setBackground(optionColor);
            optionFrame.setTitle("Option ");
            optionFrame.setSize(400, 200);
            optionFrame.setLocation(100, 200);
            optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loadOptions();

            frame.add(option1Panel, BorderLayout.NORTH);
            frame.add(option2Panel, BorderLayout.CENTER);
            optionFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    saveOptions();
                }
            });
            optionFrame.add(frame, BorderLayout.CENTER);
            optionFrame.setVisible(true);
        });

        JMenuItem quit = new JMenuItem("Quit");
        quit.setFont(new Font("Arial", Font.PLAIN, 12));
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        quit.addActionListener(e -> {
            if (!trayBox.getState()) {
                System.exit(0);
            }
            else  {
                if (SystemTray.isSupported()) {
                    SystemTray tray = SystemTray.getSystemTray();

                    //ImageIcon
                    ImageIcon trayImageIcon = new ImageIcon("./images/insomnia-icon.png");

                    //popup
                    PopupMenu popup = new PopupMenu();
                    MenuItem exitItem = new MenuItem("Exit");
                    exitItem.addActionListener(e1 -> System.exit(0));
                    MenuItem openItem = new MenuItem("Open");
                    openItem.addActionListener(e12 -> {
                        frame.setVisible(true);
                        frame.setExtendedState(JFrame.NORMAL);
                        isTray = true;
                    });
                    popup.add(openItem);
                    popup.add(exitItem);

                    //trayIcon
                    TrayIcon trayIcon = new TrayIcon(trayImageIcon.getImage(), "Insomnia", popup);
                    trayIcon.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            frame.setVisible(true);
                            frame.setExtendedState(JFrame.NORMAL);
                            isTray = true;
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                    if (!isTray) {
                        trayIcon.setImageAutoSize(true);
                        try {
                            tray.add(trayIcon);
                            isTray = true;
                        } catch (AWTException e2) {
                            System.err.println(e);
                        }
                    }
                    frame.setVisible(false);
                }
            }
        });
        //applicationMenu.add(preferences);
        applicationMenu.add(option);
        applicationMenu.addSeparator();
        applicationMenu.add(quit);
    }

    /**
     * make edit Menu.
     *
     * The method makes edit Menu for MenuBar.
     */
    private void makeEditMenu() {
    }

    /**
     * make view Menu.
     *
     * The method makes view Menu for MenuBar.
     */
    private void makeViewMenu() {
        JMenuItem toggleFull = new JMenuItem("Toggle Full Screen");
        toggleFull.setFont(new Font("Arial", Font.PLAIN, 12));
        toggleFull.addActionListener(e -> {
            if (toggleFull.getText().equals("Toggle Full Screen")) {
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                toggleFull.setText("Toggle Normal Screen");
            }
            else {
                frame.setExtendedState(Frame.NORMAL);
                toggleFull.setText("Toggle Full Screen");
            }
        });
        toggleFull.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));


        JMenuItem toggleSidebar = new JMenuItem("Toggle Sidebar");
        toggleSidebar.setFont(new Font("Arial", Font.PLAIN, 12));
        toggleSidebar.addActionListener(e -> {
            if (toggleSidebar.getText().equals("Toggle Sidebar")) {
                toggleSidebar.setText("Toggle Normal");
                frame.setLocation(0,0);
                frame.setSize(700,1024);
            }
            else {
                toggleSidebar.setText("Toggle Sidebar");
                frame.setSize(1100, 600);
                frame.setLocation(100, 200);
            }
        });
        toggleSidebar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        viewMenu.add(toggleFull);
        viewMenu.add(toggleSidebar);
    }

    /**
     * make window Menu.
     *
     * The method makes window Menu for MenuBar.
     */
    private void makeWindowMenu() {
    }

    /**
     * make tools Menu.
     *
     * The method makes tools Menu for MenuBar.
     */
    private void makeToolsMenu() {

    }

    /**
     * make help Menu.
     *
     * The method makes help Menu for MenuBar.
     */
    private void makeHelpMenu() {
        Color aboutColor = new Color(40,41,37);
        JMenuItem about = new JMenuItem("About");
        about.setFont(new Font("Arial", Font.PLAIN, 12));
        about.addActionListener(e -> {
            JFrame aboutFrame = new JFrame();
            aboutFrame.setIconImage(icon.getImage());
            JPanel frame = new JPanel(new BorderLayout());
            frame.setBackground(aboutColor);
            aboutFrame.setBackground(aboutColor);
            aboutFrame.setTitle("About ");
            aboutFrame.setSize(400, 200);
            aboutFrame.setLocation(100, 200);
            aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel aboutField = new JLabel("programmer: Mohammadreza Shahrestani ©");
            aboutField.setForeground(Color.white);
            aboutField.setHorizontalAlignment(JTextField.CENTER);
            frame.add(aboutField, BorderLayout.CENTER);
            aboutFrame.add(frame, BorderLayout.CENTER);
            aboutFrame.setVisible(true);
        });
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        JMenuItem help = new JMenuItem("Help");
        help.setFont(new Font("Arial", Font.PLAIN, 12));
        help.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File("./help/help.html"));
            } catch (IOException ignored) { }

        });
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(about);
        helpMenu.addSeparator();
        helpMenu.add(help);
    }

    /**
     * get RedirectBox for condition of follow redirect.
     *
     * @return RedirectBox.
     */
    public Checkbox getRedirectBox() {
        return redirectBox;
    }

    /**
     * save options.
     *
     * The method saves options.
     */
    public void saveOptions() {
        try {
            String content = redirectBox.getState() + "\n" + trayBox.getState();
            FileOutputStream fileOutputStream = new FileOutputStream(OPT_PATH);
            byte[] strToByteArray = content.getBytes();
            fileOutputStream.write(strToByteArray);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load options.
     *
     * The method loads options condition.
     */
    public void loadOptions()
    {
        File file = new File(OPT_PATH);
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int i;
            while ((i=fileInputStream.read())!=-1)
            {
                char in = (char)i;
                result.append(in);
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] str = result.toString().split("\n");
        if (str[0].equals("true")) {
            redirectBox.setState(true);
        }
        else {
            redirectBox.setState(false);
        }
        if (str[1].equals("true")) {
            trayBox.setState(true);
        }
        else {
            trayBox.setState(false);
        }
    }
}
