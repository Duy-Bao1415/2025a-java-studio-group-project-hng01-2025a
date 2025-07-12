package app;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import app.dataLogic.*;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class missionstatementController implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(missionstatementController.class);
    public static final String URL = "/missionstatement";
    private static final String TEMPLATE = "missionstatement.html";

    @Override
    public void handle(Context ctx) throws Exception {
        logger.info("Handling request for mission statement page");
        
        // Create model to pass data to template
        Map<String, Object> model = new HashMap<String, Object>();
        Level_1 level_1 = new Level_1();

        // Get member list from database
        ArrayList<memberClass> memberList = level_1.getMember();
        
        // Add member list to model
        model.put("members", memberList);
        
        logger.info("Retrieved {} members from database", memberList.size());
        
        // Render the template with the model data
        ctx.render(TEMPLATE, model);
    }
}