package it.polimi.tiw.tiw_project_ria.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.google.gson.Gson;

import it.polimi.tiw.tiw_project_ria.beans.AddressBook;
import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.Transfer;
import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.AddressBookDAO;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;
import it.polimi.tiw.tiw_project_ria.dao.TransferDAO;
import it.polimi.tiw.tiw_project_ria.packets.PacketAccount;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GetContacts")
@MultipartConfig
public class GetContacts extends HttpServletOnlyDBConnection {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		AddressBookDAO addressBookDAO = new AddressBookDAO(conn);
		AddressBook addressBook;
		
		try {
			addressBook = addressBookDAO.getAddressBookById(currentUser.getId());
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}

		String json = new Gson().toJson(addressBook.getContacts());

		response.setStatus(HttpServletResponse.SC_OK);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
}
