package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;

/**
 * A simple demo page for SQL injection.
 * Not going to remain in final version, simply a place to hold code during dev
 * 
 * @author matt @ Software Secured
 *
 */
@WebServlet("/demo")
public class Demo extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Demo.class);
	private static final long serialVersionUID = 1L;

	
	/**
	 * Call parent constructor
	 */
	public Demo() {
		super();
	}
	
	
	/**
	 * Handle GET. Doesn't take parameters, just passes control to the Demo JSP
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		dump(req, res);
	}
	
	/**
	 * Handle POST. Doesn't take parameters, just passes control to the Demo JSP
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		dump(req, res);
	}

	
	/**
	 * Handle the submission of a request for User ID.
	 * Uses the following parameters:
	 *   userid -> the target userid to fetch from the DB
	 *   injection -> if equals "allow" then allow easy SQL injection
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void dump(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}

		Session sess = HibernateUtility.getSessionFactory().openSession();

		String userid = req.getParameter("userid");

		StringBuilder tmp = new StringBuilder();

		for(char c : userid.toCharArray()){
			if (!Character.isDigit(c)){
				break;
			}
			tmp.append(c);
		}

		Integer uid = Integer.parseInt(tmp.toString());

		List<User> results = null;

		if (userid != null && userid.length() > 0) {
			results = sess.createSQLQuery("select * from Users WHERE ID = " + userid).addEntity(User.class).list();
			log.info("Allowed raw SQL query without validation");
		}

		req.setAttribute("rows", results);

		log.info("Starting JSP");
		req.getRequestDispatcher("user_dump.jsp").forward(req, res);
		log.info("Gave up control");

		sess.close();
	}
}
