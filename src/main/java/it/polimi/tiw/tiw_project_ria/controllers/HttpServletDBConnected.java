package it.polimi.tiw.tiw_project_ria.controllers;


import it.polimi.tiw.tiw_project_ria.filters.HttpServletFilter;
import it.polimi.tiw.tiw_project_ria.utils.Const;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class HttpServletDBConnected extends HttpServlet {
    protected Connection conn;
    protected TemplateEngine thymeleaf;

    @Override
    public void init() throws ServletException {
        try {
            // getting the connection
            ServletContext context = getServletContext();
            conn = HttpServletFilter.applyConnection(context);

            // preparing thymeleaf template
            ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setPrefix("/WEB-INF/");
            templateResolver.setSuffix(".html");
            thymeleaf = new TemplateEngine();
            thymeleaf.setTemplateResolver(templateResolver);
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

    void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{

        request.setAttribute("error", error);
        forward(request, response, Const.pathToErrorPage);
        return;
    }

    void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException, IOException {

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        thymeleaf.process(path, ctx, response.getWriter());

    }

//    public static ResourceBundle findLanguage(HttpServletRequest req) {
//        ResourceBundle lang;
//
//        if (Const.acceptedLangTags.contains(req.getLocale().getLanguage())) {
//            String language = req.getLocale().getLanguage();
//            String country = Const.isoTagToCountry.get(language);
//            lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
//        } else if (Const.acceptedOldIsoLangTags.contains(req.getLocale().getLanguage())) {
//            String language = Const.oldIsoLangTagsToNew.get(req.getLocale().getLanguage());
//            String country = Const.isoTagToCountry.get(language);
//            lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
//        } else {
//            lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(Const.defaultLanguage,Const.defaultCountry));
//        }
//        return lang;
//    }
}
