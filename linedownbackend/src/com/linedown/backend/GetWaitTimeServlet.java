package com.linedown.backend;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GetWaitTimeServlet extends HttpServlet {
 
  // private String message;
  // private WaitTimeCalculator calculator;

   public void init() throws ServletException {
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      
      response.setContentType("text/plain");

      String ID = request.getParameter("ID");
      WaitTimeCalculator calculator = new CalculatorWithLocationTracking();
      String estWaitTime = String.valueOf((int)calculator.getWaitTime(ID));
      
      PrintWriter out = response.getWriter();
      out.println(estWaitTime);
   }
   
   public void handleRequest (HttpServletRequest request, HttpServletResponse response)
      throws IOException {   
      
   }

   public void destroy() {
   }
}
