package app;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class App {
    // Log for Debugging
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // Configure Thymeleaf with caching disabled 
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false); 
        templateEngine.setTemplateResolver(resolver);
        
        // Configure Javalin and Thymeleaf
        JavalinThymeleaf.init(templateEngine);

        // Create/Configure Javalin app
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/static");
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
            // Log requests
            config.requestLogger.http((ctx, ms) -> {
                logger.info("{} {} {} - {}ms", ctx.method(), ctx.path(), ctx.status(), ms);
            });
        }).start(7000);

        // Add exception handling with logging process
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Error occurred: ", e);
            ctx.status(500);
            Map<String, Object> model = new HashMap<>();
            model.put("message", e.getMessage());
            model.put("stackTrace", e.toString());
            ctx.render("error.html", model);
        });

        // Register controllers with their own handlers
        app.get(indexController.URL, new indexController()); // Use indexController instead of simple route
        app.get(agehealthController.URL, new agehealthController());
        app.get(school_educationController.URL, new school_educationController());
        app.get(personaController.URL, new personaController());
        app.get(missionstatementController.URL, new missionstatementController()); // Added mission statement controller
        
        app.get(gapanalysis_Controller.URL, new gapanalysis_Controller());
        app.post(gapanalysis_Controller.URL, new gapanalysis_Controller());

        logger.info("Registered indexController at {}", indexController.URL);
        logger.info("Registered personaController at {}", personaController.URL);
        logger.info("Registered missionstatementController at {}", missionstatementController.URL);

        // Routes for other pages without special controllers
        String[] pages = {
             "how2use",
            "vision", "contact", "signup", "signin"
            // Removed "missionstatement" from this list since it now uses missionstatementController
        };

        for (String page : pages) {
            app.get("/" + page, ctx -> {
                logger.info("Accessing page: {}", page);
                ctx.render(page + ".html");
            });
        }

        // 404 handler
        app.error(404, ctx -> {
            logger.warn("Page not found: {}", ctx.path());
            ctx.render("404.html");
        });

        logger.info("Server started on port 7000");
    }
}