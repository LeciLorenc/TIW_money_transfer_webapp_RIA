package it.polimi.tiw.tiw_project_ria.controllers;

import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;
import it.polimi.tiw.tiw_project_ria.dao.UserDAO;
import it.polimi.tiw.tiw_project_ria.utils.Const;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/HandleRegistration")
public class HandleRegistration extends HttpServletDBConnected {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String pwd1 = request.getParameter("pwd1");
        String pwd2 = request.getParameter("pwd2");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        ServletContext context = getServletContext();
        WebContext webContext = new WebContext(request, response, context);
        String indexPage = "indexPage";
        BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);

        if (name == null || name.isEmpty() ||
                email == null || email.isEmpty() ||
                pwd1 == null || pwd1.isEmpty() ||
                pwd2 == null || pwd2.isEmpty() ||
                surname == null || surname.isEmpty() ||
                username == null || username.isEmpty()
                ) {
            forwardToErrorPage(request, response, "Register module missing some data");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            request.setAttribute("warning", "password not the same");
            forward(request, response, indexPage);
            return;
        }


        UserDAO userDAO = new UserDAO(conn);


        User user = null;

        try {
            user = userDAO.getUserByEmail(email);
        } catch (SQLException e) {
            forwardToErrorPage(request, response, e.getMessage());
            return;
        }

        if (user != null) {
            request.setAttribute("warning", "Chosen email already exists!");
            forward(request, response, indexPage);
            return;
        }


        try {
            conn.setAutoCommit(false);
            userDAO.registerUser(name,surname, email, pwd1,username);
            user = userDAO.getUserByEmail(email);
            bankAccountDAO.createAccount(user.getId(),"default account : " + user.getName());
            conn.commit();


        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();

            }
            forwardToErrorPage(request, response, e.getMessage());
            return;
        }finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    throw new SQLException("Error closing the statement when" + "error creating a new default account");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        }


        try {
            user = userDAO.getUserByEmail(email);
        } catch (SQLException e) {
            forwardToErrorPage(request, response, e.getMessage());
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);
        response.sendRedirect(getServletContext().getContextPath() + "indexPage");

    }


}




