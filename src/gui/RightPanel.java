package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * A class to design and manage RightPanel.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class RightPanel extends JPanel {
    //Color of right panel
    private Color rightColor;
    //Color of text
    private Color textColor;
    //Color of mouse button
    private Color mouseButtonColor;
    //firstPanel
    private JPanel firstPanel;
    //responseCode
    private JTextField responseMassageCode;
    //time textField
    private JTextField responseTime;
    //size textField
    private JTextField responseSize;
    //tabbedPanel
    private JTabbedPane tabbedPanel;
    //previewScroll
    private JScrollPane previewScroll;
    //headersScroll
    private JScrollPane headersScroll;
    //preview panel
    private JPanel preview;
    // headers panel
    private JPanel headers;
    //previewTabs panel
    private JTabbedPane previewTabs;
    //visualPreviewScroll
    private JScrollPane visualPreviewScroll;
    //rawDataScroll
    private JScrollPane rawDataScroll;
    //visualPreview panel
    private JPanel visualPreview;
    //rawData panel
    private JPanel rawData;
    //visualPreviewText
    private JTextPane visualPreviewText;
    //rawDataText
    private JTextArea rawDataText;
    //headerTable
    private JTable headerTable;
    //number of table's row
    private int numberOfRow;
    //copyField
    private JTextField copyField;
    //jEditorPane
    private JEditorPane jEditorPane;

    /**
     * Create a new RightPanel.
     *
     */
    public RightPanel() {
        rightColor = new Color(40,41,37);
        textColor = new Color(255,255,255);
        UIManager.put("TabbedPane.selected", new Color(71,72,69));
        mouseButtonColor = new Color(54, 55, 52);
        setLayout(new BorderLayout());
        setBackground(rightColor);
        setBorder(BorderFactory.createLineBorder(rightColor));

        firstPanel = new JPanel();
        responseMassageCode = new JTextField();
        responseTime = new JTextField();
        responseSize = new JTextField();
        makeFirstPanel();

        headerTable = new JTable();
        numberOfRow = 0;
        copyField = new JTextField();
        tabbedPanel = new JTabbedPane();
        preview = new JPanel();
        headers = new JPanel();
        previewScroll = new JScrollPane(preview);
        headersScroll =  new JScrollPane(headers);
        headersScroll.setBackground(rightColor);
        headersScroll.setBorder(BorderFactory.createLineBorder(rightColor));
        makePreview();
        makeHeaders();
        makeTabs();

        rawDataText = new JTextArea();
        previewTabs = new JTabbedPane();
        visualPreview = new JPanel();
        rawData = new JPanel();
        visualPreviewScroll = new JScrollPane(visualPreview);
        rawDataScroll = new JScrollPane(rawData);

        makePreviewTabs();
        makeVisualPreview();
        makeRawData();
        preview.add(previewTabs, BorderLayout.CENTER);

        add(firstPanel, BorderLayout.NORTH);
        add(tabbedPanel, BorderLayout.CENTER);
    }

    /**
     * make first Panel.
     *
     * The method makes first Panel for RightPanel.
     */
    private void makeFirstPanel() {
        firstPanel.setLayout(new BoxLayout(firstPanel,BoxLayout.X_AXIS));
        firstPanel.setPreferredSize(new Dimension(70, 40));
        firstPanel.setBackground(Color.white);
        firstPanel.setBorder(BorderFactory.createLineBorder(Color.white));

        Color redResponseColor = new Color(225,82,81);
        responseMassageCode.setText("  Null  ");
        responseMassageCode.setHorizontalAlignment(JTextField.CENTER);
        responseMassageCode.setFont(new Font("Arial", Font.BOLD, 14));
        responseMassageCode.setEditable(false);
        responseMassageCode.setBackground(redResponseColor);
        responseMassageCode.setForeground(Color.WHITE);
        responseMassageCode.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        responseMassageCode.setToolTipText("The request has succeeded.");
        responseMassageCode.setToolTipText("Response Code.");
        responseMassageCode.setPreferredSize(new Dimension(15, 15));

        Color timeColor = new Color(224,224,224);
        responseTime.setText("  0 ms  ");
        responseTime.setHorizontalAlignment(JTextField.CENTER);
        responseTime.setEditable(false);
        responseTime.setBackground(timeColor);
        responseTime.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        responseTime.setToolTipText("milliseconds");
        responseTime.setPreferredSize(new Dimension(15, 15));


        Color sizeColor = new Color(224,224,224);
        responseSize.setText("  0.0 B  ");
        responseSize.setHorizontalAlignment(JTextField.CENTER);
        responseSize.setEditable(false);
        responseSize.setBackground(sizeColor);
        responseSize.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        responseSize.setToolTipText("kilobytes");
        responseSize.setPreferredSize(new Dimension(15, 15));

        firstPanel.add(responseMassageCode, BorderLayout.WEST);
        firstPanel.add(responseTime);
        firstPanel.add(responseSize);
    }

    /**
     * make preview Panel.
     *
     * The method makes preview Panel for RightPanel.
     */
    private void makePreview() {
        preview.setLayout(new BorderLayout());
        preview.setBackground(rightColor);
        preview.setBorder(BorderFactory.createLineBorder(rightColor));
    }

    /**
     * make headers Panel.
     *
     * The method makes headers Panel for RightPanel.
     */
    private void makeHeaders() {
        headers.setBackground(rightColor);
        headers.setBorder(BorderFactory.createLineBorder(rightColor));
        headers.setLayout(new BorderLayout());

        // Column Names
        String[] columnNames = { "Name", "Value"};
        headerTable = new JTable(new DefaultTableModel(new String[][]{}, columnNames));
        headerTable.setBounds(40, 40, 70, 200);

        copyField.setBackground(rightColor);
        copyField.setForeground(textColor);
        copyField.setBorder(BorderFactory.createLineBorder(Color.gray));
        copyField.setText("   Copy to Clipboard   ");
        copyField.setFont(new Font("Arial", Font.BOLD, 12));
        copyField.setHorizontalAlignment(JTextField.CENTER);
        copyField.setEditable(false);
        copyField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringBuilder string = new StringBuilder();
                for (int i = 1; i < numberOfRow; i++) {
                    string.append(headerTable.getValueAt(i, 0));
                    string.append(": ");
                    string.append(headerTable.getValueAt(i, 1));
                    string.append("\n");
                }

                StringSelection selection = new StringSelection(string.toString());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                copyField.setBackground(mouseButtonColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                copyField.setBackground(rightColor);
            }
        });
        copyField.setPreferredSize(new Dimension(150, 30));

        JScrollPane scrollTable = new JScrollPane(headerTable);
        scrollTable.setPreferredSize(new Dimension(100, 425));
        headers.add(scrollTable, BorderLayout.NORTH);
        headers.add(copyField, BorderLayout.SOUTH);
    }

    /**
     * make Tabs.
     *
     * The method makes Tabs for RightPanel.
     */
    private void makeTabs(){
        tabbedPanel.setBackground(rightColor);
        tabbedPanel.setForeground(Color.white);
        tabbedPanel.setBorder(BorderFactory.createLineBorder(rightColor));
        previewScroll.setBackground(rightColor);
        previewScroll.setBorder(BorderFactory.createLineBorder(rightColor));
        headersScroll.setBackground(rightColor);
        headers.setBorder(BorderFactory.createLineBorder(rightColor));
        tabbedPanel.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = rightColor;
                lightHighlight = rightColor;
                shadow = rightColor;
                darkShadow = rightColor;
                focus = rightColor;
            }
        });

        tabbedPanel.addTab("Preview ▼", previewScroll);
        tabbedPanel.addTab("Header ▼", headersScroll);
        tabbedPanel.setBounds(0,0,400,300);
        tabbedPanel.setBackground(rightColor);
        tabbedPanel.setBackgroundAt(1, rightColor);
        tabbedPanel.setBackgroundAt(0, rightColor);
    }

    /**
     * make preview Tabs.
     *
     * The method makes preview for RightPanel.
     */
    private void makePreviewTabs() {
        previewTabs.setBackground(rightColor);
        previewTabs.setForeground(Color.white);
        previewTabs.setBorder(BorderFactory.createLineBorder(rightColor));
        previewTabs.addTab("Raw Data ▼", rawDataScroll);
        previewTabs.addTab("Visual Preview ▼", visualPreviewScroll);
        rawDataScroll.setBackground(rightColor);
        rawDataScroll.setBorder(BorderFactory.createLineBorder(rightColor));
        visualPreviewScroll.setBackground(rightColor);
        visualPreviewScroll.setBorder(BorderFactory.createLineBorder(rightColor));
        previewTabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = rightColor;
                lightHighlight = rightColor;
                shadow = rightColor;
                darkShadow = rightColor;
                focus = rightColor;
            }
        });
    }

    /**
     * make visualPreview Panel.
     *
     * The method makes visualPreview Panel for RightPanel.
     */
    private void makeVisualPreview() {
        visualPreviewText = new JTextPane();
        visualPreview.setBackground(rightColor);
        visualPreview.setBorder(BorderFactory.createLineBorder(rightColor));
        visualPreview.setLayout(new BorderLayout());
        visualPreview.setBorder(BorderFactory.createLineBorder(rightColor));
        visualPreviewText.setBorder(BorderFactory.createLineBorder(rightColor));
        visualPreviewText.setText("It is Visual preview.");
        visualPreviewText.setBackground(rightColor);
        visualPreviewText.setForeground(Color.gray);
        visualPreviewText.setEditable(false);
        visualPreview.add(visualPreviewText, BorderLayout.CENTER);
    }

    /**
     * make rawData Panel.
     *
     * The method makes rawData Panel for RightPanel.
     */
    private void makeRawData() {
        rawData.setLayout(new BorderLayout());
        rawData.setBackground(rightColor);
        rawData.setBorder(BorderFactory.createLineBorder(rightColor));
        rawDataText.setBackground(rightColor);
        rawDataText.setBorder(BorderFactory.createLineBorder(rightColor));
        rawDataText.setText("It is Raw Data.");
        rawDataText.setForeground(Color.gray);
        rawDataText.setEditable(false);
        rawData.add(rawDataText, BorderLayout.CENTER);
    }

    /**
     * set RawData Text of RightPanel
     *
     * @param rawDataText RawData.
     */
    public void setRawDataText(String rawDataText) {
        this.rawDataText.setForeground(textColor);
        this.rawDataText.setText(rawDataText);
    }

     /**
     * set Response Time of RightPanel
     *
     * @param responseTime Response Time.
     */
    public void setResponseTime(String responseTime){
        this.responseTime.setText(responseTime);
    }

    /**
     * set Response Size of RightPanel
     *
     * @param responseSize Response Size.
     */
    public void setResponseSize(String responseSize) {
        this.responseSize.setText(responseSize);
    }

    /**
     * set Response Massage and code of RightPanel
     *
     * @param responseMassage Response Massage.
     * @param responseCode Response Code
     */
    public void setResponseMassageCode(String responseMassage, int responseCode) {
        Color greenResponseColor = new Color(117,186,36);
        Color redResponseColor = new Color(225,82,81);
        if (responseCode == 0) {
            this.responseMassageCode.setText(responseMassage);
            this.responseMassageCode.setBackground(redResponseColor);
        }
        if (responseCode/100 == 5 || responseCode/100 == 4) {
            this.responseMassageCode.setText(responseCode + " " + responseMassage);
            this.responseMassageCode.setBackground(redResponseColor);
        }
        if (responseCode/100 == 3) {
            this.responseMassageCode.setText(responseCode + " " + responseMassage);
            this.responseMassageCode.setBackground(Color.orange);
        }
        if (responseCode/100 == 2) {
            this.responseMassageCode.setText(responseCode + " " + responseMassage);
            this.responseMassageCode.setBackground(greenResponseColor);
        }
        if (responseCode/100 == 1) {
            this.responseMassageCode.setText(responseCode + " " + responseMassage);
            this.responseMassageCode.setBackground(Color.yellow);
        }
    }

    /**
     * reset VisualPreviewText of RightPanel
     *
     */
    private void resetVisualPreviewText() {
        visualPreviewText = new JTextPane();
        visualPreview.remove(0);
        visualPreviewText.setText("It is Visual preview.");
        visualPreviewText.setForeground(Color.gray);
        visualPreviewText.setBackground(rightColor);
        visualPreview.add(visualPreviewText, BorderLayout.CENTER);
        updateUI();
    }

    /**
     * set VisualPreviewText of RightPanel
     *
     * @param responseHeader header of response.
     * @param fileResponse file of response.
     * @param url url.
     * @throws IOException exception.
     */
    public void setVisualPreviewText(Map<String, List<String>> responseHeader, byte[] fileResponse, URL url ) throws IOException {
        resetVisualPreviewText();
        if (fileResponse != null) {
            if (responseHeader.get("Content-Type") == null) {
                visualPreviewText.setText(new String(fileResponse));
            } else if (responseHeader.get("Content-Type").toString().contains("text/html")) {
                if (responseMassageCode.getText().contains("OK")) {
                    jEditorPane = new JEditorPane(url);
                    jEditorPane.setEditable(false);
                    jEditorPane.setBackground(rightColor);
                    visualPreview.remove(0);
                    visualPreview.add(jEditorPane, BorderLayout.CENTER);
                }
                else {
                    visualPreviewText.setContentType("text/html");
                    visualPreviewText.setBackground(Color.WHITE);
                    visualPreviewText.setText(new String(fileResponse));
                }
                updateUI();
            } else if (responseHeader.get("Content-Type").toString().contains("image")) {
                visualPreviewText.setText("");
                ImageIcon imageIcon = new ImageIcon(fileResponse);
                visualPreviewText.insertIcon(imageIcon);
                updateUI();
            }
            else if (responseHeader.get("Content-Type").toString().contains("json")) {
                visualPreviewText.setText("");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(new String(fileResponse));
                visualPreviewText.setText(gson.toJson(jsonElement));
                visualPreviewText.setForeground(textColor);
                visualPreviewText.setBackground(rightColor);
                updateUI();
            }
            else {
                visualPreviewText.setText(new String(fileResponse));
                visualPreviewText.setForeground(textColor);
                visualPreviewText.setBackground(rightColor);
            }
        }
        updateUI();
    }

    /**
     * reset JTable of RightPanel.
     *
     */
    private void resetTable(){
        DefaultTableModel row = (DefaultTableModel) headerTable.getModel();
        for (int i = 0; i < numberOfRow; i++) {
            row.removeRow(0);
        }
    }

    /**
     * set header of response with Initializing the JTable.
     *
     * @param responseHeader header of response.
     */
    public void setHeaderTable(Map<String, List<String>> responseHeader){
        resetTable();
        int i = 0;
        for (String key : responseHeader.keySet()) {
            if (key == null) {
                i++;
                continue;
            }
            DefaultTableModel row = (DefaultTableModel) headerTable.getModel();
            row.addRow(new Object[]{key, responseHeader.get(key).toString()});
        }
        numberOfRow = responseHeader.size() - i;
        updateUI();
    }
}
