package it.polimi.tiw.tiw_project_ria.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
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
import it.polimi.tiw.tiw_project_ria.packets.PacketTransfer;
import org.thymeleaf.context.WebContext;


@WebServlet("/MakeTransfer")
@MultipartConfig
public class MakeTransfer extends HttpServletDBConnected {

	private int sourceAccountId;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(request,response,context);
		String transferPage = "transferPage";

		String destUserIdString = request.getParameter("destUserId");
		String destAccountIdString = request.getParameter("destAccountId");
		String sourceAccountIdString = request.getParameter("sourceAccountId");
		String amountString = request.getParameter("amount");
		String reason = request.getParameter("reason");

		if (destAccountIdString == null || destUserIdString == null || sourceAccountIdString == null || amountString == null || reason == null) {
			forwardToErrorPage(request, response, "Some data requested is null, when making a transfer");
			return;
		}

		int destAccountId;
		int destUserId;
		BigDecimal amount;

		try {
			sourceAccountId = Integer.parseInt(sourceAccountIdString);
			destAccountId = Integer.parseInt(destAccountIdString);
			destUserId = Integer.parseInt(destUserIdString);
			amount = new BigDecimal(amountString.replace(",", "."));
		} catch (NumberFormatException e) {
			forwardToErrorPage(request, response, "Some values requested are not numbers, when making a transfer");
			return;
		}


		HttpSession session = request.getSession(false);
		User currentUser = (User) session.getAttribute("currentUser");

		BankAccountDAO bankAccountDAO = new BankAccountDAO(conn);
		BankAccount sourceAccount;
		BankAccount destAccount;

		try {
			sourceAccount = bankAccountDAO.getAccountById(sourceAccountId);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		try {
			destAccount = bankAccountDAO.getAccountById(destAccountId);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}

		if(sourceAccount == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Source account does not exist");
			return;
		}
		if(destAccount == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Destination account does not exist");
			return;
		}
		if(sourceAccount.getUserId() != currentUser.getId()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Source account does not belong to the current user");
			return;
		}
		if(destAccount.getUserId() != destUserId) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Destination account does not belong to the selected destination user");
			return;
		}
		if(sourceAccountId == destAccountId) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Cannot make transfers on the same account");
			return;
		}
		if(amount.compareTo(new BigDecimal(0)) != 1) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Transfer amount must be greater than 0");
			return;
		}
		if(sourceAccount.getBalance().subtract(amount).compareTo(new BigDecimal(0)) == -1) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("You don't have enough money on this account to make this transfer");
			return;
		}



		TransferDAO transferDAO = new TransferDAO(conn);
		try {
			transferDAO.makeTransfer(sourceAccountId, destAccountId, reason, amount);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}

		try {
			sourceAccount = bankAccountDAO.getAccountById(sourceAccountId);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		try {
			destAccount = bankAccountDAO.getAccountById(destAccountId);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}

		Transfer transfer = new Transfer();
		transfer.setSourceAccountID(sourceAccountId);
		transfer.setDestinationAccountID(destAccountId);
		transfer.setAmount(amount);
		transfer.setReason(reason);

		String json = new Gson().toJson(new PacketTransfer(sourceAccount, destAccount, transfer));

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);

	}



//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		String destUserIdString = request.getParameter("destUserId");
//		String destAccountIdString = request.getParameter("destAccountId");
//		String sourceAccountIdString = request.getParameter("sourceAccountId");
//		String amountString = request.getParameter("amount");
//		String reason = request.getParameter("reason");
//
//		if(destAccountIdString == null || destUserIdString == null || sourceAccountIdString == null || amountString == null || reason == null) {
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			response.getWriter().println("Missing parameter");
//			return;
//		}
//
//		int destAccountId;
//		int destUserId;
//		int sourceAccountId;
//		BigDecimal amount;
//
//		try {
//			sourceAccountId = Integer.parseInt(sourceAccountIdString);
//			destAccountId = Integer.parseInt(destAccountIdString);
//			destUserId = Integer.parseInt(destUserIdString);
//			amount = new BigDecimal(amountString.replace(",","."));
//		}catch (NumberFormatException e) {
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			response.getWriter().println("Parameters are not numbers");
//			return;
//		}
//
//
//		HttpSession session = request.getSession(false);
//		User currentUser = (User)session.getAttribute("currentUser");
//
//		BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);
//		BankAccount sourceAccount;
//		BankAccount destAccount;
//
//		try {
//			sourceAccount = bankAccountDAO.getAccountById(sourceAccountId);
//		} catch (SQLException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.getWriter().println(e.getMessage());
//			return;
//		}
//		try {
//			destAccount = bankAccountDAO.getAccountById(destAccountId);
//		} catch (SQLException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.getWriter().println(e.getMessage());
//			return;
//		}
//
//		if(sourceAccount == null) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("Source account does not exist");
//			return;
//		}
//		if(destAccount == null) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("Destination account does not exist");
//			return;
//		}
//		if(sourceAccount.getUserId() != currentUser.getId()) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("Source account does not belong to the current user");
//			return;
//		}
//		if(destAccount.getUserId() != destUserId) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("Destination account does not belong to the selected destination user");
//			return;
//		}
//		if(sourceAccountId == destAccountId) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("Cannot make transfers on the same account");
//			return;
//		}
//		if(amount.compareTo(new BigDecimal(0)) != 1) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("Transfer amount must be greater than 0");
//			return;
//		}
//		if(sourceAccount.getBalance().subtract(amount).compareTo(new BigDecimal(0)) == -1) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().println("You don't have enough money on this account to make this transfer");
//			return;
//		}
//
//		TransferDAO transferDAO = new TransferDAO(connection);
//		try {
//			transferDAO.makeTransfer(sourceAccountId, destAccountId, reason, amount);
//		} catch (SQLException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.getWriter().println(e.getMessage());
//			return;
//		}
//
//		try {
//			sourceAccount = bankAccountDAO.getAccountById(sourceAccountId);
//		} catch (SQLException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.getWriter().println(e.getMessage());
//			return;
//		}
//		try {
//			destAccount = bankAccountDAO.getAccountById(destAccountId);
//		} catch (SQLException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.getWriter().println(e.getMessage());
//			return;
//		}
//
//		Transfer transfer = new Transfer();
//		transfer.setSourceAccountID(sourceAccountId);
//		transfer.setDestinationAccountID(destAccountId);
//		transfer.setAmount(amount);
//		transfer.setReason(reason);
//
//		String json = new Gson().toJson(new PacketTransfer(sourceAccount, destAccount, transfer));
//
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		response.getWriter().println(json);
//
//	}

}
