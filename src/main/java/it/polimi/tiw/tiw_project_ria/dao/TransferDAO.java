package it.polimi.tiw.tiw_project_ria.dao;


import it.polimi.tiw.tiw_project_ria.beans.Transfer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransferDAO extends GeneralDAO{
    public TransferDAO(Connection conn) {
        super(conn);
    }



    public List<Transfer> getTransfersByAccountId(int accountId) throws SQLException {

        List<Transfer> transfers = new ArrayList<>();
        String performedAction = " finding transfers by account id";
        String query = "SELECT * FROM transfer WHERE source_account = ? OR destination_account = ? ORDER BY timestamp DESC";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, accountId);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Transfer transfer = new Transfer();
                transfer.setSourceAccountID(resultSet.getInt("source_account"));
                transfer.setDestinationAccountID(resultSet.getInt("destination_account"));
                transfer.setTimestamp(new Date(resultSet.getTimestamp("timestamp").getTime()));
                transfer.setAmount(resultSet.getBigDecimal("amount"));
                transfer.setReason(resultSet.getString("reason"));
                transfers.add(transfer);
            }

        }catch(SQLException e) {
            throw new SQLException("Error accessing the DB when" + performedAction);
        }finally {
            try {
                resultSet.close();
            }catch (Exception e) {
                throw new SQLException("Error closing the result set when" + performedAction);
            }
            try {
                preparedStatement.close();
            }catch (Exception e) {
                throw new SQLException("Error closing the statement when" + performedAction);
            }
        }
        return transfers;
    }


    public void makeTransfer(int sourceAccountId, int destAccountId, String reason, BigDecimal amount) throws SQLException{

        String performedAction = " making a new transfer";
        String transactionInsert = "INSERT INTO transfer (source_account, destination_account, reason, amount) VALUES(?,?,?,?)";
        String transactionUpdateSource = "UPDATE bank_account SET balance = balance - ? WHERE id = ?";
        String transactionUpdateDest = "UPDATE bank_account SET balance = balance + ? WHERE id = ?";

        PreparedStatement preparedStatementInsert = null;
        PreparedStatement preparedStatementUpdateSource= null;
        PreparedStatement preparedStatementUpdateDest = null;

        try {
            conn.setAutoCommit(false);
            preparedStatementInsert = conn.prepareStatement(transactionInsert);
            preparedStatementUpdateSource = conn.prepareStatement(transactionUpdateSource);
            preparedStatementUpdateDest = conn.prepareStatement(transactionUpdateDest);

            preparedStatementInsert.setInt(1, sourceAccountId);
            preparedStatementInsert.setInt(2, destAccountId);
            preparedStatementInsert.setString(3, reason);
            preparedStatementInsert.setBigDecimal(4, amount);
            preparedStatementInsert.executeUpdate();

            preparedStatementUpdateSource.setBigDecimal(1, amount);
            preparedStatementUpdateSource.setInt(2, sourceAccountId);
            preparedStatementUpdateSource.executeUpdate();

            preparedStatementUpdateDest.setBigDecimal(1, amount);
            preparedStatementUpdateDest.setInt(2, destAccountId);
            preparedStatementUpdateDest.executeUpdate();

            conn.commit();

        }catch(SQLException e) {
            conn.rollback();

            throw new SQLException("Error accessing the DB when" + performedAction);
        }finally {
            conn.setAutoCommit(true);
            try {
                preparedStatementInsert.close();
                preparedStatementUpdateSource.close();
                preparedStatementUpdateDest.close();
            }catch (Exception e) {
                throw new SQLException("Error closing the statement when" + performedAction);
            }
        }
    }
}
