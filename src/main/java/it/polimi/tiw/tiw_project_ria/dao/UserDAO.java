package it.polimi.tiw.tiw_project_ria.dao;


import it.polimi.tiw.tiw_project_ria.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO extends GeneralDAO {



    public UserDAO(Connection connection) {
        super(connection);
    }



    public User findUserByEmail(String email, String password) throws SQLException{

        User user = null;
        String performedAction = " finding a client by email and password";
        String query = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        query = "SELECT * FROM  client WHERE email = ? AND password = ?";

        preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("email"),
                    resultSet.getString("username")
            );
        } else return null;
        return user;


    }

    public void registerUser(String name, String surname, String email, String password,String username) throws SQLException {

        String performedAction = " registering a new user in the database";
        String queryAddUser = "INSERT INTO client (name,surname,email,password,username) VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatementAddUser = null;



        try {

            preparedStatementAddUser = conn.prepareStatement(queryAddUser);
            preparedStatementAddUser.setString(1, name);
            preparedStatementAddUser.setString(2, surname);
            preparedStatementAddUser.setString(3, email);
            preparedStatementAddUser.setString(4, password);
            preparedStatementAddUser.setString(5, username);
            preparedStatementAddUser.executeUpdate();

        }catch(SQLException e) {
            throw new SQLException("Error accessing the DB when" + performedAction);
        }finally {
            try {
                preparedStatementAddUser.close();
            }catch (Exception e) {
                throw new SQLException("Error closing the statement when" + performedAction);
            }
        }
    }

    public User getUserByEmail(String email) throws SQLException{

        User user = null;
        String performedAction = " finding a user by email";
        String query = "SELECT * FROM client WHERE email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setEmail(resultSet.getString("email"));
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
        return user;
    }

    public User getUserById(int id) throws SQLException{

        User user = null;
        String performedAction = " finding a user by id";
        String query = "SELECT * FROM client WHERE id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
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
        return user;
    }



}
