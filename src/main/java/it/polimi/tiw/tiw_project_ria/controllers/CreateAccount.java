package it.polimi.tiw.tiw_project_ria.controllers;

import it.polimi.tiw.tiw_project_ria.beans.*;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/CreateAccount")
@MultipartConfig
public class CreateAccount extends HttpServletDBConnected {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
		String accountName = request.getParameter("accountName");
		
		if(accountName == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Missing parameter");
			return;
		}
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);
		BankAccount bankAccount;
		try {
			bankAccount = bankAccountDAO.getAccountByUserIdAndName(currentUser.getId(), accountName);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		if(bankAccount != null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Chosen account name already exists");
			return;
		}
		
		try {
			bankAccountDAO.createAccount(currentUser.getId(), accountName);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;	
		}
		
		response.setStatus(HttpServletResponse.SC_OK);	
	
	}

}
