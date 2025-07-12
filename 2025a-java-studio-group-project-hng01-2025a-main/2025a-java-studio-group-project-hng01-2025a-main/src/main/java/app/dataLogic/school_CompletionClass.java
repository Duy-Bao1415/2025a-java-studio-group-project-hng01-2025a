package app.dataLogic;

public class school_CompletionClass {
    private String name;
    private int lga_code;
    private String indi_status;
    private String category;
    private int total_counts;
    private double percentage;
    private int rank;
    
    public school_CompletionClass(String name, int lga_code, String indi_status, String category, int total_counts, double percentage) {
        this.name = name;
        this.lga_code = lga_code;
        this.indi_status = indi_status;
        this.category = category;
        this.total_counts = total_counts;
        this.percentage = percentage;
        this.rank = 0; // Default rank
    }

    public school_CompletionClass(String name, int lga_code, String indi_status, String category, int total_counts, double percentage, int rank) {
        this.name = name;
        this.lga_code = lga_code;
        this.indi_status = indi_status;
        this.category = category;
        this.total_counts = total_counts;
        this.percentage = percentage;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }   
    public int getLga_code() {
        return lga_code;
    }               
    public String getIndi_status() {
        return indi_status;
    }
    public String getCategory() {
        return category;
    }   
    public int getTotal_counts() {
        return total_counts;
    }
    public double getPercentage() {
        return percentage;
    }
    public int getRank() {
        return rank;
    }
}
