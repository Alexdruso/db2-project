package it.polimi.db2.db2_project.web.application;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import it.polimi.db2.db2_project.web.controllers.GQController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.HashMap;
import java.util.Map;

public class GamifiedQuizApplication {
    private TemplateEngine templateEngine;
    private Map<String, GQController> controllersByURL;

    public GamifiedQuizApplication(final ServletContext context) {
        /**
        super();

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);

        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs(3600000L);
        // Cache is set to true by default. Set to false if you want templates to
        // be automatically updated when modified.
        templateResolver.setCacheable(true);

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);

        this.controllersByURL = new HashMap<>();
        this.controllersByURL.put("/", )**/

    }

    public ITemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }

    public GQController dispatchController(HttpServletRequest request) {
        return null;
    }
}
