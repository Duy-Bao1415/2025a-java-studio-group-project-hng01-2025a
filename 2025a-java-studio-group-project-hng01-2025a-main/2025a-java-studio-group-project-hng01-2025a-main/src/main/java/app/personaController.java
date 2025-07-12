package app;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import app.dataLogic.*;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class personaController implements Handler {
    // Add logger
    private static final Logger logger = LoggerFactory.getLogger(personaController.class);
    public static final String URL = "/persona";
    private static final String TEMPLATE = ("persona.html");

    
    // Add this enhanced logging to personaController.java to see exactly what paths are being used

    @Override
    public void handle(Context context) throws Exception {
        logger.info("Handling request for personas page");
        Map<String, Object> model = new HashMap<String, Object>();
        Level_1 level_1 = new Level_1();
        
        ArrayList<personaClass> personasList = new ArrayList<personaClass>();
        personasList = level_1.getPersona();
        
        // Enhanced logging for image paths
        logger.info("Fetched {} personas from database", personasList.size());
        for (personaClass persona : personasList) {
            logger.info("Persona: {} with image path: {}", persona.getPersonaName(), persona.getPersonaImg());
            // Create full path for debugging
            String fullPath = "/images/" + persona.getPersonaImg();
            logger.info("Full image path being used: {}", fullPath);
        }
        
        model.put("personas", personasList);
        
        // Render the template with the model data
        context.render(TEMPLATE, model);
}
}