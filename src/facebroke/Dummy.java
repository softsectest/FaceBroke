package facebroke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;

public class Dummy extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Dummy.class);
	private static final long serialVersionUID = 1L;

	private static boolean validEmail(String email) {
		boolean result = false;
		try {
			InternetAddress addr = new InternetAddress(email);
			addr.validate();
			result = true;
		} catch (Exception e) {
			// Don't need to do anything, the simple 'false' return will tell enough
		}
		return result;
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Session sess = HibernateUtility.getSessionFactory().openSession();

		String userid = req.getParameter("userid");

		List<User> results = null;

		if (userid != null && userid.length() > 0) {

			if (req.getParameter("injection").equals("allow")) {
				results = sess.createSQLQuery("select * from Users WHERE ID = " + userid).addEntity(User.class).list();
			} else {
				results = sess.createQuery("FROM User U WHERE U.id = :user_id")
						.setParameter("user_id", Long.parseLong(userid)).list();
			}
		}

		req.setAttribute("rows", results);

		log.info("Starting JSP");
		req.getRequestDispatcher("/user.jsp").forward(req, res);
		log.info("Gave up control");

	}
}
