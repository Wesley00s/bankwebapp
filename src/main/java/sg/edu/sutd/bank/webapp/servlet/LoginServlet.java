package sg.edu.sutd.bank.webapp.servlet;

import java.io.IOException;
import static sg.edu.sutd.bank.webapp.servlet.ServletPaths.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.model.UserStatus;
import sg.edu.sutd.bank.webapp.service.UserDAO;
import sg.edu.sutd.bank.webapp.service.UserDAOImpl;

@WebServlet(LOGIN)
public class LoginServlet extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAOImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user;
        try {
            user = userDAO.loadUser(username);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        try {

            if (user == null) {
                sendError(req, "Usuário não encontrado!");
                forward(req, resp);
                return;
            }

			if (!user.getPassword().equals(password)) {
				sendError(req, "Senha inválida!");
				forward(req, resp);
				return;
			}

            if (user.getStatus() != UserStatus.APPROVED) {
                sendError(req, "Conta não aprovada. Aguarde a ativação!");
                forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("authenticatedUser", username);
            setUserId(req, user.getId());

            redirect(resp, CLIENT_DASHBOARD_PAGE);

        } catch (ServletException e) {
            assert user != null;
            sendError(req, "Credenciais inválidas! " + user.getUserName() + " - " + user.getPassword());
            forward(req, resp);
        }
    }
}
