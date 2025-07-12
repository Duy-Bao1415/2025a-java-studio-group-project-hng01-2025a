package app.dataLogic;

// This class represents a single record/result for the gap analysis feature.
// It holds LGA information, demographic breakdown, and statistics for 2016 and 2021.

public class gapanalysisClass {
    // Fields for LGA code, name, sex, indigenous status, totals for 2016/2021, change, status, and category
    private String lga_code;
    private String lga_name;
    private String sex;
    private String indi_status;
    private int total_2016;
    private int total_2021;
    private int change;
    private String status;
    private String category;

    // Constructor (category can be set later)
    public gapanalysisClass(String lga_code, String lga_name, String sex, String indi_status, int total_2016, int total_2021, int change, String status) {
        this.lga_code = lga_code;
        this.lga_name = lga_name;
        this.sex = sex;
        this.indi_status = indi_status;
        this.total_2016 = total_2016;
        this.total_2021 = total_2021;
        this.change = change;
        this.status = status;
    }

    // Getters for all fields (used by Thymeleaf templates and controllers)
    public String getLga_code() {
        return lga_code;
    }
    public String getLga_name() {
        return lga_name;
    }
    public String getSex() {
        return sex;
    }
    public String getIndi_status() {
        return indi_status;
    }
    public int getTotal_2016() {
        return total_2016;
    }
    public int getTotal_2021() {
        return total_2021;
    }
    public int getChange() {
        return change;
    }
    public String getStatus() {
        return status;
    }
    public String getCategory() {
        return category;
    }
    // Setter for category (can be set after object creation)
    public void setCategory(String category) {
        this.category = category;
    }
}
