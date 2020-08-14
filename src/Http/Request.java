package Http;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * A class to make Request.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class Request implements Serializable {
    //URL address
    private URL url;
    //method connection
    private final String method;
    //connection
    private transient HttpURLConnection connection;
    //a map for storing header request
    private LinkedHashMap<String, String> requestHeader;
    //a map for storing form data request
    private LinkedHashMap<String, String> formData;
    //uploadAddress
    private String uploadAddress;
    //json
    private String json;
    //a map for storing header response
    private Map<String, List<String>> responseHeader;
    //response code
    private int responseCode;
    //response massage
    private String responseMassage;
    //response size
    private String responseSize;
    //response time
    private String responseTime;
    //fileResponse
    private byte[] fileResponse;
    //raw data
    private String rawData;
    //a String for save address
    private static final String responseBody_PATH = "./saveResponse/";
    //condition of showing header
    private boolean showResponseHeader;
    //condition of follow redirect
    private boolean followRedirect;
    //condition of take output
    private boolean saveResponseBody;
    //name for save responseBody
    private String nameForSaveResponseBody;
    //condition of json
    private boolean isJson;
    //condition of upload
    private boolean upload;
    //start time
    private long t1;

    /**
     * Create a new Request with given fields.
     *
     * @param url URL address
     * @param method method connection
     * @param requestHeader a map for storing header request
     * @param showResponseHeader condition of showing header
     * @param followRedirect condition of follow redirect
     * @param saveResponseBody condition of take output
     * @param nameForSaveResponseBody name for save responseBody
     * @param formData a map for storing form data request
     * @param json json
     * @param isJson condition of json
     * @param uploadAddress uploadAddress
     * @param upload condition of upload
     * @throws IOException IOException
     */
    public Request(URL url, String method, LinkedHashMap<String, String> requestHeader, boolean showResponseHeader,
                   boolean followRedirect, boolean saveResponseBody, String nameForSaveResponseBody,
                   LinkedHashMap<String, String> formData, String json, boolean isJson, String uploadAddress, boolean upload) throws IOException {
        this.method = method;
        this.url = url;
        this.requestHeader = requestHeader;
        this.responseHeader = new HashMap<>();
        this.formData = formData;
        this.uploadAddress = uploadAddress;
        this.json = json;
        this.showResponseHeader = showResponseHeader;
        this.followRedirect = followRedirect;
        this.saveResponseBody = saveResponseBody;
        this.nameForSaveResponseBody = nameForSaveResponseBody;
        this.isJson = isJson;
        this.upload = upload;
        sendRequest();
    }

    /**
     * send.
     *
     * The method send Request.
     * @throws IOException IOException
     */
    public void sendRequest() throws IOException {
        if (method.equals("PATCH")) {
            allowPatchMethods();
        }
        connection = (HttpURLConnection) url.openConnection();
        t1 = System.currentTimeMillis();
        if (method.equals("PATCH")) {
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.setRequestMethod("POST");
        }
        else {
            connection.setRequestMethod(method);
        }
        if (!method.equals("GET")) {
            if (upload) {
                uploadBinary(uploadAddress);
            }
            if (isJson) {
                json();
            } else {
                sendFormData();
            }
        }
        setHeaderProperty();

        if (setResponseCode()) {
            setResponseHeader();
            if (followRedirect && responseCode / 100 == 3) {
                URL reDirectUrl = new URL(followRedirectAddress());
                connection = (HttpURLConnection) reDirectUrl.openConnection();
                connection.setRequestMethod(method);
                setHeaderProperty();
                setResponseCode();
                setResponseHeader();
            }
            setResponseMassage();
            setResponseBody();
            setResponseTime();
            setResponseSize();
            printResponseBody();
            if (showResponseHeader) {
                printResponseHeader();
            }
            if (saveResponseBody) {
                saveResponseBodyFileStream(Objects.requireNonNullElseGet(nameForSaveResponseBody, () -> "output_[" + LocalDate.now() + "].txt"));
            }
        }
    }

    /**
     * set property for header.
     *
     * The method set header to property.
     */
    private void setHeaderProperty() {
        try {
            for (String key : requestHeader.keySet()) {
                connection.setRequestProperty(key, requestHeader.get(key));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * get an address for follow redirect.
     *
     * @return a redirect address.
     */
    private String followRedirectAddress() {
        return responseHeader.get("Location").get(0);
    }

    /**
     * uploadBinary with given file address.
     *
     * @param uploadAddress file address
     */
    private void uploadBinary(String uploadAddress) {
        try {
            File file = new File(uploadAddress);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedOutputStream.write(fileInputStream.readAllBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            //System.out.println("It doesn't exist.");
        }
    }

    /**
     * send form data buffer.
     *
     * The method send form data buffer.
     * @param body body
     * @param boundary boundary
     * @param bufferedOutputStream bufferedOutputStream
     * @throws IOException IOException
     */
    private static void bufferOutFormData(LinkedHashMap<String, String> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : body.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(body.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(new File(body.get(key))));
                    byte[] filesBytes = tempBufferedInputStream.readAllBytes();
                    bufferedOutputStream.write(filesBytes);
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((body.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    /**
     * send request form data.
     *
     * The method send form data.
     */
    private void sendFormData() {
        try {
            String boundary = System.currentTimeMillis() + "";
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            BufferedOutputStream request = new BufferedOutputStream(connection.getOutputStream());
            bufferOutFormData(formData, boundary, request);
        } catch (Exception ignored) {}
    }

    /**
     * send json.
     *
     * The method send json.
     *  @throws IOException IOException
     */
    private void json() throws IOException {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        BufferedOutputStream request = new BufferedOutputStream(connection.getOutputStream());
        request.write((json).getBytes());
        request.flush();
        request.close();
    }

    /**
     * save response body in output.
     *
     * The method save response body.
     *
     * @param fileName name of file for saving
     */
    private void saveResponseBodyFileStream(String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(responseBody_PATH + fileName);
            byte[] strToByteArray = fileResponse;
            fileOutputStream.write(strToByteArray);
            fileOutputStream.close();
        } catch (Exception e) {
            //System.out.println("Can not make file.");
        }
    }

    /**
     * set response header.
     *
     */
    private void setResponseHeader() {
        responseHeader = connection.getHeaderFields();
    }

    /**
     * set response body
     *
     */
    private void setResponseBody() throws IOException {
        BufferedInputStream bufferedInputStream;
        if ((responseCode / 100) == 4 || (responseCode / 100) == 5) {
            bufferedInputStream = new BufferedInputStream(connection.getErrorStream());
        } else {
            bufferedInputStream = new BufferedInputStream(connection.getInputStream());
        }
        fileResponse = bufferedInputStream.readAllBytes();
        rawData = new String(fileResponse);
    }

    /**
     * set response code
     *
     */
    private boolean setResponseCode()  {
        try {
            responseCode = connection.getResponseCode();
            return true;
        } catch (IOException exception) {
            rawData = "Error: Couldn't resolve host name";
            printResponseBody();
            responseMassage = "Error";
            responseSize = "0 B";
            responseTime = "0 ms";
            responseCode = 0;
            fileResponse = null;
            return false;
        }
    }

    /**
     * set response size
     *
     */
    private void setResponseSize() {
        double size = fileResponse.length;
        if (fileResponse.length / 1000 >= 1) {
            size = (double) fileResponse.length / 1024;
            responseSize = ((double) ((int) (Math.round((size) * 10))) / 10) + " KB";
        }
        else {
            responseSize = size + " B";
        }
    }

    /**
     * set response massage
     *
     */
    private void setResponseMassage() throws IOException {
        responseMassage = connection.getResponseMessage();
    }

    /**
     * set response time
     *
     */
    private void setResponseTime() {
        //end time
        long t2 = System.currentTimeMillis();
        responseTime = (t2 - t1) + " ms";
    }

    /**
     * print response body
     *
     */
    private void printResponseBody() {
        //System.out.println(rawData);
        //System.out.println();
    }

    /**
     * print response header
     *
     */
    private void printResponseHeader() {
        //System.out.println(responseHeader);
        //System.out.println();
    }

    /**
     * get a String for show Request.
     *
     * @return a String.
     */
    @Override
    public String toString() {
        return "url: " + url +
                " | method: " + method +
                " | requestHeader: " + requestHeader +
                " | formData: " + formData +
                " | uploadAddress: " + uploadAddress +
                " | json: " + json;
    }

    /**
     * get Raw Data of Request.
     *
     * @return RawData.
     */
    public String getRawData() {
        return rawData;
    }

    /**
     * get Response Time of Request.
     *
     * @return ResponseTime.
     */
    public String getResponseTime() {
        return responseTime;
    }

    /**
     * get Response Size of Request.
     *
     * @return ResponseTime.
     */
    public String getResponseSize() {
        return responseSize;
    }

    /**
     * get Response Code of Request.
     *
     * @return Response Code.
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * get Response Massage of Request.
     *
     * @return Response Massage.
     */
    public String getResponseMassage() {
        return responseMassage;
    }

    /**
     * get Response Header of Request.
     *
     * @return Response Header.
     */
    public Map<String, List<String>> getResponseHeader() {
        return responseHeader;
    }

    /**
     * get Response File of Request.
     *
     * @return Response File.
     */
    public byte[] getFileResponse() {
        return fileResponse;
    }

    /**
     * get URL of Request.
     *
     * @return url.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Handel Patch method.
     *
     */
    private static void allowPatchMethods() {
            try {
                Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(methodsField,methodsField.getModifiers() & ~Modifier.FINAL);
                methodsField.setAccessible(true);
                String[] oldMethods = (String[])methodsField.get(null);
                Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
                methodsSet.addAll(Collections.singletonList("PATCH"));
                String[] newMethods = methodsSet.toArray(new String[0]);
                methodsField.set(null, /*static field*/newMethods);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                //throw new IllegalStateException(e);
            }
    }
}
