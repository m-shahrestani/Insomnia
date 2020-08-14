package gui;

import java.io.Serializable;

/**
 * A class to store MyRequest.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class MyRequest implements Serializable {
    //name of request
    private String name;
    //url of request
    private String url;
    //method of request
    private String method;
    //header of request
    private String header;
    //formData of request
    private String formData;
    //json of request
    private String json;
    //binary of request
    private String binary;
    //condition of clicking
    private boolean isClicked;

    /**
     * Create a new MyRequest.
     *
     * @param name name of request.
     */
    public MyRequest(String name) {
        this.name = name;
        url = "https://www.google.com";
        method = "GET";
        header = "  header:  value";
        formData = "  header=  value";
        json = "It is Json.";
        binary = "   Choose file   ";
        isClicked = false;
    }

    /**
     * fill data.
     *
     * The method loads data to middlePanel.
     *
     * @param middlePanel middle panel.
     */
    public void fillData(MiddlePanel middlePanel) {
       middlePanel.setURL(url);
       middlePanel.setMethod(method);
       middlePanel.setHeaderRequest(header);
       middlePanel.setFormData(formData);
       middlePanel.setJson(json);
       middlePanel.setBinary(binary);
    }

    /**
     * back up data.
     *
     * The method saves data from middlePanel.
     *
     * @param middlePanel middle panel.
     */
    public void backUpData(MiddlePanel middlePanel) {
        url = middlePanel.getURL();
        method = middlePanel.getMethod();
        header = middlePanel.getHeaderRequest2();
        formData = middlePanel.getFormData2();
        json = middlePanel.getJson();
        binary = middlePanel.getBinary();
    }

    /**
     * set clicked.
     *
     */
    public void setClicked() {
        isClicked = true;
    }

    /**
     * set unClicked.
     *
     */
    public void setUnClicked() {
        isClicked = false;
    }

    /**
     * get condition of clicking.
     *
     * @return if is clicked return true, otherwise false.
     */
    public boolean isClicked() {
        return isClicked;
    }

    /**
     * get Name of MyRequest.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * get Method of MyRequest.
     *
     * @return method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * set Name of MyRequest
     *
     * @param name new name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
