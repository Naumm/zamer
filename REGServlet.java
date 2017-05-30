package servlet;

import dao.UserDAO;
import dao.UserDAOimpl;
import mdconvert.ConvertHash;
import model.Users;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by user on 19.04.2017.
 */
@WebServlet( "/registration")
public class REGServlet  extends HttpServlet {
    private UserDAO dao;
    private final String g = "klim";

    public REGServlet() {
        dao = new UserDAOimpl();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String tel = request.getParameter("tel");
        String company = request.getParameter("company");
        String password = request.getParameter("password");
        String pass2 = request.getParameter("pass2");

        String errorMsg = null;
        if (login == null || login.equals("")) {
            errorMsg = "Введите логин";
        }
        if (email == null || email.equals("")) {
            errorMsg = "Забыли ввести email, попробуйте еще раз";
        }
        if (tel == null || tel.equals("")) {
            errorMsg = "Не ввели номер телефона, попробуйте еще раз";
        }
        if (password == null || password.equals("")) {
            errorMsg = "Пароль придумайте посложней";
        }
        if (!pass2.equals(password)) {
            errorMsg = "Повторный пароль введен не верно";
        }

        if (errorMsg != null) {
            response.setCharacterEncoding("utf-8");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "</font>");
            rd.include(request, response);
        } else {

            Users user = new Users();
            user.setLogin(login);
            user.setEmail(email);
            user.setTel(tel);
            user.setCompany(company);
            user.setPass(ConvertHash.md5Custom(ConvertHash.md5Custom(login) + g + ConvertHash.md5Custom(password)));
            user.setSolt(ConvertHash.md5Custom(login));
            dao.addUser(user);
            response.setCharacterEncoding("utf-8");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/measurements");
            dispatcher.include(request, response);
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("utf-8");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/reg.jsp");
            dispatcher.include(request, response);

    }
}