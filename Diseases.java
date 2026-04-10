import java.util.*; 

public class Diseases {
    public double infectionChance; 
    public double fatalityRate; 
    public boolean chronic; 

    public static Map<String, Diseases> diseases = new HashMap<>(); 

    public Diseases(double infectionChance, double fatalityRate, boolean chronic) {
        this.infectionChance = infectionChance; 
        this.fatalityRate = fatalityRate; 
        this.chronic = chronic; 
    } 



    static {
        String[] diseaseNames = {
            "asthma", "pneumonia", "bronchitis", "common_cold", "influenza", "covid19", 
            "sinusitis", "strep_throat", "tonsillitis", "laryngitis", "whooping_cough", 
            "measles", "mumps", "rubella", "chickenpox", "shingles", "meningitis", 
            "encephalitis", "bronchiolitis", "rsv", "tuberculosis", "cholera", "typhoid", 
            "salmonella", "e_coli", "food_poisoning", "gastroenteritis", "norovirus", 
            "rotavirus", "hepatitis_a", "hepatitis_b", "hepatitis_c", "hiv", "aids", 
            "mononucleosis", "adenovirus", "enterovirus", "hand_foot_mouth", "pink_eye", 
            "ear_infection", "diabetes_type1", "diabetes_type2", "hypertension", 
            "coronary_artery_disease", "heart_attack", "stroke", "kidney_failure", 
            "liver_failure", "copd", "arthritis", "rheumatoid_arthritis", "lupus", 
            "multiple_sclerosis", "alzheimers", "parkinsons", "epilepsy", "depression", 
            "anxiety_disorder", "bipolar_disorder", "schizophrenia", "skin_cancer", 
            "lung_cancer", "breast_cancer", "colon_cancer", "pancreatic_cancer", 
            "brain_cancer", "leukemia", "lymphoma", "sepsis", "acute_respiratory_distress", 
            "multi_organ_failure", "heatstroke", "hypothermia"
        }; 
        double[] infectionChances = {
            0.02, 0.03, 0.05, 0.30, 0.10, 0.08, 0.08, 0.06, 0.05, 0.04, 0.04, 
            0.07, 0.05, 0.04, 0.06, 0.02, 0.01, 0.005, 0.04, 0.07, 0.02, 0.03, 
            0.02, 0.08, 0.07, 0.12, 0.10, 0.15, 0.12, 0.02, 0.01, 0.01, 0.002, 
            0.001, 0.04, 0.05, 0.05, 0.05, 0.06, 0.06, 0.002, 0.01, 0.02, 0.01, 
            0.005, 0.005, 0.003, 0.002, 0.01, 0.03, 0.01, 0.002, 0.001, 0.003, 
            0.002, 0.005, 0.05, 0.08, 0.01, 0.005, 0.01, 0.005, 0.01, 0.008, 
            0.002, 0.002, 0.003, 0.003, 0.01, 0.005, 0.003, 0.01, 0.01
        };

        double[] fatalityRates = {
            0.0005, 0.05, 0.002, 0.00001, 0.001, 0.01, 0.0001, 0.0005, 0.0002, 
            0.0001, 0.01, 0.002, 0.0001, 0.0002, 0.0002, 0.001, 0.10, 0.15, 
            0.002, 0.002, 0.15, 0.01, 0.02, 0.001, 0.002, 0.001, 0.001, 0.0005, 
            0.002, 0.002, 0.02, 0.03, 0.40, 0.80, 0.0005, 0.001, 0.001, 0.0001, 
            0.00001, 0.0001, 0.02, 0.05, 0.01, 0.20, 0.30, 0.25, 0.40, 0.50, 
            0.20, 0.001, 0.02, 0.05, 0.03, 0.60, 0.20, 0.02, 0.01, 0.001, 0.01, 
            0.02, 0.15, 0.70, 0.20, 0.30, 0.80, 0.60, 0.40, 0.35, 0.25, 0.35, 
            0.60, 0.10, 0.10
        }; 

        boolean[] chronic = {
            false, false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, true, false,
            true, true, false, true, false, false,
            false, false, false, false, false, false,
            false, false, true, true, true,
            true, true, false, false, false, false,
            false, false, true, false,
            true, true, true, true, true, true,
            true, true, true, true, true,
            true, true, true, true, true, true,
            false, false, true, true, true,
            true, true, true, true, true, true,
            true, true, true, true, true
        };
        
        for (int i = 0; i < diseaseNames.length; i++) {
            diseases.put(
                diseaseNames[i],
                new Diseases(infectionChances[i], fatalityRates[i], chronic[i])
            );
        }
    } 
}