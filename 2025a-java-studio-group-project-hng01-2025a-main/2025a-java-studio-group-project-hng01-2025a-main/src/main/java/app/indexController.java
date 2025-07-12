package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.dataLogic.Level_1;
import app.dataLogic.populationClass;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class indexController implements Handler {
    public static final String URL = "/";
    private static final Logger logger = LoggerFactory.getLogger(indexController.class);

    @Override
    public void handle(Context context) throws Exception {
        logger.info("Processing index page request");
        
        // Create model to pass data to template
        Map<String, Object> model = new HashMap<>();
        
        try {
            // Get population data
            Level_1 level1 = new Level_1();
            ArrayList<populationClass> populations = level1.getPopulation();
            
            // Calculate totals for 2016 and 2021
            long total2016 = 0;
            long total2021 = 0;
            
            // Map to store regional data
            Map<String, Map<String, Object>> regionData = new HashMap<>();
            
            // Initialize region data structure
            String[] regions = {"Western Australia", "Northern Territory", "South Australia", 
                               "Queensland", "New South Wales", "Victoria", "Tasmania"};
            
            for (String region : regions) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", region);
                data.put("pop2016", 0L);
                data.put("pop2021", 0L);
                data.put("rate", "0.00%");
                regionData.put(region.toLowerCase().replace(" ", "-"), data);
            }
            
            // Process population data and group by LGA
            for (populationClass pop : populations) {
                try {
                    long count = Long.parseLong(pop.getCount());
                    String year = pop.getLga_year();
                    String lgaCode = pop.getLga_code();
                    
                    // Map LGA codes to regions (you'll need to create this mapping based on your data)
                    String region = mapLgaToRegion(lgaCode);
                    if (region != null) {
                        String regionKey = region.toLowerCase().replace(" ", "-");
                        Map<String, Object> data = regionData.get(regionKey);
                        
                        if (data != null) {
                            if ("2016".equals(year)) {
                                long current2016 = (Long) data.get("pop2016");
                                data.put("pop2016", current2016 + count);
                            } else if ("2021".equals(year)) {
                                long current2021 = (Long) data.get("pop2021");
                                data.put("pop2021", current2021 + count);
                            }
                        }
                    }
                    
                    // Calculate national totals
                    if ("2016".equals(year)) {
                        total2016 += count;
                    } else if ("2021".equals(year)) {
                        total2021 += count;
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid count value: {}", pop.getCount());
                }
            }
            
            // Calculate growth rates for each region
            for (Map<String, Object> data : regionData.values()) {
                long pop2016 = (Long) data.get("pop2016");
                long pop2021 = (Long) data.get("pop2021");
                
                if (pop2016 > 0) {
                    double growthRate = ((double)(pop2021 - pop2016) / pop2016) * 100;
                    data.put("rate", String.format("%.2f%%", growthRate));
                }
                
                // Format population numbers with commas
                data.put("pop2016Formatted", String.format("%,d", pop2016));
                data.put("pop2021Formatted", String.format("%,d", pop2021));
            }
            
            // Calculate national growth rate
            double growthRate = 0.0;
            if (total2016 > 0) {
                growthRate = ((double)(total2021 - total2016) / total2016) * 100;
            }
            
            // Add data to model
            model.put("population2016", String.format("%,d", total2016));
            model.put("population2021", String.format("%,d", total2021));
            model.put("growthRate", String.format("%.2f%%", growthRate));
            model.put("regionData", regionData);
            
            logger.info("Population 2016: {}, Population 2021: {}, Growth Rate: {}%", 
                       total2016, total2021, String.format("%.2f", growthRate));
            
        } catch (SQLException e) {
            logger.error("Database error while fetching population data", e);
            // Use fallback static data if database fails
            model.put("population2016", "23,353,685");
            model.put("population2021", "25,420,752");
            model.put("growthRate", "8.85%");
            model.put("dataError", "Unable to load current data. Showing estimated figures.");
            
            // Add fallback region data
            Map<String, Map<String, Object>> fallbackRegionData = createFallbackRegionData();
            model.put("regionData", fallbackRegionData);
        }
        
        // Render the template with the model
        context.render("index.html", model);
    }
    
    /**
     * Maps LGA codes to Australian states/territories
     * This is a simplified mapping - you'll need to expand this based on your actual LGA codes
     */
    private String mapLgaToRegion(String lgaCode) {
        // This is a basic mapping - you'll need to complete this based on your actual LGA codes
        // LGA codes typically start with state/territory identifiers
        if (lgaCode == null || lgaCode.length() < 2) {
            return null;
        }
        
        String prefix = lgaCode.substring(0, 1);
        switch (prefix) {
            case "1": return "New South Wales";
            case "2": return "Victoria";
            case "3": return "Queensland";
            case "4": return "South Australia";
            case "5": return "Western Australia";
            case "6": return "Tasmania";
            case "7": return "Northern Territory";
            default: return null;
        }
    }
    
    /**
     * Creates fallback region data when database is unavailable
     */
    private Map<String, Map<String, Object>> createFallbackRegionData() {
        Map<String, Map<String, Object>> fallbackData = new HashMap<>();
        
        // Hardcoded fallback data
        String[][] regionInfo = {
            {"western-australia", "Western Australia", "2467208", "2661203", "7.86%"},
            {"northern-territory", "Northern Territory", "226196", "232658", "2.77%"},
            {"south-australia", "South Australia", "1673572", "1781263", "6.05%"},
            {"queensland", "Queensland", "4690069", "5155809", "9.03%"},
            {"new-south-wales", "New South Wales", "7477418", "8072136", "7.37%"},
            {"victoria", "Victoria", "5918874", "6503230", "8.98%"},
            {"tasmania", "Tasmania", "608952", "557559", "-9.22%"}
        };
        
        for (String[] info : regionInfo) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", info[1]);
            data.put("pop2016", Long.parseLong(info[2]));
            data.put("pop2021", Long.parseLong(info[3]));
            data.put("pop2016Formatted", String.format("%,d", Long.parseLong(info[2])));
            data.put("pop2021Formatted", String.format("%,d", Long.parseLong(info[3])));
            data.put("rate", info[4]);
            fallbackData.put(info[0], data);
        }
        
        return fallbackData;
    }
}