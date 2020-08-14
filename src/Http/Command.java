package Http;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A class to make Command.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class Command {
    //command
    private static String[] command;
    //url
    private static URL url;
    //method
    private static String method;
    //a map for storing header request
    private static LinkedHashMap<String, String> requestHeader;
    //condition of showing header
    private static boolean showResponseHeader;
    //condition of follow redirect
    private static boolean followRedirect;
    //condition of take output
    private static boolean saveResponseBody;
    //name for save responseBody
    private static String nameForSaveResponseBody;
    //a String for save address
    private static final String REQ_PATH = "./saveRequest/request.txt";
    //an array for requests
    private static ArrayList<Request> requests;
    //a map for storing form data request
    private static LinkedHashMap<String, String> formData;
    //condition of json
    private static boolean isJson;
    //json
    private static String json;
    //condition of upload
    private static boolean upload;
    //uploadAddress
    private static String uploadAddress;
    //recent request
    private static Request recentRequest;

    /**
     * Create a new Command.
     *
     * @param args array of string
     * @throws IOException IOException
     * @return Request  request.
     */
    public static Request Command(String[] args) throws IOException {
        command = args;
        showResponseHeader = false;
        followRedirect = false;
        saveResponseBody = false;
        nameForSaveResponseBody = null;
        requestHeader = new LinkedHashMap<>();
        requests = new ArrayList<>();
        formData = new LinkedHashMap<>();
        upload = false;
        isJson = false;
        loadRequests();
        decide();
        return recentRequest;
    }

    /**
     * decide to do witch behavior.
     *
     */
    private static void decide() throws IOException {

        if (command.length == 0) {
            //System.out.println("Input haven't entered.");
            printError();
        }

        //list
        if(command.length == 1 && command[0].equals("list")) {
            printList();
        }
        if(command.length > 1 && command[0].equals("list")) {
            System.out.println("list command should have one argument.");
            printError();
        }

        //help
        if(command.length == 1 && (command[0].equals("--help") || command[0].equals("-h"))) {
            help();
        }
        if(command.length > 1 && (command[0].equals("--help") || command[0].equals("-h"))) {
            System.out.println("--help/-h command should have one argument.");
            printError();
        }

        if (command.length == 1 && command[0].equals("fire")) {
            System.out.println("fire command should have more than input.(for example: fire 2 3)");
            printError();
        }

        //fire and url
        if (command.length >= 1) {

            //url
            if (!command[0].equals("fire")) {

                //url
                if (command[0].startsWith("http://") || command[0].startsWith("https://"))
                {
                    url = new URL(command[0]);
                }
                else {
                    url = new URL("http://" + command[0]);
                }

                //default method
                method = "GET";

                for (int i = 0; i < command.length; i++) {
                    String s = command[i];
                    try {
                        //invalid command
                        if (s.startsWith("-")) {
                            if (!("--method".equals(s) || "-M".equals(s)) && !("--headers".equals(s) || "-H".equals(s)) &&
                                    !("-i".equals(s)) && !("-f".equals(s)) && !("--output".equals(s) || "-O".equals(s)) &&
                                    !("--data".equals(s) || "-d".equals(s)) && !("--json".equals(s) || "-j".equals(s)) &&
                                    !("--upload".equals(s)) && !("--save".equals(s) || "-S".equals(s)))
                            {
                                //System.out.println(command[i] + " isn't valid.");
                                printError();
                            }
                        }

                        //set method
                        if ("--method".equals(s) || "-M".equals(s)) {
                            if (command[i + 1].equals("GET")) {
                                method = "GET";
                            }
                            if (command[i + 1].equals("PUT")) {
                                method = "PUT";
                            }
                            if (command[i + 1].equals("POST")) {
                                method = "POST";
                            }
                            if (command[i + 1].equals("DELETE")) {
                                method = "DELETE";
                            }
                        }

                        //get request header
                        if ("--headers".equals(s) || "-H".equals(s)) {
                            getRequestHeader(i + 1);
                        }

                        //show Header response
                        if ("-i".equals(s)) {
                            showResponseHeader = true;
                        }

                        //enable follow redirect
                        if ("-f".equals(s)) {
                            followRedirect = true;
                        }

                        //output response body.
                        if ("--output".equals(s) || "-O".equals(s)) {
                            saveResponseBody = true;
                            if (!command[i + 1].startsWith("-")) {
                                nameForSaveResponseBody = command[i + 1];
                            }
                        }

                        //get form date
                        if ("--data".equals(s) || "-d".equals(s)) {
                            if (method.equals("GET")) {
                                //System.out.println("GET method cant send form data.");
                                printError();
                            } else {
                                getFormData(i + 1);
                            }
                        }

                        //get json
                        if ("--json".equals(s) || "-j".equals(s)) {
                            isJson = true;
                            getJson(i + 1);
                        }

                        //get upload binary
                        if ("--upload".equals(s)) {
                            upload = true;
                            uploadAddress = command[i + 1];
                        }
                    } catch (IndexOutOfBoundsException ignored) {}
                }

                //make request
                makeRequest();

                //save request
                for (int i = 1; i < command.length; i++) {
                    String s = command[i];
                    if ("--save".equals(s) || "-S".equals(s)) {
                        saveRequest();
                    }
                }
            }

            //fire
            else {
                int j = 1;
                while (j < command.length) {
                    if ( command[j].charAt(0) != '-' && 1 <= Integer.parseInt(command[j]) && Integer.parseInt(command[j])<=command.length) {
                        loadRequest(Integer.parseInt(command[j]));
                    }
                    else if (command[j].charAt(0) == '-'){
                        System.out.println("Invalid fire pattern.");
                        printError();
                    }
                    else {
                        System.out.println(command[j] + " is invalid number.");
                        printError();
                    }
                    j++;
                }
            }
        }
    }

    /**
     * get request header from command.
     *
     * @param index index of args
     */
    private static void getRequestHeader(int index) {
        if (!command[index].contains(":") && !command[index].equals(""))
        {
            //System.out.println("Invalid header request pattern.");
            printError();
        }
        String input = command[index];
        String[] lines = input.split(";");
        for (String line : lines) {
            String[] keyValue = line.split(":");
            requestHeader.put(keyValue[0], keyValue[1]);
        }
    }

    /**
     * get form data from command.
     *
     * @param index index of args
     */
    private static void getFormData(int index) {
        if (!command[index].contains("=") && !command[index].equals(""))
        {
            //System.out.println("Invalid form data request pattern.");
            printError();
        }
        String input = command[index];
        String[] lines = input.split("&");
        for (String line : lines) {
            String[] keyValue = line.split("=");
            formData.put(keyValue[0], keyValue[1]);
        }
    }

    /**
     * get json from command.
     *
     * @param index index of args
     */
    private static void getJson(int index) {
        json = command[index];
    }

    /**
     * make request.
     *
     * The method makes request.
     */
    private static void makeRequest() throws IOException {
        Request request = new Request(url, method, requestHeader, showResponseHeader,
            followRedirect, saveResponseBody, nameForSaveResponseBody,
            formData, json, isJson, uploadAddress, upload);
        requests.add(request);
        recentRequest = request;
    }

    /**
     * save request.
     *
     * The method saves request.
     */
    private static void saveRequest() {
        try (FileOutputStream fs = new FileOutputStream(REQ_PATH )){
            ObjectOutputStream  os = new ObjectOutputStream(fs);
            os.writeObject(requests);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * load request.
     *
     * The method loads all of request.
     */
    private static void loadRequests() {
        try (FileInputStream fs = new FileInputStream(REQ_PATH)){
            ObjectInputStream os = new ObjectInputStream(fs);
            requests = (ArrayList<Request>)os.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("cannot read");
            e.printStackTrace();
        }
    }

    /**
     * load request.
     *
     * The method load request.
     * @param index index of request
     */
    private static void loadRequest(int index) throws IOException {
        try {
            System.out.println(index + "-");
            requests.get(index - 1).sendRequest();
        }catch (IndexOutOfBoundsException e) {
            printError();
        }
    }

    /**
     * show help.
     *
     */
    private static void help() {
        System.out.println("First argument should be \"URL\" or list or fire or --help/-h.");
        System.out.println("\t\"URL\" \t\t\t\t\t\t\t\t\t\t|first command");
        System.out.println("\t--upload \"absolute address of file\" \t\t|Upload binary");
        System.out.println("-d,\t--data \"key1=value1&key2=value2&...\" \t\t|Form data");
        System.out.println("-f\t\t\t\t\t\t\t\t\t\t\t\t|Follow redirect = on");
        System.out.println("-H,\t--headers \"key1:value1;key2:value2;...\" \t|Header request");
        System.out.println("-h,\t--help\t\t\t\t\t\t\t\t\t\t|Show help for commands");
        System.out.println("-i\t\t\t\t\t\t\t\t\t\t\t\t|Show header response");
        System.out.println("-j,\t--json \"json\" \t\t\t\t\t\t\t\t|JSON");
        System.out.println("-M,\t--method \"type of method\" \t\t\t\t\t|Method of HttpURLConnection");
        System.out.println("-O,\t--output \"name of file\" \t\t\t\t\t|Save response body");
        System.out.println("-S,\t--save\t\t\t\t\t\t\t\t\t\t|Save request");
        System.out.println("\tlist\t\t\t\t\t\t\t\t\t\t|List of saved request");
        System.out.println("\tfire \"index of list\" \"index of list\" ...\t|Selection of requests for loading");
        System.exit(0);
    }

    /**
     * print list of requests.
     *
     */
    private static void printList() {
        int i = 1;
        for(Request temp: requests) {
            System.out.print(i + " . ");
            System.out.println(temp.toString());
            i++;
        }
        System.exit(0);
    }

    /**
     * print error.
     *
     */
    private static void printError() {
        //System.out.println("The entered command is not recognized, for more information enter '--help' or '-h'.");
        //System.exit(0);
    }
}
