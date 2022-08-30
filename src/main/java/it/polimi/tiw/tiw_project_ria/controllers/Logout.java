package it.polimi.tiw.tiw_project_ria.controllers;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@MultipartConfig

@WebServlet("/Logout")
public class Logout extends HttpServletDBConnected{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);


        if (session != null) {
            session.invalidate();
        }

        String path = getServletContext().getContextPath();
        resp.sendRedirect(path + "/index");
    }
}
