package app.dataLogic;

// This class represents a user persona for the application.
// It stores all relevant information for a persona, such as name, image, quote, age, description, background, needs, pains, and skills.

public class personaClass {
    // Fields for persona attributes
    private String personaName;
    private String personaImg;
    private String personaQuote;
    private String personaAge;
    private String personaDescription;
    private String personaBackground;
    private String personaNeed1;
    private String personaNeed2;
    private String personaPain1;
    private String personaPain2;
    private String personaSkills;

    // Constructor to initialize all persona fields
    public personaClass(String personaName, String personaImg, String personaQuote, String personaAge, String personaDescription, String personaBackground, String personaNeed1, String personaNeed2, String personaPain1, String personaPain2, String personaSkills) {
        this.personaName = personaName;
        this.personaImg = personaImg;
        this.personaQuote = personaQuote;
        this.personaAge = personaAge;
        this.personaDescription = personaDescription;
        this.personaBackground = personaBackground;
        this.personaNeed1 = personaNeed1;
        this.personaNeed2 = personaNeed2;
        this.personaPain1 = personaPain1;
        this.personaPain2 = personaPain2;
        this.personaSkills = personaSkills;
    }
    // Getters for all persona fields (used by templates and controllers)
    public String getPersonaName() {
        return personaName;
    }
    public String getPersonaImg() {
        return personaImg;
    }
    public String getPersonaQuote() {
        return personaQuote;
    }
    public String getPersonaAge() {
        return personaAge;
    }
    public String getPersonaDescription() {
        return personaDescription;
    }
    public String getPersonaBackground() {
        return personaBackground;
    }
    public String getPersonaNeed1() {
        return personaNeed1;
    }
    public String getPersonaNeed2() {
        return personaNeed2;
    }
    public String getPersonaPain1() {
        return personaPain1;
    }
    public String getPersonaPain2() {
        return personaPain2;
    }
    public String getPersonaSkills() {
        return personaSkills;
    }
}
