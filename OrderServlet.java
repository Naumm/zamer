package servlet;

import dao.OrderDAO;
import dao.OrderDAOimpl;
import model.Orders;
import model.Users;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet( "/order")

public class OrderServlet extends HttpServlet {

    private OrderDAO dao;

    public OrderServlet() {
        dao = new OrderDAOimpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null ){response.sendRedirect("/");}
        else {
            String p = request.getParameter("id");
            int foo = Integer.parseInt(p);
            RequestDispatcher view = request.getRequestDispatcher("/pages/order.jsp");
            request.setAttribute("order", dao.getOrderById(foo));
            view.forward(request, response);
        }
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");




        String adres = request.getParameter("adr");
        String floor = request.getParameter("floor");
        String telz = request.getParameter("te");
        String namer = request.getParameter("name");
        String den = request.getParameter("den");
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy.MM.dd");
        try {
            Date docDate= format.parse(den);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Time tim = Time.valueOf(request.getParameter("tim"));
        String coment = request.getParameter("come");
        String metka = request.getParameter("me");


        Orders order = new Orders();
        order.setAdres(adres);
        order.setFloor(floor);
        order.setTelz(telz);
        order.setNamer(namer);
        //order.setDen(docDate);
        //order.setTim(tim);
        order.setComent(coment);
        order.setMetka(metka);
        order.setMan(user.getLogin());
        order.setCompany(user.getCompany());

        dao.addOrder(order);

        response.setCharacterEncoding("utf-8");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/orders");
        dispatcher.forward(request, response);
    }

}




