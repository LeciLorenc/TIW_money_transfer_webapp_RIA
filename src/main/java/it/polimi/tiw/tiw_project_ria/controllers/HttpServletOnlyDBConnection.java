package it.polimi.tiw.tiw_project_ria.controllers;

import it.polimi.tiw.tiw_project_ria.filters.HttpServletFilter;
import it.polimi.tiw.tiw_project_ria.utils.Const;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public class HttpServletOnlyDBConnection extends HttpServlet {
    protected Connection conn;
    protected TemplateEngine thymeleaf;

    @Override
    public void init() throws ServletException {
        try {
            // getting the connection
            ServletContext context = getServletContext();
            conn = HttpServletFilter.applyConnection(context);

            // preparing thymeleaf template

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnavailableException(Const.unavailableException);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnavailableException(Const.sqlException);
        }
    }


    @Override
    public void destroy() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ignored) {}
    }

}
