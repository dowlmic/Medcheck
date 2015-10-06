/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medcheck;

import java.io.*;
import java.util.*;
import org.json.*;

/**
 *
 * @author michelledowling
 */
public class Medcheck {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileLocation = "meddata.json";
        File jsonTxt = new File(fileLocation);
        
        String jsonString = "";
        try {
            Scanner fileScanner = new Scanner(jsonTxt);
            boolean isFirstLine = true;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!isFirstLine) {
                    jsonString += " ";
                }
                isFirstLine = false;
                jsonString += line;
            }
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        JSONObject obj = new JSONObject(jsonString);
    }
    
}
