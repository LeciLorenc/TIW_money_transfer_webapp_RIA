package it.polimi.tiw.tiw_project_ria.controllers;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig

@WebServlet("/index")
public class GoToIndexPage extends HttpServletDBConnected {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext context = getServletContext();
        WebContext webContext = new WebContext(req,resp,context);
        String page = "indexPage";

        webContext.setVariable("loginError",false);
        webContext.setVariable("registerError",false);
        thymeleaf.process(page,webContext,resp.getWriter());
    }
}

