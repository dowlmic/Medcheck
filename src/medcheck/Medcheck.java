/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medcheck;

import java.io.*;
import static java.lang.System.*;
import java.util.*;
import org.json.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 *
 * @author michelledowling
 */
public class Medcheck {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String jsonFileLocation = "meddata.json";
        File jsonTxt = new File(jsonFileLocation);
        
        String xmlFileLocation = "XML Files";
        File xmlDirectory = new File(xmlFileLocation);
        
        String jsonString = getJSONString(jsonTxt);
        
        PrintWriter csvWriter;
        try {
             csvWriter = new PrintWriter("meddata.csv", "UTF-8");
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        Drug[] csvArray = convertToDrugArrayFromJSON(jsonString);
        Drug[] xmlArray = convertToDrugArrayFromXML(xmlDirectory);
        String csvString = convertToCsvString(csvArray, xmlArray);
        csvWriter.print(csvString);
        csvWriter.close();
    }
    
    private static String getJSONString(File jsonFile) {
        String jsonString = "";
        try {
            Scanner fileScanner = new Scanner(jsonFile);
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
            exit(1);
        }
        
        return jsonString;
    }
    
    private static Drug[] convertToDrugArrayFromJSON(String jsonFile) {
        JSONObject obj = new JSONObject(jsonFile);
        JSONArray drugs = obj.getJSONArray("result");
        
        int drugCount = drugs.length();
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
    
        private static String[] convertToStringArray(JSONArray jArray) {
        String[] array = new String[jArray.length()];
        for (int i = 0; i < jArray.length(); i++) {
            array[i] = jArray.getString(i);
        }
        return array;
    }
    
    private static Drug[] convertToDrugArrayFromXML(File xmlDirectory) {        
        File[] xmlFiles = xmlDirectory.listFiles();
        Drug[] drugs = new Drug[xmlFiles.length];
        
        System.out.println(xmlFiles.length);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
                    
            for (int i = 0; i < xmlFiles.length; i++) {
                db.parse(xmlFiles[i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            exit(1);
        }
        
        return drugs;
    }
    
    public static String convertToCsvString(Drug[] csvArray, Drug[] xmlArray) {
        String csvString = "Name,Class,Physiologic Affects,Active Ingredients,"
                + "Formulations,Brands,Part of Drugs\n";
        for (Drug drug : csvArray) {
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
}
