package com.linedown.backend;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateUserLocationServlet extends HttpServlet {
 
   public void init() throws ServletException {
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
       String ID = request.getParameter("ID");
       int isNearby = Integer.parseInt(request.getParameter("Nearby"));
       
       LineupUpdater updater = new DatabaseUpdater();
       updater.updateUserLocation(ID, isNearby);
   }
   
   public void destroy() {
   }
   
}
