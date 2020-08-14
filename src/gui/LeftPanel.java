package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * A class to design and manage LeftPanel.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class LeftPanel extends JPanel {
    //frame
    private JFrame frame;
    //middlePanel
    private MiddlePanel middlePanel;
    //a String for save address
    private static final String MyReq_PATH = "./cache/MyReq.bin";
    //Color of left panel
    private Color leftColor;
    //Color of mouse button
    private Color mouseButtonColor;
    //Color of text
    private Color textColor;
    //first panel
    private JPanel firstPanel;
    //insomniaField
    private JTextField insomniaField;
    //secondPanel
    private JPanel secondPanel;
    //environment field
    private JTextField environment;
    //cookies field
    private JTextField cookies;
    //thirdPanel
    private JPanel thirdPanel;
    //filter field
    private JTextField filter;
    //newRequestFiled field
    private JTextField newRequestFiled;
    //requestScrollPane
    private JScrollPane requestScrollPane;
    //requestPanel
    private JPanel requestPanel;
    //requestsScrollPane
    private JScrollPane requestsScrollPane;
    //requestsPanel
    private JPanel requestsPanel;
    //requests collection
    private ArrayList<JPanel> requests;
    //myRequests
    private ArrayList<MyRequest> myRequests;

    /**
     * Create a new LeftPanel.
     *
     * @param frame frame
     * @param middlePanel middlePanel
     */
    public LeftPanel(JFrame frame, MiddlePanel middlePanel) {
        this.frame = frame;
        this.middlePanel = middlePanel;
        myRequests = new ArrayList<>();
        leftColor = new Color(46,47,43);
        mouseButtonColor = new Color(54, 55, 52);
        textColor = new Color(255,255,255);
        setLayout(new BorderLayout());
        setBackground(leftColor);
        setBorder(BorderFactory.createLineBorder(leftColor));

        requests = new ArrayList<>();
        requestPanel = new JPanel();
        requestScrollPane = new JScrollPane(requestPanel);
        requestScrollPane.setBackground(leftColor);
        requestScrollPane.setBorder(BorderFactory.createLineBorder(leftColor));
        requestsPanel = new JPanel();
        requestsScrollPane = new JScrollPane(requestsPanel);
        requestsScrollPane.setBackground(leftColor);
        requestsScrollPane.setBorder(BorderFactory.createLineBorder(leftColor));
        requestsPanel.setLayout(new GridLayout(13, 1));
        requestsPanel.setBackground(leftColor);


        firstPanel = new JPanel();
        firstPanel.setLayout(new BoxLayout(firstPanel,BoxLayout.Y_AXIS));
        firstPanel.setPreferredSize(new Dimension(250, 100));
        firstPanel.setBackground(leftColor);
        firstPanel.setBorder(BorderFactory.createLineBorder(leftColor));
        insomniaField = new JTextField();
        makeInsomniaField();
        firstPanel.add(insomniaField);


        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        secondPanel.setBackground(new Color(54, 55, 52));
        secondPanel.setBorder(BorderFactory.createLineBorder(new Color(46, 47, 43)));
        environment = new JTextField();
        makeEnvironment();
        cookies = new JTextField();
        makeCookies();
        secondPanel.add(environment, BorderLayout.WEST);
        secondPanel.add(cookies, BorderLayout.CENTER);
        firstPanel.add(secondPanel);


        thirdPanel = new JPanel();
        thirdPanel.setLayout(new BorderLayout());
        thirdPanel.setBackground(new Color(54, 55, 52));
        thirdPanel.setBorder(BorderFactory.createLineBorder(new Color(46, 47, 43)));

        filter = new JTextField();
        makeFilter();

        newRequestFiled = new JTextField();
        makeNewRequestField();

        thirdPanel.add(filter, BorderLayout.CENTER);
        thirdPanel.add(newRequestFiled, BorderLayout.EAST);
        firstPanel.add(thirdPanel);
        add(firstPanel,BorderLayout.NORTH);

        makeRequestPanel();

        add(requestScrollPane, BorderLayout.CENTER);
        loadMyRequest();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveMyRequest();
            }
        });
    }

    /**
     * make Insomnia Field.
     *
     * The method makes insomnia textfield for LeftPanel.
     */
    private void makeInsomniaField() {
        insomniaField.setText("   Insomnia                           ▼");
        insomniaField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        insomniaField.setForeground(textColor);
        insomniaField.setBackground(new Color(105,94,184));
        insomniaField.setBorder(BorderFactory.createLineBorder(new Color(105,94,184)));
        insomniaField.setEditable(false);
        insomniaField.setPreferredSize(new Dimension(50, 45));
    }

    /**
     * make environment Field.
     *
     * The method makes environment textfield for LeftPanel.
     */
    private void makeEnvironment() {
        environment.setBackground(new Color(54, 55, 52));
        environment.setForeground(textColor);
        environment.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        environment.setText("  No Environment  ▼");
        environment.setEditable(false);
        environment.setPreferredSize(new Dimension(130, 30));
        environment.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                environment.setBackground(mouseButtonColor);
                environment.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                environment.setBackground(leftColor);
                environment.setBorder(BorderFactory.createLineBorder(leftColor));
            }
        });
    }

    /**
     * make cookies Field.
     *
     * The method makes cookies textfield for LeftPanel.
     */
    private void makeCookies() {
        cookies.setBackground(new Color(54, 55, 52));
        cookies.setForeground(textColor);
        cookies.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        cookies.setText("Cookies");
        cookies.setHorizontalAlignment(JTextField.CENTER);
        cookies.setEditable(false);
        cookies.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cookies.setBackground(mouseButtonColor);
                cookies.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cookies.setBackground(leftColor);
                cookies.setBorder(BorderFactory.createLineBorder(leftColor));
            }
        });
    }

    /**
     * make filter Field.
     *
     * The method makes filter textfield for LeftPanel.
     */
    private void makeFilter(){
        filter.setBackground(new Color(46, 47, 43));
        filter.setEditable(true);
        filter.setForeground(textColor);
        filter.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        filter.setText("  Filter");
        filter.setPreferredSize(new Dimension(260, 30));
    }

    /**
     * make newRequest Field.
     *
     * The method makes newRequest textfield for LeftPanel.
     */
    private void makeNewRequestField() {
        //⊕
        newRequestFiled.setText("  +  ");
        newRequestFiled.setEditable(false);
        newRequestFiled.setFont(new Font( "Arial", Font.PLAIN, 20));
        newRequestFiled.setBackground(new Color(46, 47, 43));
        newRequestFiled.setForeground(textColor);
        newRequestFiled.setBorder(BorderFactory.createLineBorder(new Color(46, 47, 43)));
        newRequestFiled.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String nameOfRequest = JOptionPane.showInputDialog(frame,"Enter name of new Request.");
                myRequests.add(new MyRequest(nameOfRequest));
                addRequest(nameOfRequest);
                drawRequests();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                newRequestFiled.setBackground(mouseButtonColor);
                newRequestFiled.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                drawRequests();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newRequestFiled.setBackground(leftColor);
                newRequestFiled.setBorder(BorderFactory.createLineBorder(leftColor));
                drawRequests();
            }
        });
    }

    /**
     * make Request Panel.
     * <p>
     * The method makes Request Panel for InsomniaGUI.LeftPanel.
     */
    private void makeRequestPanel() {
        requestPanel.setLayout(new BorderLayout());
        requestPanel.setBackground(leftColor);
        requestPanel.setBorder(BorderFactory.createLineBorder(leftColor));
        drawRequests();
    }

    /**
     * add request.
     *
     * The method adds newRequest to requests.
     */
    private void addRequest(String name) {
        JPanel newRequest = new JPanel();
        newRequest.setLayout(new BorderLayout());
        newRequest.setBackground(new Color(54, 55, 52));
        newRequest.setBorder(BorderFactory.createLineBorder(new Color(46, 47, 43)));
        JTextField requestField = new JTextField();
        JTextField typeField = new JTextField();

        String type = "GET   ";
        Color typeColor = new Color(125,105,203);

        typeField.setText( "  " + type);
        typeField.setFont(new Font( "Arial", Font.PLAIN, 9));
        typeField.setEditable(false);
        typeField.setBackground(leftColor);
        typeField.setForeground(typeColor);
        typeField.setBorder(BorderFactory.createLineBorder(leftColor));
        typeField.setPreferredSize(new Dimension(40, 30));

        requestField.setText(name);
        requestField.setFont(new Font( "Arial", Font.PLAIN, 14));
        requestField.setEditable(false);
        requestField.setBackground(leftColor);
        requestField.setForeground(new Color(255,255,255));
        requestField.setBorder(BorderFactory.createLineBorder(leftColor));

        requestField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(MyRequest temp: myRequests) {
                    if (temp.isClicked()) {
                        temp.backUpData(middlePanel);
                        Component[] component = requests.get(myRequests.indexOf(temp)).getComponents();
                        JTextField reqField = (JTextField) component[1];
                        JTextField typField = (JTextField) component[0];
                        typField.setText(getType(temp.getMethod()));
                        typField.setForeground(getColor(temp.getMethod()));
                        reqField.setBackground(leftColor);
                        reqField.setBorder(BorderFactory.createLineBorder(leftColor));
                        reqField.setText(temp.getName());
                        typField.setBackground(leftColor);
                        typField.setBorder(BorderFactory.createLineBorder(leftColor));
                        temp.setUnClicked();
                        drawRequests();
                    }
                }
                myRequests.get(requests.indexOf(newRequest)).setClicked();
                myRequests.get(requests.indexOf(newRequest)).fillData(middlePanel);
                requestField.setBackground(mouseButtonColor);
                requestField.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                typeField.setBackground(mouseButtonColor);
                typeField.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                drawRequests();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestField.setBackground(mouseButtonColor);
                requestField.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                requestField.setText(myRequests.get(requests.indexOf(newRequest)).getName() + "                    ▼");
                typeField.setBackground(mouseButtonColor);
                typeField.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                drawRequests();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!myRequests.get(requests.indexOf(newRequest)).isClicked()) {
                    requestField.setBackground(leftColor);
                    requestField.setBorder(BorderFactory.createLineBorder(leftColor));
                    requestField.setText(myRequests.get(requests.indexOf(newRequest)).getName());
                    typeField.setBackground(leftColor);
                    typeField.setBorder(BorderFactory.createLineBorder(leftColor));
                }
                drawRequests();
            }
        });

        typeField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestField.setBackground(mouseButtonColor);
                requestField.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                requestField.setText(myRequests.get(requests.indexOf(newRequest)).getName() + "                    ▼");
                typeField.setBackground(mouseButtonColor);
                typeField.setBorder(BorderFactory.createLineBorder(mouseButtonColor));
                drawRequests();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                requestField.setBackground(leftColor);
                requestField.setBorder(BorderFactory.createLineBorder(leftColor));
                requestField.setText(myRequests.get(requests.indexOf(newRequest)).getName());
                typeField.setBackground(leftColor);
                typeField.setBorder(BorderFactory.createLineBorder(leftColor));
                drawRequests();
            }
        });

        newRequest.add(typeField,BorderLayout.WEST);
        newRequest.add(requestField,BorderLayout.CENTER);

        //popup
        JPopupMenu popup = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.setIcon(new ImageIcon("./images/recycle.png"));
        removeItem.addActionListener(e -> {
            myRequests.remove(requests.indexOf(newRequest));
            requests.remove(newRequest);
            requestsPanel.remove(newRequest);
            drawRequests();
        });
        JMenuItem renameItem = new JMenuItem("Rename");
        renameItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(frame,"Enter new name for this request.");
            myRequests.get(requests.indexOf(newRequest)).setName(newName);
            drawRequests();
        });
        popup.add(removeItem);
        popup.addSeparator();
        popup.add(renameItem);
        requestField.setComponentPopupMenu(popup);

        requests.add(newRequest);
    }

    /**
     * draw Requests Panel.
     * <p>
     * The method makes Requests Panel for InsomniaGUI.LeftPanel.
     */
    private void drawRequests() {
        for (JPanel temp : requests) {
            requestsPanel.add(temp, BorderLayout.CENTER);
        }
        requestPanel.add(requestsScrollPane);
        updateUI();
    }

    /**
     * get Color of Method.
     *
     * @return Color. color of each method
     */
    private Color getColor(String method) {
        switch (method) {
            case "POST":
                return new Color(89,162,16);
            case "PUT":
                return new Color(206,129,33);
            case "DELETE":
                return new Color(208,68,68);
            case "PATCH":
                return new Color(240,225,55);
            default:
                return new Color(125,105,203);
        }
    }

    /**
     * get type of Method.
     *
     * @return type. type of each method.
     */
    private String getType(String method) {
        switch (method) {
            case "POST":
                return "  POST  ";
            case "PUT":
                return "  PUT   ";
            case "DELETE":
                return "  DEL   ";
            case "PATCH":
                return "  PTCH  ";
            default:
                return "  GET   ";
        }
    }

    /**
     * load MyRequest.
     *
     * The method loads MyRequest.
     */
    private void loadMyRequest(){
        try (FileInputStream fs = new FileInputStream(MyReq_PATH)){
            ObjectInputStream os = new ObjectInputStream(fs);
            myRequests = (ArrayList<MyRequest>)os.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("cannot read");
            e.printStackTrace();
        }
        for (MyRequest temp: myRequests) {
            addRequest(temp.getName());
            Component[] component = requests.get(myRequests.indexOf(temp)).getComponents();
            JTextField typField = (JTextField) component[0];
            typField.setText(getType(temp.getMethod()));
            typField.setForeground(getColor(temp.getMethod()));
            if (temp.isClicked()) {
                temp.fillData(middlePanel);
            }
        }
        drawRequests();
    }

    /**
     * save MyRequest.
     *
     * The method saves MyRequest.
     */
    private void saveMyRequest(){
        try (FileOutputStream fs = new FileOutputStream(MyReq_PATH)){
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(myRequests);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}