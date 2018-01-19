package facebroke

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.hibernate.Session
import org.slf4j.LoggerFactory
import facebroke.model.User
import facebroke.model.Wall
import facebroke.util.ValidationSnipets
import groovy.transform.PackageScope

@WebServlet("/register")
class Register extends HttpServlet {
	
	def logger = LoggerFactory.getLogger(Register.class)
	
	def Register() {
		super()
	}
	
	void doGet(HttpServletRequest req, HttpServletResponse res) {
		if (ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index")
		}else {
			req.getRequestDispatcher("register.jsp").forward(req,res)
		}
	}
	
	void doPost(HttpServletRequest req, HttpServletResponse res) {
		handleRegistration(req,res)
	}
	
	@PackageScope void handleRegistration(HttpServletRequest req, HttpServletResponse res) {
		
		if (ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index")
			return
		}

		def reqDis = req.getRequestDispatcher("register.jsp")
			
		def username = Encode.forHtml(req.getParameter("regUsername"))
		def email = Encode.forHtml(req.getParameter("regEmail"))
		def fname = Encode.forHtml(req.getParameter("regFirstName"))
		def lname = Encode.forHtml(req.getParameter("regLastName"))
		def dob_raw = Encode.forHtml(req.getParameter("regDOB"))
		def pass1 = Encode.forHtml(req.getParameter("regPassword"))
		def pass2 = Encode.forHtml(req.getParameter("regPasswordConfirm"))
		
		def dob
		
		// THIS IS A DELIBERATE BUG, SHOULD BE FLAGGED AS CRLF INJECTION
		def usernameVuln = req.getParameter("regUsername")
		log.info("Allow CRLF injection: {}", usernameVuln)
		// END BUG
		
		
		req.setAttribute("regUsername", username)
		req.setAttribute("regEmail", email)
		req.setAttribute("regFirstName", fname)
		req.setAttribute("regLastName", lname)
		req.setAttribute("regDOB", dob_raw)
			
		if (!username) {
			req.setAttribute("authMessage", "Username is required")
			reqDis.forward(req, res)
		}
		
		log.info("Received register request with username '{}'",ValidationSnipets.sanitizeCRLF(username))
		log.info("Email: {}",ValidationSnipets.sanitizeCRLF(email))
		log.info("First Name: {}",ValidationSnipets.sanitizeCRLF(fname))
		log.info("Last Name: {}",ValidationSnipets.sanitizeCRLF(lname))
		log.info("DOB: {}",ValidationSnipets.sanitizeCRLF(dob_raw))
		
		if(ValidationSnipets.isUsernameTaken(username)) {
			req.setAttribute("authMessage", "Username already taken")
			reqDis.forward(req, res)
			return
		}
		
		if(email == null || !ValidationSnipets.isValidEmail(email)) {
			req.setAttribute("authMessage", "Invalid email address")
			reqDis.forward(req, res)
			return
		}
		
		if(ValidationSnipets.isEmailTaken(email)) {
			req.setAttribute("authMessage", "Email already taken")
			reqDis.forward(req, res)
			return
		}
		
		if (fname == null || fname.length() < 1) {
			req.setAttribute("authMessage", "First name can't be blank. If you have a mononym, leave Last Name blank")
			reqDis.forward(req, res)
			return
		}
		
		if (!lname) {
			lname = ""
		}
		
		if (!dob_raw) {
			req.setAttribute("authMessage", "Date of Bith can't be blank")
			reqDis.forward(req, res)
			return
		}
		
		try {
			dob = ValidationSnipets.parseDate(dob_raw)
		} catch (e) {
			// Die if DOB can't be parsed
			req.setAttribute("authMessage", "Invalid Date of Birth Format. Need yyyy-mm-dd")
			reqDis.forward(req, res)
			return
		}
		
		if (!pass1 || !pass2) {
			req.setAttribute("authMessage", "Password can't be blank")
			reqDis.forward(req, res)
			return
		}
		
		if (!ValidationSnipets.passwordFormatValid(pass1)) {
			req.setAttribute("authMessage","Password must be at least 8 characters long and contain only a-z,A-z,0-9,!,#,\$,^")
			reqDis.forward(req, res)
			return
		}
		
		if (!pass1.equals(pass2)) {
			req.setAttribute("authMessage", "Passwords don't match");
			reqDis.forward(req, res);
			return;
		}
		
		def u = new User(fname,lname, username, email, dob)
		def w = new Wall(u)
		
		u.updatePassword(pass1)
		u.setWall(w)
		
		Session sess = HibernateUtility.getSessionFactory().openSession()
		sess.beginTransaction()
		sess.save(u)
		sess.save(w)
		sess.getTransaction().commit()
		
		// Finally
		req.setAttribute("authMessage", "Registration Successful, go ahead and login!")
		req.setAttribute("regUsername", null)
		req.setAttribute("regEmail", null)
		req.setAttribute("regFirstName", null)
		req.setAttribute("regLastName", null)
		req.setAttribute("regDOB", null)
		reqDis.forward(req, res)
		sess.close()
	}
}