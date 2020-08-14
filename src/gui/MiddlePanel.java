package gui;

import Http.Command;
import Http.Request;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A class to design and manage InsomniaGUI.MiddlePanel.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class MiddlePanel extends JPanel {
    //frame
    private JFrame frame;
    //RightPanel
    private RightPanel rightPanel;
    //menuBar
    private MenuBar menuBar;
    //Color of middle panel
    private Color middleColor;
    //Color of text
    private Color textColor;
    //Color of mouse button
    private Color mouseButtonColor;
    //firstPanel
    private JPanel firstPanel;
    //url textField
    private JTextField url;
    //send textField
    private JTextField send;
    //typeComboBox
    private JComboBox typeComboBox;
    //tabbedPane
    private JTabbedPane tabbedPane;

    //bodyScroll
    private JScrollPane bodyScroll;
    //bodyTabs
    private JTabbedPane bodyTabs;
    //body panel
    private JPanel body;

    //headerScroll
    private JScrollPane headerScroll;
    //header panel
    private JPanel header;
    //headersScrollPane
    private JScrollPane headersScrollPane;
    //headersPanel
    private JPanel headersPanel;
    //headers
    private ArrayList<JPanel> headers;

    //formDataScroll
    private JScrollPane formDataScroll;
    //formData
    private JPanel formData;
    //formDataScrollPane
    private JScrollPane formDatasScrollPane;
    //formDataPanel
    private JPanel formDatasPanel;
    //formData's
    private ArrayList<JPanel> formDatas;

    //jsonScroll
    private JScrollPane jsonScroll;
    //binaryScroll
    private JScrollPane binaryScroll;
    //json
    private JPanel json;
    //jsonText
    private JTextArea jsonText;

    //binary
    private JPanel binary;
    //binaryFiled
    private JTextField binaryField;
    //fileChooser
    private JFileChooser fileChooser =new JFileChooser();
    //file
    private File file;
    //filePath
    private String filePath;

    /**
     * Create a new InsomniaGUI.MiddlePanel.
     *
     * @param rightPanel rightPanel
     * @param menuBar menuBar
     * @param frame frame.
     */
    public MiddlePanel(RightPanel rightPanel, MenuBar menuBar, JFrame frame) {
        this.frame = frame;
        this.rightPanel = rightPanel;
        this.menuBar = menuBar;
        middleColor = new Color(40, 41, 37);
        textColor = new Color(255, 255, 255);
        mouseButtonColor = new Color(54, 55, 52);
        setLayout(new BorderLayout());
        setBackground(middleColor);
        setBorder(BorderFactory.createLineBorder(middleColor));

        firstPanel = new JPanel();
        typeComboBox = new JComboBox(new String[]{"GET", "POST", "PUT", "DELETE", "PATCH"});
        typeComboBox.setBackground(Color.WHITE);
        typeComboBox.setForeground(middleColor);
        typeComboBox.setBorder(BorderFactory.createLineBorder(Color.white));
        typeComboBox.setPreferredSize(new Dimension(75, getHeight()));


        url = new JTextField();
        send = new JTextField();
        makeFirstPanel();

        headers = new ArrayList<>();
        headersPanel = new JPanel();
        headersScrollPane = new JScrollPane(headersPanel);
        headersScrollPane.setBackground(middleColor);
        headersScrollPane.setBorder(BorderFactory.createLineBorder(middleColor));
        headersPanel.setLayout(new GridLayout(15, 1));
        headersPanel.setBackground(middleColor);

        body = new JPanel();
        header = new JPanel();

        tabbedPane = new JTabbedPane();
        bodyScroll = new JScrollPane(body);
        headerScroll = new JScrollPane(header);


        makeBody();
        makeHeader();
        makeTabs();


        formDatas = new ArrayList<>();
        formDatasPanel = new JPanel();
        formDatasScrollPane = new JScrollPane(formDatasPanel);
        formDatasScrollPane.setBackground(middleColor);
        formDatasScrollPane.setBorder(BorderFactory.createLineBorder(middleColor));
        formDatasPanel.setLayout(new GridLayout(15, 1));
        formDatasPanel.setBackground(middleColor);

        jsonText = new JTextArea();
        bodyTabs = new JTabbedPane();
        formData = new JPanel();
        json = new JPanel();
        binary = new JPanel();
        formDataScroll = new JScrollPane(formData);
        jsonScroll = new JScrollPane(json);
        binaryScroll = new JScrollPane(binary);
        binaryField = new JTextField();
        makeFormData();
        makeJson();
        makeBinary();
        makeBodyTabs();
        body.add(bodyTabs);

        add(firstPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * make first Panel.
     * <p>
     * The method makes first Panel for InsomniaGUI.MiddlePanel.
     */
    private void makeFirstPanel() {
        Color mouseColor = new Color(242, 242, 242);
        firstPanel.setLayout(new BorderLayout());
        firstPanel.setBackground(middleColor);
        firstPanel.setBorder(BorderFactory.createLineBorder(middleColor));

        typeComboBox.setEditable(false);
        typeComboBox.setBackground(Color.WHITE);
        typeComboBox.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        typeComboBox.addMouseListener(new MouseListener() {
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
                typeComboBox.setBackground(mouseColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                typeComboBox.setBackground(Color.white);
            }
        });

        url.setText("https://www.google.com");
        url.setEditable(true);
        url.setBackground(Color.WHITE);
        url.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        url.setPreferredSize(new Dimension(50, 30));


        send.setText("    Send   ");
        send.setEditable(false);
        send.setBackground(Color.WHITE);
        send.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        send.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    SwingWorker<Request ,Void> swingWorker = new SwingWorker<>() {
                        @Override
                        protected Request doInBackground() throws Exception {
                            if (menuBar.getRedirectBox().getState()) {
                                if (!Objects.equals(typeComboBox.getSelectedItem(), "GET")) {
                                    if (bodyTabs.getSelectedIndex() == 2) {
                                        return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                                "-i", "-H", getHeaderRequest(), "--upload", getBinary(), "-f"});
                                    }
                                    if (bodyTabs.getSelectedIndex() == 1) {
                                        return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                                "-i", "-H", getHeaderRequest(), "-j", getJson(), "-f"});
                                    } else {
                                        return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                                "-i", "-H", getHeaderRequest(), "-d", getFormData(), "-f"});
                                    }
                                }
                                else {
                                    return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                            "-i", "-H", getHeaderRequest(), "-f"});
                                }
                            }
                            else  {
                                if (!Objects.equals(typeComboBox.getSelectedItem(), "GET")) {
                                    if (bodyTabs.getSelectedIndex() == 2) {
                                        return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                                "-i", "-H", getHeaderRequest(), "--upload", getBinary()});
                                    }
                                    if (bodyTabs.getSelectedIndex() == 1) {
                                        return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                                "-i", "-H", getHeaderRequest(), "-j", getJson()});
                                    } else {
                                        return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                                "-i", "-H", getHeaderRequest(), "-d", getFormData()});
                                    }
                                }
                                else {
                                    return Command.Command(new String[]{getURL(), "-M", getMethod(),
                                            "-i", "-H", getHeaderRequest()});
                                }
                            }
                        }

                        @Override
                        protected void done() {
                            super.done();
                            try {
                                rightPanel.setRawDataText(get().getRawData());
                                rightPanel.setResponseTime(get().getResponseTime());
                                rightPanel.setResponseSize(get().getResponseSize());
                                rightPanel.setResponseMassageCode(get().getResponseMassage(), get().getResponseCode());
                                rightPanel.setVisualPreviewText(get().getResponseHeader(), get().getFileResponse(), get().getUrl());
                                rightPanel.setHeaderTable(get().getResponseHeader());
                                updateUI();
                            } catch (InterruptedException | ExecutionException | IOException ex) {
                                //JOptionPane.showMessageDialog(frame, "Enter valid request.", "Error", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    };
                    swingWorker.execute();
                } catch (Exception ex) {
                    //JOptionPane.showMessageDialog(frame, "Enter valid request.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                send.setBackground(mouseColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                send.setBackground(Color.white);
            }
        });


        firstPanel.add(typeComboBox, BorderLayout.WEST);
        firstPanel.add(url, BorderLayout.CENTER);
        firstPanel.add(send, BorderLayout.EAST);
        firstPanel.setPreferredSize(new Dimension(130, 30));//dorost shavad
    }

    /**
     * make body Panel.
     * <p>
     * The method makes body Panel for InsomniaGUI.MiddlePanel.
     */
    private void makeBody() {
        body.setLayout(new BorderLayout());
        body.setBackground(middleColor);
        body.setBorder(BorderFactory.createLineBorder(middleColor));
    }

    /**
     * make header Panel.
     * <p>
     * The method makes header Panel for InsomniaGUI.MiddlePanel.
     */
    private void makeHeader() {
        header.setLayout(new BorderLayout());
        header.setBackground(middleColor);
        header.setBorder(BorderFactory.createLineBorder(middleColor));
        addHeader();
        drawHeaders();
    }

    /**
     * make Tabs.
     * <p>
     * The method makes tabbedPane for InsomniaGUI.MiddlePanel.
     */
    private void makeTabs() {
        tabbedPane.setBackground(middleColor);
        tabbedPane.setForeground(Color.white);
        tabbedPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, middleColor));
        tabbedPane.addTab("Body ▼", bodyScroll);
        tabbedPane.addTab("Header ▼", headerScroll);
        bodyScroll.setBackground(middleColor);
        bodyScroll.setBorder(BorderFactory.createLineBorder(middleColor));
        headerScroll.setBackground(middleColor);
        headerScroll.setBorder(BorderFactory.createLineBorder(middleColor));
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = middleColor;
                lightHighlight = middleColor;
                shadow = middleColor;
                darkShadow = middleColor;
                focus = middleColor;
            }
        });
    }

    /**
     * make body Tabs.
     * <p>
     * The method makes bodyTabs for InsomniaGUI.MiddlePanel.
     */
    private void makeBodyTabs() {
        bodyTabs.setBackground(middleColor);
        bodyTabs.setForeground(Color.white);
        bodyTabs.setBorder(BorderFactory.createLineBorder(middleColor));
        bodyTabs.addTab("FORM DATA ▼", formDataScroll);
        bodyTabs.addTab("JSON ▼", jsonScroll);
        bodyTabs.addTab("UPLOAD BINARY ▼", binaryScroll);
        bodyTabs.setBackgroundAt(0, middleColor);
        bodyTabs.setBackgroundAt(1, middleColor);
        formDataScroll.setBackground(middleColor);
        formDataScroll.setBorder(BorderFactory.createLineBorder(middleColor));
        jsonScroll.setBackground(middleColor);
        jsonScroll.setBorder(BorderFactory.createLineBorder(middleColor));
        binaryScroll.setBackground(middleColor);
        binaryScroll.setBorder(BorderFactory.createLineBorder(middleColor));


        bodyTabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = middleColor;
                lightHighlight = middleColor;
                shadow = middleColor;
                darkShadow = middleColor;
                focus = middleColor;
            }
        });
    }

    /**
     * make formData Panel.
     * <p>
     * The method makes formData Panel for InsomniaGUI.MiddlePanel.
     */
    private void makeFormData() {
        formData.setLayout(new BorderLayout());
        formData.setBackground(middleColor);
        formData.setBorder(BorderFactory.createLineBorder(middleColor));
        addFormData();
        drawFormData();
    }

    /**
     * make json Panel.
     * <p>
     * The method makes json Panel for InsomniaGUI.MiddlePanel.
     */
    private void makeJson() {
        json.setLayout(new BorderLayout());
        json.setBackground(middleColor);
        json.setBorder(BorderFactory.createLineBorder(middleColor));
        jsonText.setBackground(middleColor);
        jsonText.setBorder(BorderFactory.createLineBorder(middleColor));
        jsonText.setText("It is Json.");
        jsonText.setForeground(Color.gray);
        jsonText.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jsonText.getText().equals("It is Json.")) {
                    jsonText.setText("");
                    jsonText.setForeground(textColor);
                }
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
                if (jsonText.getText().equals("")) {
                    jsonText.setForeground(Color.gray);
                    jsonText.setText("It is Json.");
                }
            }
        });
        json.add(jsonText, BorderLayout.CENTER);
    }

    /**
     * make binary Panel.
     * <p>
     * The method makes binary Panel for InsomniaGUI.MiddlePanel.
     */
    private void makeBinary() {
        binary.setLayout(new BorderLayout());
        binary.setBackground(middleColor);
        binary.setBorder(BorderFactory.createLineBorder(middleColor));

        binaryField.setBackground(middleColor);
        binaryField.setForeground(textColor);
        binaryField.setBorder(BorderFactory.createLineBorder(Color.gray));
        binaryField.setText("   Choose file   ");
        binaryField.setFont(new Font("Arial", Font.BOLD, 12));
        binaryField.setHorizontalAlignment(JTextField.CENTER);
        binaryField.setEditable(false);
        binaryField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser.showOpenDialog(null);
                file = fileChooser.getSelectedFile();
                filePath = file.getAbsolutePath();
                binaryField.setText(filePath);
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                binaryField.setBackground(mouseButtonColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                binaryField.setBackground(middleColor);
            }
        });
        binaryField.setPreferredSize(new Dimension(150, 30));
        binary.add(binaryField, BorderLayout.NORTH);
    }

    /**
     * add Header.
     * <p>
     * The method adds newHeader to headers.
     */
    private void addHeader() {
        JPanel newHeader = new JPanel();
        newHeader.setLayout(new BoxLayout(newHeader, BoxLayout.X_AXIS));
        newHeader.setBackground(middleColor);
        newHeader.setBorder(BorderFactory.createLineBorder(middleColor));
        ImageIcon iconDrag = new ImageIcon("./images/drag.png");
        JLabel drag = new JLabel(iconDrag);
        drag.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addHeader();
                drawHeaders();
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
        JTextField headerField = new JTextField();
        JTextField valueField = new JTextField();
        Checkbox checkBox = new Checkbox();
        checkBox.setBackground(middleColor);
        checkBox.setForeground(middleColor);
        Color interCheckBox = new Color(87, 88, 86);
        checkBox.addItemListener(e -> {
            if (checkBox.getState()) {
                headerField.setForeground(interCheckBox);
                valueField.setForeground(interCheckBox);
                headerField.setEditable(false);
                valueField.setEditable(false);
            } else {
                headerField.setForeground(textColor);
                valueField.setForeground(textColor);
                headerField.setEditable(true);
                valueField.setEditable(true);
            }
        });


        ImageIcon iconRecycle = new ImageIcon("./images/recycle.png");
        ImageIcon iconRecycleOn = new ImageIcon("./images/recycleOn.png");
        JLabel recycleBin = new JLabel(iconRecycle);
        recycleBin.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (headers.size() > 0) {
                    headers.remove(newHeader);
                    headersPanel.remove(newHeader);
                    updateUI();
                }
                if(headers.size() == 0) {
                    addHeader();
                    drawHeaders();
                    updateUI();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                recycleBin.setIcon(iconRecycleOn);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                recycleBin.setIcon(iconRecycle);
            }
        });

        headerField.setBackground(middleColor);
        headerField.setEditable(true);
        headerField.setForeground(textColor);
        headerField.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        headerField.setText("  header");
        headerField.setPreferredSize(new Dimension(15, 15));
        headerField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (headerField.getText().equals("  header") && !checkBox.getState()) {
                    headerField.setText("");
                }
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

        valueField.setBackground(middleColor);
        valueField.setEditable(true);
        valueField.setForeground(textColor);
        valueField.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        valueField.setText("  value");
        valueField.setPreferredSize(new Dimension(15, 15));
        valueField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (valueField.getText().equals("  value") && !checkBox.getState()) {
                    valueField.setText("");
                }
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

        newHeader.add(drag, BorderLayout.WEST);
        newHeader.add(headerField);
        newHeader.add(valueField);
        newHeader.add(checkBox);
        newHeader.add(recycleBin, BorderLayout.EAST);
        headers.add(newHeader);
    }

    /**
     * draw headers Panel.
     * <p>
     * The method makes headers Panel for InsomniaGUI.MiddlePanel.
     */
    private void drawHeaders() {
        for (JPanel temp : headers) {
            headersPanel.add(temp, BorderLayout.CENTER);
        }
        header.add(headersScrollPane);
        updateUI();
    }

    /**
     * add FormData.
     * <p>
     * The method adds newFormData to formData's.
     */
    private void addFormData() {
        JPanel newFormData = new JPanel();
        newFormData.setLayout(new BoxLayout(newFormData, BoxLayout.X_AXIS));
        newFormData.setBackground(middleColor);
        newFormData.setBorder(BorderFactory.createLineBorder(middleColor));
        ImageIcon iconDrag = new ImageIcon("./images/drag.png");
        JLabel drag = new JLabel(iconDrag);
        drag.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addFormData();
                drawFormData();
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
        JTextField formDataField = new JTextField();
        JTextField valueField = new JTextField();
        Checkbox checkBox = new Checkbox();
        checkBox.setBackground(middleColor);
        checkBox.setForeground(middleColor);
        Color interCheckBox = new Color(87, 88, 86);
        checkBox.addItemListener(e -> {
            if (checkBox.getState()) {
                formDataField.setForeground(interCheckBox);
                valueField.setForeground(interCheckBox);
                formDataField.setEditable(false);
                valueField.setEditable(false);
            } else {
                formDataField.setForeground(textColor);
                valueField.setForeground(textColor);
                formDataField.setEditable(true);
                valueField.setEditable(true);
            }
        });


        ImageIcon iconRecycle = new ImageIcon("./images/recycle.png");
        ImageIcon iconRecycleOn = new ImageIcon("./images/recycleOn.png");
        JLabel recycleBin = new JLabel(iconRecycle);
        recycleBin.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (formDatas.size() > 0) {
                    formDatas.remove(newFormData);
                    formDatasPanel.remove(newFormData);
                    updateUI();
                }
                if(formDatas.size() == 0 ) {
                    addFormData();
                    drawFormData();
                    updateUI();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                recycleBin.setIcon(iconRecycleOn);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                recycleBin.setIcon(iconRecycle);
            }
        });

        formDataField.setBackground(middleColor);
        formDataField.setEditable(true);
        formDataField.setForeground(textColor);
        formDataField.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        formDataField.setText("  header");
        formDataField.setPreferredSize(new Dimension(15, 15));
        formDataField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (formDataField.getText().equals("  header") && !checkBox.getState()) {
                    formDataField.setText("");
                }
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

        valueField.setBackground(middleColor);
        valueField.setEditable(true);
        valueField.setForeground(textColor);
        valueField.setBorder(BorderFactory.createLineBorder(new Color(54, 55, 52)));
        valueField.setText("  value");
        valueField.setPreferredSize(new Dimension(15, 15));
        valueField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (valueField.getText().equals("  value") && !checkBox.getState()) {
                    valueField.setText("");
                }
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

        newFormData.add(drag, BorderLayout.WEST);
        newFormData.add(formDataField);
        newFormData.add(valueField);
        newFormData.add(checkBox);
        newFormData.add(recycleBin, BorderLayout.EAST);
        formDatas.add(newFormData);
    }

    /**
     * draw form data's Panel.
     * <p>
     * The method makes  form data's Panel for InsomniaGUI.MiddlePanel.
     */
    private void drawFormData() {
        for (JPanel temp : formDatas) {
            formDatasPanel.add(temp, BorderLayout.CENTER);
        }
        formData.add(formDatasScrollPane);
        updateUI();
    }

    /**
     * get HeaderRequest of MiddlePanel.
     *
     * @return HeaderRequest.
     */
    public String getHeaderRequest() {
        ArrayList<String> headerRequest = new ArrayList<>();

        for(JPanel temp : headers) {
            Component[] component = temp.getComponents();
            JTextField headerField = (JTextField) component[1];
            JTextField valueField = (JTextField) component[2];
            Checkbox checkbox = (Checkbox) component[3];
            if (checkbox.getState()) {
                headerRequest.add(headerField.getText() + ":" + valueField.getText());
            }
        }

        try {
            StringBuilder header = new StringBuilder();
            for (String s : headerRequest) {
                header.append(s).append(";");
            }
            header = new StringBuilder(header.substring(0, header.length() - 1));
            return header.toString();
        }catch (Exception e) {
            return "";
        }
    }

    /**
     * get FormData of MiddlePanel.
     *
     * @return FormData.
     */
    public String getFormData() {
        ArrayList<String> formDataRequest = new ArrayList<>();

        for(JPanel temp : formDatas) {
            Component[] component = temp.getComponents();
            JTextField formDataField = (JTextField) component[1];
            JTextField valueField = (JTextField) component[2];
            Checkbox checkbox = (Checkbox) component[3];
            if (checkbox.getState()) {
                formDataRequest.add(formDataField.getText() + "=" + valueField.getText());
            }
        }

        try {
            StringBuilder formData = new StringBuilder();
            for (String s : formDataRequest) {
                formData.append(s).append("&");
            }
            formData = new StringBuilder(formData.substring(0, formData.length() - 1));
            return formData.toString();
        }catch (Exception e) {
            return "";
        }
    }

    /**
     * get URL of MiddlePanel.
     *
     * @return url.
     */
    public String getURL() {
        return url.getText();
    }

    /**
     * get Method of MiddlePanel.
     *
     * @return typeComboBox. type of method.
     */
    public String getMethod() {
        return (String) typeComboBox.getSelectedItem();
    }

    /**
     * get HeaderRequest of MiddlePanel to MyRequest.
     *
     * @return HeaderRequest.
     */
    public String getHeaderRequest2() {
        ArrayList<String> headerRequest = new ArrayList<>();

        for(JPanel temp : headers) {
            Component[] component = temp.getComponents();
            JTextField headerField = (JTextField) component[1];
            JTextField valueField = (JTextField) component[2];
            headerRequest.add(headerField.getText() + ":" + valueField.getText());
        }

        try {
            StringBuilder header = new StringBuilder();
            for (String s : headerRequest) {
                header.append(s).append(";");
            }
            header = new StringBuilder(header.substring(0, header.length() - 1));
            return header.toString();
        }catch (Exception e) {
            return "";
        }
    }

    /**
     * get FormData of MiddlePanel to MyRequest.
     *
     * @return FormData.
     */
    public String getFormData2() {
        ArrayList<String> formDataRequest = new ArrayList<>();

        for(JPanel temp : formDatas) {
            Component[] component = temp.getComponents();
            JTextField formDataField = (JTextField) component[1];
            JTextField valueField = (JTextField) component[2];
            formDataRequest.add(formDataField.getText() + "=" + valueField.getText());
        }

        try {
            StringBuilder formData = new StringBuilder();
            for (String s : formDataRequest) {
                formData.append(s).append("&");
            }
            formData = new StringBuilder(formData.substring(0, formData.length() - 1));
            return formData.toString();
        }catch (Exception e) {
            return "";
        }
    }

    /**
     * get Json of MiddlePanel.
     *
     * @return jsonText.
     */
    public String getJson() {
        return jsonText.getText();
    }

    /**
     * get Binary of MiddlePanel.
     *
     * @return binaryText.
     */
    public String getBinary() {
        return binaryField.getText();
    }

    /**
     * set urlText of MiddlePanel
     *
     * @param url url.
     */
    public void setURL(String url) {
        this.url.setText(url);
    }

    /**
     * set method of MiddlePanel
     *
     * @param method method.
     */
    public void setMethod(String method) {
        this.typeComboBox.setSelectedItem(method);
    }

    /**
     * set HeaderRequest of MiddlePanel
     *
     * @param header header.
     */
    public void setHeaderRequest(String header) {
        String[] lines = header.split(";");

        while (headers.size() > 0) {
            headers.remove(0);
            headersPanel.remove(0);
        }

        for (int i = 0; i < lines.length; i++) {
            addHeader();
        }

        for(int i = 0; i < lines.length; i++) {
            try {
                Component[] component = headers.get(i).getComponents();
                JTextField headerField = (JTextField) component[1];
                JTextField valueField = (JTextField) component[2];
                String[] keyValue = lines[i].split(":");
                headerField.setText(keyValue[0]);
                valueField.setText(keyValue[1]);
            }catch (Exception ignored){}
        }

        drawHeaders();
    }

    /**
     * set FormData of MiddlePanel
     *
     * @param formData formData.
     */
    public void setFormData(String formData) {
        String[] lines = formData.split("&");

        while (formDatas.size() > 0) {
            formDatas.remove(0);
            formDatasPanel.remove(0);
        }

        for (int i = 0; i < lines.length; i++) {
            addFormData();
        }

        for(int i = 0; i < lines.length; i++) {
            try{
            Component[] component = formDatas.get(i).getComponents();
            JTextField formDataField = (JTextField) component[1];
            JTextField valueField = (JTextField) component[2];
            String[] keyValue = lines[i].split("=");
            formDataField.setText(keyValue[0]);
            valueField.setText(keyValue[1]);
            }catch (Exception ignored){}
        }

        drawFormData();
    }

    /**
     * set JsonText of MiddlePanel
     *
     * @param json json.
     */
    public void setJson(String json) {
        this.jsonText.setText(json);
    }

    /**
     * set binaryField of MiddlePanel
     *
     * @param binary binary.
     */
    public void setBinary(String binary) {
        binaryField.setText(binary);
    }
}
