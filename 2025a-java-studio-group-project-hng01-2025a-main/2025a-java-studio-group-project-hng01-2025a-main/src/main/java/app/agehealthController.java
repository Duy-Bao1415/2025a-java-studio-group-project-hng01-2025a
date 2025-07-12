package app;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import app.dataLogic.*;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class agehealthController implements Handler {
    public static final String URL = "/agehealth";
    private static final String TEMPLATE = ("agehealth.html");
    private static final Logger logger = LoggerFactory.getLogger(agehealthController.class);

    @Override
    public void handle(Context context) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Level_2 level_2 = new Level_2();
        
        // Get sort parameters from the request
        String healthSortOrder = context.queryParam("healthSortOrder");
        String ageSortOrder = context.queryParam("ageSortOrder");
        
        // Log the sort parameters
        logger.info("Health sort order: {}", healthSortOrder);
        logger.info("Age sort order: {}", ageSortOrder);
        
        // Get health data with sort order if specified
        ArrayList<healthClass> healths;
        if (healthSortOrder != null && !healthSortOrder.isEmpty()) {
            healths = level_2.getallhealth_order(healthSortOrder);
        } else {
            healths = level_2.getAllHealth();
        }
        
        // Get age data (we'll need to implement sorting for this)
        ArrayList<ageClass> ages = level_2.getPopulationAge();
        
        // Sort age data if requested
        if (ageSortOrder != null && !ageSortOrder.isEmpty()) {
            // This will be implemented in Level_2 class
            ages = level_2.getPopulationAge_order(ageSortOrder);
        }

        // Add data to the model
        model.put("healths", healths);
        model.put("ages", ages);
        
        // Add sort orders to the model to track selected options
        model.put("healthSortOrder", healthSortOrder);
        model.put("ageSortOrder", ageSortOrder);

        // Log the data size
        logger.info("Number of health records: {}", healths.size());
        logger.info("Number of age records: {}", ages.size());

        // Render the template
        context.render(TEMPLATE, model);
    }
}