package it.polimi.tiw.tiw_project_ria.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.tiw_project_ria.beans.AddressBook;
import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.AddressBookDAO;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GetContacts")
@MultipartConfig
public class GetContacts extends HttpServletDBConnected {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
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
