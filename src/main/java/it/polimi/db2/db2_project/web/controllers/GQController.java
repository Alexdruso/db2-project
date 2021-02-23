package it.polimi.db2.db2_project.web.controllers;

import org.thymeleaf.ITemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GQController {
    void process(HttpServletRequest request, HttpServletResponse response,
                        ServletContext servletContext, ITemplateEngine templateEngine) throws Exception;
}
