package it.polimi.db2.db2_project.web.filter;

import it.polimi.db2.db2_project.web.application.GamifiedQuizApplication;
import it.polimi.db2.db2_project.web.controllers.GQController;
import org.thymeleaf.ITemplateEngine;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GQFilter implements Filter {

    private ServletContext servletContext;
    private GamifiedQuizApplication application;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        this.application = new GamifiedQuizApplication(this.servletContext);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //TODO Handle Session?????
        if(!process((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            // This prevents triggering engine executions for resource URLs
            if (request.getRequestURI().startsWith("/css") ||
                    request.getRequestURI().startsWith("/images") ||
                    request.getRequestURI().startsWith("/favicon")) {
                return false;
            }

            GQController controller = this.application.dispatchController(request);
            if (controller == null) {
                return false;
            }

            ITemplateEngine templateEngine = this.application.getTemplateEngine();

            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            controller.process(
                    request, response, this.servletContext, templateEngine);
        } catch (Exception e) {
            try{
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ignore) {

            }
            throw new ServletException(e);
        }
        return true;
    }

    @Override
    public void destroy() {

    }

}
