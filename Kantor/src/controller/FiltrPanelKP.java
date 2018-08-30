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
 * Servlet Filter implementation class FiltrPanelKP
 */
@WebFilter(urlPatterns= { "/panelKlientaPrywatnego", "/transakcjaKP", "/jsp/panelKlientaPrywatnego.jsp", "/jsp/transakcjaKP.jsp"})
public class FiltrPanelKP implements Filter {

    /**
     * Default constructor. 
     */
    public FiltrPanelKP() {
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

		if(uz == null || uz.getIdRola() != 4) {
			sesja.invalidate();
			resp.sendRedirect("http://localhost:8080/Kantor/logowanie");
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
