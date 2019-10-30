package com.eShopool.backend.log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Send request to another SOAP server to get the content of exception log.
 * 
 * @author Group 10
 * @since 30/10/2019
 */
public class LogRequest {
    
    /**
     * Send request to another SOAP server to get the content of exception log.
     * 
     * @return the content of another SOAP server's exception log
     */
    public static String request() {
    try {
	//Web service address
        URL url = new URL("http://192.168.0.77:8080/SoapForeShopool-war/SoapService");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //Whether there are input parameters
        conn.setDoInput(true);
        //Whether there are output parameters
        conn.setDoOutput(true);
        //Post request
        conn.setRequestMethod("POST");
        //Set the request header (note that it must be in XML format)
        conn.setRequestProperty("content-type", "text/xml;charset=utf-8");
        //Construct the request body to conform to the soap specification (most important)
        String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<SOAP-ENV:Header/>"
                + "<S:Body xmlns:ns2=\"http://backend.eShopool.com/\">"
                + "<ns2:readException/>"
                + "</S:Body>"
                + "</S:Envelope>";
        //Get an output stream
        OutputStream out = conn.getOutputStream();
        out.write(requestBody.getBytes());
        //Get server response status code
        int code = conn.getResponseCode();
        StringBuffer sb = new StringBuffer();
        if(code == 200) {
            //Obtain an input stream to read the data of the server response
            InputStream is = conn.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
 
            while((len = is.read(b)) != -1) {
                String s = new String(b,0,len,"utf-8");
                sb.append(s);
            }
            is.close();
        }
        out.close();
        String pattern = "<return>(.*)</return>";
        //Create pattern object
        Pattern r = Pattern.compile(pattern);
        //Create the matcher object
        Matcher m = r.matcher(sb.toString());
        if (m.find())
            return m.group(1);
        else
            return "";
	} catch (Exception e) {
		e.printStackTrace();
        return null;
	}
    }
    
}
