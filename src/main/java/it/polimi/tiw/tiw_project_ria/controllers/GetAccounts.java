package it.polimi.tiw.tiw_project_ria.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GetAccounts")
@MultipartConfig
public class GetAccounts extends HttpServletOnlyDBConnection {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);
		List<BankAccount> hisAccounts;
		try {
			hisAccounts = bankAccountDAO.getAccountsByUserId(currentUser.getId());
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		String json = new Gson().toJson(hisAccounts);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
}
