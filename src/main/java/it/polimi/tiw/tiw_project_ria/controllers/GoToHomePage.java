package it.polimi.tiw.tiw_project_ria.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@MultipartConfig

@WebServlet("/goToHomePage")
public class GoToHomePage extends HttpServletDBConnected {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User)session.getAttribute("currentUser");
        Boolean createAccountExists = (Boolean)session.getAttribute("createAccountExists");
        String homePage = "/home";
        ServletContext context = getServletContext();
        WebContext webContext = new WebContext(req,resp,context);
        BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);
        List<BankAccount> clientBankAccounts;
        try {
            clientBankAccounts = bankAccountDAO.getAccountsByUserId(currentUser.getId());
        }catch(SQLException e) {
            forwardToErrorPage(req, resp, e.getMessage());
            return;
        }

        if(createAccountExists != null && createAccountExists) {
            session.removeAttribute("createAccountExists");
            req.setAttribute("warning", "Chosen bank account name already exists!");
        }
       // req.setAttribute("accounts", clientBankAccounts);

        String json = new Gson().toJson(clientBankAccounts);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(json);

//        webContext.setVariable("currentUser",currentUser);
//        webContext.setVariable("listClientsAccount",clientBankAccounts);

       // thymeleaf.process(homePage,webContext,resp.getWriter());


    }


}
