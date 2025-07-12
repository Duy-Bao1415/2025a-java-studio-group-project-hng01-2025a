package app;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import app.dataLogic.*;


import io.javalin.http.Context;
import io.javalin.http.Handler;


public class school_educationController implements Handler {
   public static final String URL = "/education";
   private static final String TEMPLATE = ("education.html");


   @Override
   public void handle(Context context) throws Exception {
       String schoolSortOrder = context.queryParam("schoolSortOrder");
       String educationLevelSortOrder = context.queryParam("educationLevelSortOrder");


       // Initialize data manager
       Level_2 level2 = new Level_2();


       // Get data based on sort order
       ArrayList<school_CompletionClass> schoolCompletions;
       ArrayList<Education_levelClass> educationLevels;


       // Apply sorting for school completion data if sort parameter exists
       if (schoolSortOrder != null && !schoolSortOrder.isEmpty()) {
           // Use the sorting method
           schoolCompletions = level2.getSchoolRaw_order(schoolSortOrder);


       } else {
           // Use default (unsorted) method
           schoolCompletions = level2.getSchoolRaw();
       }


       // Apply sorting for education level data if sort parameter exists
       if (educationLevelSortOrder != null && !educationLevelSortOrder.isEmpty()) {
           // Use the sorting method
           educationLevels = level2.getEducationLevelRaw_order(educationLevelSortOrder);


       } else {
           // Use default (unsorted) method
           educationLevels = level2.getEducationLevelRaw();
       }


       // Create model with data
       Map<String, Object> model = new HashMap<>();
       model.put("school_completion", schoolCompletions);
       model.put("education_level", educationLevels);


       // Add sort parameters to model for UI state
       model.put("schoolSortOrder", schoolSortOrder);
       model.put("educationLevelSortOrder", educationLevelSortOrder);


       // Render template with model
       context.render("education.html", model);
   }
}
