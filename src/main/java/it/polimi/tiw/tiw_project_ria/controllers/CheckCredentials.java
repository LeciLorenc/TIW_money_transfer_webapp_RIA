package it.polimi.tiw.tiw_project_ria.controllers;

import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.UserDAO;
import it.polimi.tiw.tiw_project_ria.packets.PacketUser;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/checkLogin")
public class CheckCredentials extends HttpServletDBConnected {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ResourceBundle lang = findLanguage(req);
        ServletContext context = getServletContext();
        WebContext webContext = new WebContext(req,resp,context);
        HttpSession session;

        String loginPage = "loginPage";
        String homePage = getServletContext().getContextPath() + "/goToHomePage";
        String indexPage = getServletContext().getContextPath() + "/index";
        String uMail = null;
        String uPassword0 = null;
        String uPassword1 = null;
        UserDAO userDAO = new UserDAO(conn);
        User user;
        boolean loginError = false , dataError = false ,passwordError = false;

        try {
            uMail = StringEscapeUtils.escapeJava(req.getParameter("email"));
            uPassword0 = StringEscapeUtils.escapeJava(req.getParameter("password0"));
            uPassword1 = StringEscapeUtils.escapeJava(req.getParameter("password1"));
            if(!uPassword0.equals(uPassword1) ) passwordError = true;

        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            loginError = true;
        }

        // request processed only if the parameters are sent correctly
        if(!passwordError && uMail != null && !uMail.isEmpty() && uPassword0 != null && !uPassword0.isEmpty()
        ) {


            try {

                user = userDAO.findUserByEmail(uMail,uPassword0);

                if(user != null) {
                    session = req.getSession(true);
                    session.setAttribute("currentUser",user);
                } else {
                    loginError = true;
                    dataError = false;
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                resp.sendRedirect("/error?code=500");
                return;

            }




            //if errors accured , user is sent back to its inital page
            if(loginError || passwordError || dataError) {
                try {
                  //  webContext.setVariable("lang",lang);
                    webContext.setVariable("error",loginError);
                    thymeleaf.process(loginPage,webContext,resp.getWriter());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else { //no errors

//                String packetUser = new Gson().toJson(new PacketUser(user.getName(), user.getId()));
//
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.setCharacterEncoding("UTF-8");
//                resp.getWriter().println(packetUser);

                resp.sendRedirect(homePage);
            }
        }else { //bad request
            resp.sendRedirect(indexPage);
        }
//
//                ResourceBundle lang = findLanguage(req);
//        ServletContext context = getServletContext();
//        WebContext webContext = new WebContext(req, resp, context);
//        HttpSession session;
//        User user;
//        UserDAO userDAO = new UserDAO(conn);
//        String path = getServletContext().getContextPath() + "/GoToHome";
//        String email = req.getParameter("email");
//        String password = req.getParameter("password");
//
//
//        // the request is processed only if the parameters sent are correct
//        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
//            //resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "dbAccountInvalidCredentials");
//            resp.sendRedirect("/error?code=500");
//            //throw new UnavailableException("problema leggendo input");
//            return;
//        }
//
//        try {
//            user = userDAO.findUser(email, password);
//
//            if (user != null) {
//                session = req.getSession(true);
//                session.setAttribute("user", user);
//
//            } else {
//                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "user not found");
//                return;
//            }
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
//            return;
//        }
//
//
//        resp.sendRedirect(path);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/index");
    }
}

