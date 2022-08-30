package it.polimi.tiw.tiw_project_ria.dao;


import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/*
this class is used to generalize the DAO connection
 */
public class GeneralDAO {
    protected Connection conn;

    public GeneralDAO(Connection conn) {
        this.conn = conn;
    }

    public GeneralDAO(Connection conn, String language, String country) {
        this.conn = conn;
    }

    public static Connection applyConnection(ServletContext context) throws ClassNotFoundException, SQLException, SQLException {
        String username = context.getInitParameter("dbUser");
        String password = context.getInitParameter("dbPassword");
        String database = context.getInitParameter("dbUrl");
        String driver = context.getInitParameter("dbDriver");
        Class.forName(driver);
        return DriverManager.getConnection(database,username,password);
    }
}
