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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by user on 28.03.2017.
 */
@WebServlet(
        name = "LoginUser",
        description = "Вход пользователя - проверка имени пользователя и пароля",
        urlPatterns = "/home"
        )

public class MyServlet extends HttpServlet {

    private final String g = "klim";
    private UserDAO dao;

    public MyServlet() {
        dao = new UserDAOimpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        String errorMsg = null;
        if (login == null || login.equals("")) {
            errorMsg = "Введите логин или e-mail";
        }
        if (password == null || password.equals("")) {
            errorMsg = "Забыли ввести пароль, попробуйте еще раз";
        }

        if (errorMsg != null) {
            response.setCharacterEncoding("utf-8");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/auth.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "</font>");
            rd.include(request, response);
        } else {

            Users use;
            Users user = new Users();
            List users = dao.getAllUser();
            for (int i = 0; i < users.size(); i++) {
                use = (Users) users.get(i);
                if (use.getLogin().toString() == login) {
                    user = use;
                }
            }
            //user = dao.getUserById(69);
            if (user.getLogin() != null) {
                String selina = user.getSolt();
                String po = null;
                try {
                    po = ConvertHash.md5Custom(selina + g + ConvertHash.md5Custom(password));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (po.equals(user.getPass())) {
                    HttpSession session = request.getSession();
                    session.setAttribute("login", user.getLogin());
                    session.setAttribute("company", user.getCompany());
                    //setting session to expiry in 30 mins
                    session.setMaxInactiveInterval(30 * 60);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/orders");
                    dispatcher.forward(request, response);

                    // request.getRequestDispatcher("pages/header.jsp").include(request, response);
                    //getServletContext().getRequestDispatcher("/orders").forward(request,response);
                    // request.getRequestDispatcher("/orders").forward(request, response);
                    // request.getRequestDispatcher("pages/footer.jsp").include(request, response);
                } else {
                    response.setCharacterEncoding("utf-8");
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/auth.jsp");
                    PrintWriter out = response.getWriter();
                    out.println("<font color=red>" + "Пароль не верный!" + "</font>");
                    rd.include(request, response);
                }

            } else {
                response.setCharacterEncoding("utf-8");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/auth.jsp");
                PrintWriter out = response.getWriter();
                out.println("<font color=red>" + "Такого логина нет в базе!" + "</font>");
                rd.include(request, response);
            }
        }
    }
}





