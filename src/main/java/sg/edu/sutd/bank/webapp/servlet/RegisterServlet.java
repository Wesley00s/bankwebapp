package sg.edu.sutd.bank.webapp.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.*;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;
import sg.edu.sutd.bank.webapp.service.ClientInfoDAO;
import sg.edu.sutd.bank.webapp.service.ClientInfoDAOImpl;
import sg.edu.sutd.bank.webapp.service.UserDAO;
import sg.edu.sutd.bank.webapp.service.UserDAOImpl;
import sg.edu.sutd.bank.webapp.service.UserRoleDAO;
import sg.edu.sutd.bank.webapp.service.UserRoleDAOImpl;

@WebServlet("/register")
public class RegisterServlet extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private ClientInfoDAO clientInfoDAO = new ClientInfoDAOImpl();
	private UserDAO userDAO = new UserDAOImpl();
	private UserRoleDAO userRoleDAO = new UserRoleDAOImpl();
	private ClientAccountDAO clientAccountDAO = new ClientAccountDAOImpl();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");

		User user = new User();
		user.setUserName(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setStatus(UserStatus.APPROVED);

		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setFullName(request.getParameter("fullName"));
		clientInfo.setFin(request.getParameter("fin"));
		clientInfo.setDateOfBirth(Date.valueOf(request.getParameter("dateOfBirth")));
		clientInfo.setOccupation(request.getParameter("occupation"));
		clientInfo.setMobileNumber(request.getParameter("mobileNumber"));
		clientInfo.setAddress(request.getParameter("address"));
		clientInfo.setEmail(request.getParameter("email"));
		clientInfo.setUser(user);

		try {
			userDAO.create(user);
			clientInfoDAO.create(clientInfo);

			ClientAccount clientAccount = new ClientAccount();
			clientAccount.setUser(user);
			clientAccount.setAmount(BigDecimal.TEN);
			clientAccountDAO.create(clientAccount);

			UserRole userRole = new UserRole();
			userRole.setUser(user);
			userRole.setRole(Role.client);
			userRoleDAO.create(userRole);

			sendMsg(request, "Registration successful! You can now login.");
			redirect(response, ServletPaths.LOGIN);

		} catch (ServiceException e) {
			sendError(request, "Registration failed: " + e.getMessage());
			forward(request, response);
		}
	}
}