package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.services.LeaderboardService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "LeaderboardController", value = "/LeaderboardController")
public class LeaderboardController extends TemplatingServlet {

    @EJB
    LeaderboardService leaderboardService;

    public LeaderboardController() {
        super("leaderboard", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        context.put("leaderboard", leaderboardService.findCurrentLeaderboard());

        super.processTemplate(request, response, context);
    }

}
