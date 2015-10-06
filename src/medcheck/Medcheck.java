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
        
        PrintWriter csvWriter;
        try {
             csvWriter = new PrintWriter("meddata.csv", "UTF-8");
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        String csvString = convertToCsvString(convertToDrugArray(jsonString));
        csvWriter.print(csvString);
        csvWriter.close();
    }
    
    private static String[] convertToStringArray(JSONArray jArray) {
        String[] array = new String[jArray.length()];
        for (int i = 0; i < jArray.length(); i++) {
            array[i] = jArray.getString(i);
        }
        return array;
    }
    
    private static Drug[] convertToDrugArray(String jsonFile) {
        JSONObject obj = new JSONObject(jsonFile);
        JSONArray drugs = obj.getJSONArray("result");
        
        int drugCount = drugs.length();
        System.out.println(drugCount);
        Drug[] drugArray = new Drug[drugCount];
        for (int i = 0; i < drugCount; i++) {
            JSONObject drug = drugs.getJSONObject(i);
            String drugName = drug.getString("name");
            String[] drugClass = convertToStringArray(drug.getJSONArray("/medicine/drug/drug_class"));
            String[] physiologicAffects = convertToStringArray(drug.getJSONArray("/medicine/drug/physiologic_effect"));
            String[] activeIngredients = convertToStringArray(drug.getJSONArray("/medicine/drug/active_moieties"));
            String[] formulations = convertToStringArray(drug.getJSONArray("/medicine/drug/marketed_formulations"));
            String[] brands1 = convertToStringArray(drug.getJSONArray("/medicine/drug/brands"));
            String[] brands2 = convertToStringArray(drug.getJSONArray("/medicine/drug/canonical_brands"));
            String[] partOfDrugs = convertToStringArray(drug.getJSONArray("/medicine/drug/part_of_compound_drug"));
            
            String[] brands = new String[brands1.length + brands2.length];
            System.arraycopy(brands1, 0, brands, 0, brands1.length);
            System.arraycopy(brands2, 0, brands, brands1.length, brands2.length);
            
            Drug drugObj = new Drug(drugName, drugClass, physiologicAffects,
                activeIngredients, formulations, brands, partOfDrugs);
            drugArray[i] = drugObj;
        }
        
        return drugArray;
    }
    
    public static String convertToArrayString(String[] array) {
        String deliminator = "**";
        String arrayString = "";
        for (String str : array) {
            arrayString += str + deliminator;
        }
        
        if (arrayString.length() > 0) {
            return arrayString.substring(0, arrayString.length()-1);
        }
        else {
            return "";
        }
    }
    
    public static String convertToCsvString(Drug[] drugArray) {
        String csvString = "Name,Class,Physiologic Affects,Active Ingredients,"
                + "Formulations,Brands,Part of Drugs\n";
        for (Drug drug : drugArray) {
            String drugClass = convertToArrayString(drug.getDrugClass());
            String physiologicAffects = convertToArrayString(drug.getPhysiologicAffects());
            String activeIngredients = convertToArrayString(drug.getActiveIngredients());
            String formulations = convertToArrayString(drug.getFormulations());
            String brands = convertToArrayString(drug.getBrands());
            String partOfDrugs = convertToArrayString(drug.getPartOfDrugs());
            
            csvString += drug.getName() + "," + drugClass + "," +
                    physiologicAffects + "," + activeIngredients + "," +
                    formulations + "," + brands + "," + partOfDrugs + "\n";
        }
        
        return csvString;
    }
}
