package medcheck;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author michelledowling
 */
public class Drug {
    private String name;
    private String[] drugClass;
    private String[] physiologicAffects;
    private String[] activeIngredients;
    private String[] formulations;
    private String[] brands;
    private String[] partOfDrugs;
    
    public Drug (String name, String[] drugClass, String[] physiologicAffects,
            String[] activeIngredients, String[] formulations, String[] brands,
            String[] partOfDrugs) {
        this.name = name;
        this.drugClass = drugClass;
        this.physiologicAffects = physiologicAffects;
        this.activeIngredients = activeIngredients;
        this.formulations = formulations;
        this.brands = brands;
        this.partOfDrugs = partOfDrugs;
    }
    
    public String getName() {
        return name;
    }
    
    public String[] getDrugClass() {
        return drugClass;
    }
    public String[] getPhysiologicAffects() {
        return physiologicAffects;
    }
    
    public String[] getActiveIngredients() {
        return activeIngredients;
    }
    
    public String[] getFormulations() {
        return formulations;
    }
    
    public String[] getBrands() {
        return brands;
    }
    
    public String[] getPartOfDrugs() {
        return partOfDrugs;
    }
}
