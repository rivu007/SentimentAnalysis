package org.rssoftware.sentiment.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.rssoftware.sentiment.dao.FetchRecords;

/**
 * Servlet implementation class ReportService
 */
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		System.out.println("Inside doPOST()");
		FetchRecords fetchRecords = new FetchRecords();
		ArrayList<String> getSentimentResult = fetchRecords.showReport();
		if(getSentimentResult.size()==2){
			session.setAttribute("key", getSentimentResult.get(0));
			/*
			 * Rounding the value to two decimal place
			 */
			String formatedValue = String.format("%.2f", Float.valueOf(getSentimentResult.get(1))); 
			session.setAttribute("totalSentiment", formatedValue);
			System.out.println("key" + getSentimentResult.get(0));
			System.out.println("totalSentiment" + getSentimentResult.get(1));
			RequestDispatcher reDisp = request.getRequestDispatcher("ShowReport.jsp");
			reDisp.forward(request, response);
		}

	}

}
