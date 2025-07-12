package app.dataLogic;

// This class represents a team member (persona) for the project.
// It stores student ID, image path, full name, and gender for each member.

public class memberClass {
    // Fields for student ID, image filename/path, full name, and gender
    private String sID;
    private String mem_img;
    private String full_name;
    private String gender;

    // Constructor to initialize all fields
    public memberClass(String sID, String mem_img, String full_name, String gender) {
        this.sID = sID;
        this.mem_img = mem_img;
        this.full_name = full_name;
        this.gender = gender;
    }

    // Getter for student ID
    public String getsID() {
        return sID;
    }
    // Getter for member image path/filename
    public String getMem_img() {
        return mem_img;
    }
    // Getter for full name
    public String getFull_name() {
        return full_name;
    }
    // Getter for gender
    public String getGender() {
        return gender;
    }
}