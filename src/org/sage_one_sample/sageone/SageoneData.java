package org.sage_one_sample.sageone;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class GetData
 */
@WebServlet("/SageoneData")
public class SageoneData extends HttpServlet {
	private static final long serialVersionUID = 1L;

  /**
  * @see HttpServlet#HttpServlet()
  */
  public SageoneData() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
  */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String signingSecret = SageoneConstants.SIGNING_SECRET;
    String accessToken = req.getParameter("access_token");
    String endpoint = "https://api.sageone.com/" + req.getParameter("endpoint");
    String nonce = "e033affd7f6ba0684046620b02932732";
    String authHeader = "Bearer " + accessToken;
    TreeMap<String, String> params = new TreeMap<String, String>();
    SageoneSigner s = new SageoneSigner("get", endpoint, params, nonce, signingSecret, accessToken);
    String signature = s.signature();

    try {
      HttpURLConnection httpConn = HttpUtility.sendGetRequest(endpoint);
      httpConn.setRequestProperty("Authorization", authHeader);
      httpConn.setRequestProperty("X-Signature",  signature);
      httpConn.setRequestProperty("X-Nonce", nonce);
      httpConn.setRequestProperty("Accept", "*/*");
      httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      httpConn.setRequestProperty("User-Agent", "SageOneSampleApp");
      String[] response = HttpUtility.readMultipleLinesResponse();
      JSONObject jsonResponse = new JSONObject(response[0]);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String prettyJson = gson.toJson(jsonResponse);

      resp.getWriter().println("<html>");
      resp.getWriter().println("<head>");
      resp.getWriter().println("<link type=\"text/css\" rel=\"stylesheet\" href=\"sample_app.css\">");
      resp.getWriter().println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
      resp.getWriter().println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">");
      resp.getWriter().println("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>");
      resp.getWriter().println("<title>Response</title>");
      resp.getWriter().println("</head>");
      resp.getWriter().println("<body>");
      resp.getWriter().println("<header class=\"navbar navbar-fixed-top navbar-inverse\">");
      resp.getWriter().println("<div class='container'>");
      resp.getWriter().println("<a id=\"logo\" href=\"/SageOneSampleApp\">Sage One API Sample App</a>");
      resp.getWriter().println("</div>");
      resp.getWriter().println("</header>");
      resp.getWriter().println("<div class='container'>");
      resp.getWriter().println("<h1>Sage One Data</h1>");
      resp.getWriter().println("<pre>" + prettyJson + "</pre>");
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
    // TODO Auto-generated method stub
    doGet(request, response);
  }
}
