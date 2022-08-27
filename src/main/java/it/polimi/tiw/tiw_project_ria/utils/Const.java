package it.polimi.tiw.tiw_project_ria.utils;

public class Const {

    public static String pathToHomePage = "HomePage";
    public static String goToHomeServletPath = "/goToHomePage";

    private Const() {
    }

    // error messages before having instantiated the correct lang
    public static final String unavailableException = "Can't find database driver";
    public static final String sqlException = "Can't load database";
    public static final String validEmailRegex = "/^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$/\n";
    public static String pathToErrorPage = "/error.html";

}
