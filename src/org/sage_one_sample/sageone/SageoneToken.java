package org.sage_one_sample.sageone;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.json.*;

/**
 * Servlet implementation class AccessToken
 */
@WebServlet("/SageoneToken")
public class SageoneToken extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
  */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String requestURL = SageoneConstants.TOKEN_ENDPOINT;
    String clientId = SageoneConstants.CLIENT_ID;
    String secret = SageoneConstants.CLIENT_SECRET;
    String callbackUrl = SageoneConstants.CALLBACK_URL;
    String code = req.getParameter("code");
    Map<String, String> params = new HashMap<String, String>();

    params.put("client_id", clientId);
    params.put("client_secret", secret);
    params.put("code", code);
    params.put("grant_type", "authorization_code");
    params.put("redirect_uri", callbackUrl);

    try {
      HttpUtility.sendPostRequest(requestURL, params);
      String[] response = HttpUtility.readMultipleLinesResponse();
      JSONObject jsonResponse = new JSONObject(response[0]);
      String token = jsonResponse.getString("access_token");

      resp.getWriter().println("<html>");
      resp.getWriter().println("<head>");
      resp.getWriter().println("<link type=\"text/css\" rel=\"stylesheet\" href=\"../sample_app.css\">");
      resp.getWriter().println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
      resp.getWriter().println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">");
      resp.getWriter().println("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>");
      resp.getWriter().println("<title>This is the response</title>");
      resp.getWriter().println("</head>");
      resp.getWriter().println("<body>");
      resp.getWriter().println("<header class=\"navbar navbar-fixed-top navbar-inverse\">");
      resp.getWriter().println("<div class='container'>");
      resp.getWriter().println("<a id=\"logo\" href=\"/SageOneSampleApp\">Sage One API Sample App</a>");
      resp.getWriter().println("</div>");
      resp.getWriter().println("</header>");
      resp.getWriter().println("<div class='container'>");
      resp.getWriter().println("<div class='col-md-6 col-md-offset-3'>");
      resp.getWriter().println("<h3>Successfully authenticated!</h3>");
      resp.getWriter().println("<p>Your Access Token is: " + token + "</p>");
      resp.getWriter().println("<form action='/SageOneSampleApp/sageone_data' method='get'>");
      resp.getWriter().println("Endpoint: <input name='endpoint' type='text' class='form-control' required='true'>");
      resp.getWriter().println("<input name='access_token' type='hidden' value='" + token + "'>");
      resp.getWriter().println("<input type='submit' value='GET' class='btn btn-primary'>");
      resp.getWriter().println("</form>");
      resp.getWriter().println("</div>");
      resp.getWriter().println("</div>");
      resp.getWriter().println("</body>");
      resp.getWriter().println("</html>");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    HttpUtility.disconnect();
  }

	/**
  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
  */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}
