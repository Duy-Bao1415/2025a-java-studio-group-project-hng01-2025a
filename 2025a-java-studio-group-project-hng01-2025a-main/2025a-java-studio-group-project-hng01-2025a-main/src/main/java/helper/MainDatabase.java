package helper;


public class MainDatabase {
    public static void main (String[] args) {
        Education_completion.process2016();
        Education_completion.process2021();
        Education_level.process2016();
        Education_level.process2021();
        Health.process();
        Population.process2016();
        Population.process2021();
        LGA.process2016();
         LGA.process2021();
}
}
