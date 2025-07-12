package app.dataLogic;

// This class represents a health data record for a specific LGA and category.
// It is used to store and transfer health statistics for the Age & Health page.

public class healthClass {
    // Fields for LGA name, code, indigenous status, health category, total count, percentage, and rank
    private String name;
    private int lga_code;
    private String indi_status;
    private String category;
    private int total_counts;
    private double percentage;
    private int rank;
    
    // Constructor without rank (rank defaults to 0)
    public healthClass(String name, int lga_code, String indi_status, String category, int total_counts, double percentage) {
        this.name = name;
        this.lga_code = lga_code;
        this.indi_status = indi_status;
        this.category = category;
        this.total_counts = total_counts;
        this.percentage = percentage;
        this.rank = 0; // Default rank
    }

    // Constructor with rank
    public healthClass(String name, int lga_code, String indi_status, String category, int total_counts, double percentage, int rank) {
        this.name = name;
        this.lga_code = lga_code;
        this.indi_status = indi_status;
        this.category = category;
        this.total_counts = total_counts;
        this.percentage = percentage;
        this.rank = rank;
    }
    
    // toString method for debugging/logging
    public String toString() {
        return "Health Data [name=" + name + 
               ", lga_code=" + lga_code + 
               ", status=" + indi_status + 
               ", category=" + category + 
               ", count=" + total_counts + 
               ", percentage=" + percentage + 
               ", rank=" + rank + "]";
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
