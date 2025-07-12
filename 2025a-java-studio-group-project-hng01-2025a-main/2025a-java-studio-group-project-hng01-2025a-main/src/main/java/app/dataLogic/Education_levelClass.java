package app.dataLogic;

// This class represents a record for non-school education level statistics for a specific LGA.
// It is used to store and transfer education data for the Education page.

public class Education_levelClass {
    // Fields for LGA name, code, indigenous status, education category, total count, percentage, and rank
    private String name;
    private int lga_code;
    private String indi_status;
    private String category;
    private int total_counts;
    private double percentage;
    private int rank;
    
    // Constructor without rank (rank defaults to 0)
    public Education_levelClass(String name, int lga_code, String indi_status, String category, int total_counts, double percentage) {
        this.name = name;
        this.lga_code = lga_code;
        this.indi_status = indi_status;
        this.category = category;
        this.total_counts = total_counts;
        this.percentage = percentage;
        this.rank = 0; // Default rank
    }

    // Constructor with rank
    public Education_levelClass(String name, int lga_code, String indi_status, String category, int total_counts, double percentage, int rank) {
        this.name = name;
        this.lga_code = lga_code;
        this.indi_status = indi_status;
        this.category = category;
        this.total_counts = total_counts;
        this.percentage = percentage;
        this.rank = rank;
    }
    
    // Getters for all fields (used by Thymeleaf templates and controllers)
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

