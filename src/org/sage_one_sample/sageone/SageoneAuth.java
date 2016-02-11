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
    String clientId = SageoneConstants.CLIENT_ID;
    String callbackUrl = SageoneConstants.CALLBACK_URL;
    String authServer = SageoneConstants.AUTH_ENDPOINT;
    String scope = SageoneConstants.SCOPE;
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
