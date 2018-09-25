package com.linedown.backend;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateBaseDataSetServlet extends HttpServlet {
 
   public void init() throws ServletException {
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
       String ID = request.getParameter("ID");
       
       LineupUpdater updater = new DatabaseUpdater();
       updater.recalculateBaseDataSet(ID);   
   }
   
   public void destroy() {
   }
   
}
