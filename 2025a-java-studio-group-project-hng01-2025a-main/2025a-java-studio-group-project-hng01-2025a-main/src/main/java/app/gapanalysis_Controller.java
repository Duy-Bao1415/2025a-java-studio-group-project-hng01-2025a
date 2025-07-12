package app;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import app.dataLogic.*;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class gapanalysis_Controller implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(gapanalysis_Controller.class);
    public static final String URL = "/gapanalysis";
    private static final String TEMPLATE = "gapanalysis.html";

    @Override
    public void handle(Context context) throws Exception {
        logger.info("Handling request for gapanalysis page - Method: {}", context.method());
        Map<String, Object> model = new HashMap<String, Object>();

        try {
            // Set page title
            model.put("title", "Gap Analysis");

            // Get LGA codes for dropdown
            Level_3 level_3 = new Level_3();
            model.put("lgaCodes", level_3.getLGACodes());

            // Get form parameters
            String chosenlga_Code = context.formParam("targetLgacode");
            String chosenage_min = context.formParam("targetage_min");
            String chosenage_max = context.formParam("targetage_max");
            String selectedDataset = context.formParam("dataset");

            // Get filter parameters
            List<String> selectedSexes = new ArrayList<>();
            if (context.formParam("sex") != null) {
                selectedSexes = context.formParams("sex");
            }

            List<String> selectedStatuses = new ArrayList<>();
            if (context.formParam("status") != null) {
                selectedStatuses = context.formParams("status");
            }

            // Dataset-specific filters
            List<String> selectedConditions = new ArrayList<>();
            if (context.formParam("condition") != null) {
                selectedConditions = context.formParams("condition");
            }

            List<String> selectedEducationLevels = new ArrayList<>();
            if (context.formParam("education_level") != null) {
                selectedEducationLevels = context.formParams("education_level");
            }

            List<String> selectedCompletions = new ArrayList<>();
            if (context.formParam("completion") != null) {
                selectedCompletions = context.formParams("completion");
            }

            List<String> selectedYears = new ArrayList<>();
            if (context.formParam("year") != null) {
                selectedYears = context.formParams("year");
            }

            // Debug logging
            logger.info("Form parameters - Dataset: {}, LGA Code: {}, Age Min: {}, Age Max: {}", 
                       selectedDataset, chosenlga_Code, chosenage_min, chosenage_max);
            logger.info("Selected filters - Sexes: {}, Statuses: {}", 
                       selectedSexes, selectedStatuses);
            logger.info("Dataset-specific filters - Conditions: {}, Education Levels: {}, Completions: {}", 
                       selectedConditions, selectedEducationLevels, selectedCompletions);

            // Initialize gap analysis data
            ArrayList<gapanalysisClass> gaplist = new ArrayList<gapanalysisClass>();
            
            // Only perform analysis if parameters are provided
            if (chosenlga_Code != null && !chosenlga_Code.trim().isEmpty() && 
                selectedDataset != null && !selectedDataset.trim().isEmpty()) {
                try {
                    logger.info("Attempting to retrieve data for dataset: {}", selectedDataset);
                    
                    switch (selectedDataset.toLowerCase()) {
                        case "education":
                            gaplist = level_3.getEducationData(
                                chosenlga_Code.trim(),
                                selectedStatuses,
                                selectedSexes,
                                selectedEducationLevels
                            );
                            break;
                        case "health":
                            gaplist = level_3.getHealthData(
                                chosenlga_Code.trim(),
                                selectedStatuses,
                                selectedSexes,
                                selectedConditions
                            );
                            break;
                        case "school":
                            gaplist = level_3.getSchoolCompletionData(
                                chosenlga_Code.trim(),
                                selectedStatuses,
                                selectedSexes,
                                selectedCompletions
                            );
                            break;
                        case "population":
                            if (chosenage_min != null && !chosenage_min.trim().isEmpty() && 
                                chosenage_max != null && !chosenage_max.trim().isEmpty()) {
                                gaplist = level_3.getGapAnalysis(
                                    chosenlga_Code.trim(),
                                    chosenage_min.trim(),
                                    chosenage_max.trim(),
                                    selectedSexes,
                                    selectedStatuses,
                                    selectedYears, selectedYears
                                );
                            } else {
                                logger.warn("Age parameters are missing or empty");
                                model.put("error", "Please provide both minimum and maximum age values.");
                            }
                            break;
                        default:
                            logger.warn("Invalid dataset selected: {}", selectedDataset);
                            model.put("error", "Invalid dataset selected.");
                            break;
                    }
                    logger.info("Successfully retrieved {} records", gaplist.size());
                    
                    // Add search parameters to model for display
                    model.put("searchPerformed", true);
                    model.put("selectedDataset", selectedDataset);
                    model.put("selectedLgaCode", chosenlga_Code);
                    model.put("selectedAgeMin", chosenage_min);
                    model.put("selectedAgeMax", chosenage_max);
                    model.put("selectedSexes", selectedSexes);
                    model.put("selectedStatuses", selectedStatuses);
                    model.put("selectedConditions", selectedConditions);
                    model.put("selectedEducationLevels", selectedEducationLevels);
                    model.put("selectedCompletions", selectedCompletions);
                    model.put("selectedYears", selectedYears);
                    
                } catch (Exception e) {
                    logger.error("Error retrieving data: {}", e.getMessage(), e);
                    model.put("error", "Error retrieving data: " + e.getMessage());
                    model.put("searchPerformed", true);
                    model.put("selectedDataset", selectedDataset);
                    model.put("selectedLgaCode", chosenlga_Code);
                    model.put("selectedAgeMin", chosenage_min);
                    model.put("selectedAgeMax", chosenage_max);
                    model.put("selectedSexes", selectedSexes);
                    model.put("selectedStatuses", selectedStatuses);
                    model.put("selectedConditions", selectedConditions);
                    model.put("selectedEducationLevels", selectedEducationLevels);
                    model.put("selectedCompletions", selectedCompletions);
                    model.put("selectedYears", selectedYears);
                }
            } else {
                logger.info("No LGA code or dataset provided, showing empty form");
                model.put("searchPerformed", false);
            }

            // Add gap analysis data to model
            model.put("gap", gaplist);
            model.put("hasResults", !gaplist.isEmpty());

            logger.info("About to render template: {}", TEMPLATE);
            // Render the correct template
            context.render(TEMPLATE, model);
            
        } catch (Exception overallException) {
            logger.error("Overall error in gapanalysisController: {}", overallException.getMessage(), overallException);
            // Create a minimal model for error display
            model.clear();
            model.put("title", "Gap Analysis - Error");
            model.put("error", "An unexpected error occurred: " + overallException.getMessage());
            model.put("searchPerformed", false);
            model.put("gap", new ArrayList<gapanalysisClass>());
            model.put("hasResults", false);
            
            try {
                context.render(TEMPLATE, model);
            } catch (Exception renderException) {
                logger.error("Failed to render error page: {}", renderException.getMessage(), renderException);
                // Fallback to simple error response
                context.status(500);
                context.result("Error in Gap Analysis: " + overallException.getMessage());
            }
        }
    }
}
