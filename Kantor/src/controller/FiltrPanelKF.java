package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.UserZalogowany;

/**
 * Servlet Filter implementation class FiltrPanelKF
 */
@WebFilter(urlPatterns= {"/panelKlientaFirmowego", "/transakcjaKF", "/jsp//panelKlientaFirmowego.jsp", "/jsp/transakcjaKF.jsp", "/statystykaKF", "/jsp/statystykaKF.jsp"})
public class FiltrPanelKF implements Filter {

    /**
     * Default constructor. 
     */
    public FiltrPanelKF() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession sesja = req.getSession();
		UserZalogowany uz = (UserZalogowany) sesja.getAttribute("userZalogowany");

		if(uz == null || uz.getIdRola() != 3) {
			sesja.invalidate();
			resp.sendRedirect("/Kantor/logowanie");
			return;
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
