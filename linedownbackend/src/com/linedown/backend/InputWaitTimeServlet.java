package com.linedown.backend;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class InputWaitTimeServlet extends HttpServlet {
 
   public void init() throws ServletException {
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
       String ID = request.getParameter("ID");
       String waitTime = request.getParameter("WaitTime");
       int WaitTime = Integer.parseInt(waitTime);
       
       LineupUpdater updater = new DatabaseUpdater();
       updater.addWaitTime(ID, WaitTime);   
   }
   
   public void destroy() {
   }
   
}
