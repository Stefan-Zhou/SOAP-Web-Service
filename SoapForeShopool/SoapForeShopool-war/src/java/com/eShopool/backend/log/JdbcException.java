package com.eShopool.backend.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The class provides operations of writing and reading log file.
 * The log file is created for storing exception content.
 * 
 * @author Group 10
 * @since 30/10/2019
 */
public class JdbcException {
    
    /**
     * Store the content to txt file.
     * 
     * @param content the message going to be stored in txt file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void writeException (String content) throws FileNotFoundException, IOException {
        String path = "C:/Users/Administrator/Documents/NetBeansProjects/SoapForeShopool/SoapForeShopool-war/web/exception_log.txt";
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }
    
    /**
     * Read the conent stored in the txt file.
     * 
     * @return the content stored in the txt file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static String readException () throws FileNotFoundException, IOException {
        String path = "exception_log.txt";
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        String content = "";
        while ((line = bufferedReader.readLine()) != null) {
            content = line;
        }
        fileInputStream.close();
        return content;
    }
    
}
