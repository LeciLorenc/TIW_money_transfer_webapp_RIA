package it.polimi.tiw.tiw_project_ria.controllers;

import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.AddressBookDAO;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/AddContact")
@MultipartConfig
public class AddContact extends HttpServletDBConnected {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("dentro add contacts");
		String contactIdString = request.getParameter("contactId");
		String contactAccountIdString = request.getParameter("contactAccountId");
		
		if(contactAccountIdString == null || contactIdString == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Missing parameter");
			return;
		}
		
		int contactId;
		int contactAccountId;
		
		try {
			contactAccountId = Integer.parseInt(contactAccountIdString);
			contactId = Integer.parseInt(contactIdString);
			
		}catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Parameters are not integers");
			return;
		}
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);
		BankAccount bankAccount;

		try {
			bankAccount = bankAccountDAO.getAccountById(contactAccountId);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;		
		}

		if(bankAccount == null || bankAccount.getUserId() != contactId) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Chosen parameters are inconsistent with the database info, retry");
			return;
		}
		
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn);
		boolean alreadyPresent = false;
		try {
			alreadyPresent = addressBookDAO.existsContactEntry(currentUser.getId(), contactAccountId);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;		
		}
		
		if(alreadyPresent) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Contact entry already exists");
			return;
		}
		
		try {
			addressBookDAO.addContactToAddressBook(currentUser.getId(), contactAccountId);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_OK);	
		
	
	}

}
