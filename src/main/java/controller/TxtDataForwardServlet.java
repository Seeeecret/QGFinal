package controller;

import dao.TxtWatcherThread;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/txtDataForward")
public class TxtDataForwardServlet extends BaseServlet{
    public void forwardData(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        TxtWatcherThread txtWatcherThread = new TxtWatcherThread();
        txtWatcherThread.start();
        request.getRequestDispatcher("/txtData").forward(request,response);
    }
}
