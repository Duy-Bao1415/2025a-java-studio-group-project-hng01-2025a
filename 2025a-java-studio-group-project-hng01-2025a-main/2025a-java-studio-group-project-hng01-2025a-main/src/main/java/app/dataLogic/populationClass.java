package app.dataLogic;

public class populationClass {
    private String lga_code;
    private String lga_year;
    private String indi_status;
    private String sex;
    private String age_category;
    private String count;
    private String age_min;
    private String age_max;


    public populationClass(String lga_code, String lga_year, String indi_status, String sex, String age_category, String count, String age_min, String age_max) {
        this.lga_code = lga_code;
        this.lga_year = lga_year;
        this.indi_status = indi_status;
        this.sex = sex;
        this.age_category = age_category;
        this.count = count;
        this.age_min = age_min;
        this.age_max = age_max;
    }
    public String getLga_code() {
        return lga_code;
    }
    public String getLga_year() {
        return lga_year;
    }
    public String getIndi_status() {
        return indi_status;
    }
    public String getSex() {
        return sex;
    }
    public String getAge_category() {
        return age_category;
    }
    public String getCount() {
        return count;
    }
    public String getAge_min() {
        return age_min;
    }
    public String getAge_max() {
        return age_max;
    }
    
}
