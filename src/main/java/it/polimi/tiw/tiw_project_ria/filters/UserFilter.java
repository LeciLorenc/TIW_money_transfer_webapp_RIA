package it.polimi.tiw.tiw_project_ria.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserFilter extends HttpServletFilter {

    /* check if user is already stored in Session */
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();

        if(session.isNew() || session.getAttribute("currentUser")==null) {
            String path = req.getPathInfo();
            req.setAttribute("error", "You are not authorized to access this page");
//            forward(req, res, PathUtils.pathToErrorPage);
            res.sendRedirect(path + "/index");
        }else {
            chain.doFilter(req,res);
        }
    }


}
