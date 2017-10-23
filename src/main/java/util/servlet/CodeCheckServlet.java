package util.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name ="CodeCheckServlet",urlPatterns = "/CodeCheck")
public class CodeCheckServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code") ;
		String rand = (String) request.getSession().getAttribute("rand") ;
		if (rand == null) {
			response.getWriter().print(false);
		} else {
			response.getWriter().print(rand.equalsIgnoreCase(code)); 
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response); 
	}
}
