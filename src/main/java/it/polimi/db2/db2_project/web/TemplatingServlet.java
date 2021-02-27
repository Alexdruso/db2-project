package it.polimi.db2.db2_project.web;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class TemplatingServlet extends HttpServlet {

    protected TemplateMode templateMode;
    protected TemplateEngine templateEngine;
    private String templatePath;
    private String pathPrefix;
    private String pathSuffix;


    public TemplatingServlet(String templatePath, TemplateMode templateMode, String pathPrefix, String pathSuffix) {
        System.out.println("Ho chiamato il costruttore!");
        this.templateEngine = new TemplateEngine();
        this.templatePath = templatePath;
        this.templateMode = templateMode;
        this.pathPrefix = pathPrefix;
        this.pathSuffix = pathSuffix;
    }

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setSuffix(pathSuffix);
        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix(pathPrefix);
    }

    protected void processTemplate(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        processTemplate(request, response, null);
    }

    protected void processTemplate(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Map<String, Object> variables) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        ServletContext servletCtx = getServletContext();
        final WebContext webCtx = new WebContext(request, response, servletCtx, request.getLocale());
        if(variables != null) {
            webCtx.setVariables(variables);
        }
        templateEngine.process(templatePath, webCtx, response.getWriter());
    }
}
