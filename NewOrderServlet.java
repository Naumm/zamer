package servlet;

import dao.OrderDAO;
import dao.OrderDAOimpl;
import mail.Sender;
import model.Orders;
import model.Users;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;

@WebServlet("/new")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)   // 50MB
public class NewOrderServlet extends HttpServlet {

    private static final String SAVE_DIR = "/../uploads";

    //private static Sender sslSender = new Sender("3d.zamer@gmail.com", "zamer@zamer");
    private OrderDAO dao;
    public NewOrderServlet() {
        dao = new OrderDAOimpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null ){response.sendRedirect("/");}
        else {

            response.setCharacterEncoding("utf-8");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/new.jsp");
            dispatcher.include(request, response);
        }

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");



        Orders o = dao.getLastOrders();
        int id = o.getId()+1;
        String nameDirectory = String.valueOf(id);
        // gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("/");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR + File.separator + nameDirectory;
        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }


        String adres = request.getParameter("adr");
        String floor = request.getParameter("floor");
        String telz = request.getParameter("te");
        String namer = request.getParameter("name");
        String den = request.getParameter("den");
        String man = request.getParameter("man");
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
        order.setMan(man);
        order.setCompany(user.getCompany());
        order.setFot("/uploads/"+nameDirectory+"/1.JPG");
        dao.addOrder(order);




        Sender em = new Sender("3d.zamer@gmail.com", "zamer@zamer");
        em.send(order.getAdres(), "Спасибо за заявку."+order.getMan()+" В ближайшее время она будет обработана! ", "3d.zamer@gmail.com", user.getEmail());

        response.sendRedirect("/measurements");

    }
    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }



}



