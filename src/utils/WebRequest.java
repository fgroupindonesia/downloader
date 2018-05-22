package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created in the early morning @06:19am, 22nd May 2018 in Bandung, Indonesia.
 *
 * @author FGroupIndonesia.com
 */
public class WebRequest {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String GET_METHOD = "GET";
    private final String POST_METHOD = "POST";

    private String method;
    private String destinationURL;

    private String obtainedResult;
    private Map keyValuesData;
    private boolean showLog = false;
    private boolean httpsRequest = false;

    // TURN THE CODE BELOW FOR TESTING PURPOSES
//    public static void main(String[] args) {
//        WebRequest coba = new WebRequest();
//       
//        coba.setDestinationURL("http://localhost/testing/terpanggil.php");
//        coba.addData("nilaiPOST", "29292");
//        coba.setMethod("POST");
//        coba.sendNow();
//        System.out.println("test " + coba.getObtainedResult());
//    }

    public WebRequest() {
        // default values
        this.setMethod("GET");
        keyValuesData = new HashMap();
    }

    public void addData(Object key, Object val) {
        keyValuesData.put(key, val);
    }

    private String compileAllData() {
        StringBuilder dataAll = new StringBuilder();
        String hasilAkhir = null;

        if (!keyValuesData.isEmpty()) {
            // this is the first devider from the url
            // "http://www.google.com/search?q=fgroupindonesia";
            if (this.getMethod() == this.GET_METHOD) {
                // for GET METHOD Only
                dataAll.append("?");
            }
            //System.out.println("terisi");
        }

        for (Object key : keyValuesData.keySet()) {
            try {
                dataAll.append(key);
                dataAll.append("=");
                // value must be no spaces otherwise convert it using URL format
                dataAll.append(URLEncoder.encode((String) keyValuesData.get(key), "utf-8"));
                // value must be devided by ampersand for later key-values entries
                dataAll.append("&");

            } catch (Exception ex) {
                System.err.println("Error while compiling parameter data! " + ex.getMessage());
            }
        }

        // obtaining the String without the last ampersand (deleted)
        hasilAkhir = dataAll.toString();
        if (hasilAkhir.length() != 0) {
            return hasilAkhir.substring(0, hasilAkhir.length() - 1);
        }

        // if no data at all then there's nothing to do
        return "";

    }

    public void sendNow() {
        if (this.getMethod().equalsIgnoreCase(this.GET_METHOD)) {
            sendingGETRequest();
        } else if (this.getMethod().equalsIgnoreCase(this.POST_METHOD)) {
            sendingPOSTRequest();
        } else {
            System.out.println("Failed to send webrequest!");
        }
    }

    private void sendingPostHttp(HttpURLConnection koneksi) {

        try {

            //add request header
            koneksi.setRequestMethod(this.getMethod());
            koneksi.setRequestProperty("User-Agent", USER_AGENT);
            koneksi.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            koneksi.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            koneksi.setRequestProperty("charset", "utf-8");
            koneksi.setUseCaches(false);

            //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            String data = this.compileAllData();
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);

            koneksi.setRequestProperty("Content-Length", Integer.toString(postData.length));

            // Send post request
            koneksi.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(koneksi.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();

            int responseCode = koneksi.getResponseCode();
            if (isShowLog()) {
                System.out.println("\nSending 'POST' request to URL : " + this.getDestinationURL());
                System.out.println("Post parameters : " + data);
                System.out.println("Response Code : " + responseCode);
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(koneksi.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            obtainedResult = response.toString();

            if (isShowLog()) {
                //print result
                System.out.println(obtainedResult);

            }

        } catch (Exception ex) {
            System.out.println("Error when sending secured https post request! " + ex.getMessage());
        }

    }

    private void sendingPostHttps(HttpsURLConnection koneksi) {

        try {

            //add request header
            koneksi.setRequestMethod(this.getMethod());
            koneksi.setRequestProperty("User-Agent", USER_AGENT);
            koneksi.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            String data = this.compileAllData();

            // Send post request
            koneksi.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(koneksi.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            int responseCode = koneksi.getResponseCode();
            if (isShowLog()) {
                System.out.println("\nSending 'POST' request to URL : " + this.getDestinationURL());
                System.out.println("Post parameters : " + data);
                System.out.println("Response Code : " + responseCode);
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(koneksi.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            obtainedResult = response.toString();

            if (isShowLog()) {
                //print result
                System.out.println(obtainedResult);

            }

        } catch (Exception ex) {
            System.out.println("Error when sending normal http post request! " + ex.getMessage());
        }

    }

    private void sendingPOSTRequest() {

        try {
            String url = this.getDestinationURL();
            URL obj = null;
            if (isHttpsRequest()) {
                obj = new URL(null, url, new sun.net.www.protocol.https.Handler());
                //System.out.println("secured");
            } else {
                obj = new URL(url);
                //System.out.println("normal");
            }

            if (isHttpsRequest() == false) {
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                sendingPostHttp(conn);
            } else {
                HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
                sendingPostHttps(conn);
            }

        } catch (Exception ex) {
            System.err.println("Some error when sending POST Request!" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void sendingGETRequest() {

        try {
            String url = this.getDestinationURL() + this.compileAllData();

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(this.getMethod().toUpperCase());

            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            if (isShowLog()) {
                //showing process
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            obtainedResult = response.toString();

            if (isShowLog()) {
                //print result
                System.out.println(obtainedResult);
            }

        } catch (Exception ex) {
            System.err.println("Some error when sending GET Request!" + ex.getMessage());
            //ex.printStackTrace();
        }

    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;

    }

    /**
     * @return the destinationURL
     */
    public String getDestinationURL() {
        return destinationURL;
    }

    /**
     * @param destinationURL the destinationURL to set
     */
    public void setDestinationURL(String destinationURL) {
        this.destinationURL = destinationURL;

        if (destinationURL.contains("https")) {
            this.setHttpsRequest(true);
        }

        if (isShowLog()) {
            if (this.isHttpsRequest()) {
                System.out.println("This is secured (https) request!");
            } else {
                System.out.println("This is normal (http) request!");

            }
        }

    }

    /**
     * @return the obtainedResult
     */
    public String getObtainedResult() {
        return obtainedResult;
    }

    /**
     * @param obtainedResult the obtainedResult to set
     */
    public void setObtainedResult(String obtainedResult) {
        this.obtainedResult = obtainedResult;
    }

    /**
     * @return the keyValuesData
     */
    public Map getKeyValuesData() {
        return keyValuesData;
    }

    /**
     * @return the showLog
     */
    public boolean isShowLog() {
        return showLog;
    }

    /**
     * @param showLog the showLog to set
     */
    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    /**
     * @return the httpsRequest
     */
    public boolean isHttpsRequest() {
        return httpsRequest;
    }

    /**
     * @param httpsRequest the httpsRequest to set
     */
    public void setHttpsRequest(boolean httpsRequest) {
        this.httpsRequest = httpsRequest;
    }

}
