package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.Kursy;
import model.dao.ObslugaBD;
import model.encje.DaneDolar;


@WebServlet(urlPatterns= {"/kantor", "/index"})
public class Kantor extends HttpServlet {
	private static final long serialVersionUID = 1L;


	public void init(ServletConfig config) throws ServletException {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ObslugaBD bd = new ObslugaBD();
			Kursy kursy = new Kursy();
			List<DaneDolar> listaDaneDolar = bd.odczytListaDaneDolar();
			int rozmiar = listaDaneDolar.size();
			DaneDolar dd = listaDaneDolar.get(rozmiar-1);
			ServletContext sc = request.getServletContext();
			sc.setAttribute("kurs", kursy);
			sc.setAttribute("mnoznik", dd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=UTF-8");
		request.getRequestDispatcher("jsp/logowanie.jsp").include(request, response);
		request.getRequestDispatcher("jsp/index.jsp").include(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
