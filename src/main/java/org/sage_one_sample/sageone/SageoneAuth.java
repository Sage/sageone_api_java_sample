package org.sage_one_sample.sageone;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SageoneAuth
 */
@WebServlet("/SageoneAuth")
public class SageoneAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Redirect the user to the authorisation url with the required query params
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SageoneConfigs configs = SageoneConfigs.getInstance();
		String clientId = configs.getProperty(SageoneConstants.CLIENT_ID_PROPERTY);
		String callbackUrl = configs.getProperty(SageoneConstants.CALLBACK_URL_PROPERTY);
		String authServer = configs.getProperty(SageoneConstants.AUTH_ENDPOINT_PROPERTY);
		String scope = configs.getProperty(SageoneConstants.SCOPE_PROPERTY);
		String queryParams = "?response_type=code&client_id=" + clientId + "&redirect_uri=" + callbackUrl + "&scope=" + scope;

		resp.sendRedirect(resp.encodeRedirectURL(authServer + queryParams));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
