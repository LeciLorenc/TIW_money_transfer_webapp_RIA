package it.polimi.tiw.tiw_project_ria.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.google.gson.Gson;
import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.Transfer;
import it.polimi.tiw.tiw_project_ria.beans.User;
import it.polimi.tiw.tiw_project_ria.dao.BankAccountDAO;
import it.polimi.tiw.tiw_project_ria.dao.TransferDAO;
import it.polimi.tiw.tiw_project_ria.packets.PacketAccount;


/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GetAccountDetails")
@MultipartConfig
public class GetAccountDetails extends HttpServletDBConnected {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String accountIdString = request.getParameter("accountId");
		
		if(accountIdString == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Missing parameter");
			return;
		}
		
		int accountId;
		try {
			accountId = Integer.parseInt(accountIdString);
		}catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Parameter is not an integer");
			return;
		}
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);
		BankAccount bankAccount;
		try {
			bankAccount = bankAccountDAO.getAccountById(accountId);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;		
		}
		
		if(bankAccount == null || bankAccount.getUserId() != currentUser.getId()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("The account doesn't exist or is not yours");
			return;
		}
		
		List<Transfer> transfers;
		TransferDAO transferDAO = new TransferDAO(conn);
		try {
			transfers = transferDAO.getTransfersByAccountId(accountId);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;	
		}
		String json = new Gson().toJson(new PacketAccount(bankAccount, transfers));

		response.setStatus(HttpServletResponse.SC_OK);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
