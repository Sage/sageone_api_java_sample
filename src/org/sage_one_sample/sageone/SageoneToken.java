package org.sage_one_sample.sageone;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Servlet implementation class AccessToken
 */
@WebServlet("/SageoneToken")
public class SageoneToken extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * POST request to exchange authorisation code for access_token
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURL = SageoneConstants.TOKEN_ENDPOINT;
		String clientId = SageoneConstants.CLIENT_ID;
		String secret = SageoneConstants.CLIENT_SECRET;
		String callbackUrl = SageoneConstants.CALLBACK_URL;
		String code = req.getParameter("code");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("client_secret", secret));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("redirect_uri", callbackUrl));

		try {
			HttpPost request = new HttpPost(requestURL);
			request.setEntity(new UrlEncodedFormEntity(params));
			HttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(request);

			HttpEntity entity = response.getEntity();
			JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
			String token = jsonResponse.getString("access_token");

			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<link type=\"text/css\" rel=\"stylesheet\" href=\"../sample_app.css\">");
			resp.getWriter().println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>");
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
			resp.getWriter().println("<ul class='nav nav-tabs' role='tablist'>");
			resp.getWriter().println("<li role='presentation' class='active'><a href='#get' aria-controls='get' role='tab' data-toggle='tab'>GET</a></li>");
			resp.getWriter().println("<li role='presentation'><a href='#post' aria-controls='post' role='tab' data-toggle='tab'>POST</a></li>");
			resp.getWriter().println("<li role='presentation'><a href='#put' aria-controls='put' role='tab' data-toggle='tab'>PUT</a></li>");
			resp.getWriter().println("<li role='presentation'><a href='#delete' aria-controls='delete' role='tab' data-toggle='tab'>DELETE</a></li></ul>");
			resp.getWriter().println("<div class='tab-content'>");
			resp.getWriter().println("<div role='tabpanel' class='tab-pane fade in active' id='get'>");
			resp.getWriter().println("<h3>GET request</h3>");
			resp.getWriter().println("<form action='/SageOneSampleApp/sageone_data' method='get'>");
			resp.getWriter().println("<input name='request_method' type='hidden' value='get'>");
			resp.getWriter().println("<label for='endpoint'>Endpoint</label>");
			resp.getWriter().println("<input name='endpoint' type='text' class='form-control' required='true'>");
			resp.getWriter().println("<p>Example: accounts/v1/contacts</p>");
			resp.getWriter().println("<input name='data' type='hidden' value=''>");
			resp.getWriter().println("<input name='access_token' type='hidden' value='" + token + "'>");
			resp.getWriter().println("<input type='submit' value='GET' class='btn btn-primary'>");
			resp.getWriter().println("</form>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("<div role='tabpanel' class='tab-pane fade' id='post'>");
			resp.getWriter().println("<h3>POST request</h3>");
			resp.getWriter().println("<form action='/SageOneSampleApp/sageone_data' method='post'>");
			resp.getWriter().println("<input name='request_method' type='hidden' value='post'>");
			resp.getWriter().println("<label for='endpoint'>Endpoint</label>");
			resp.getWriter().println("<input name='endpoint' type='text' class='form-control' required='true'>");
			resp.getWriter().println("<p>Example: accounts/v1/contacts</p>");
			resp.getWriter().println("<label for='data'>Post data</label>");
			resp.getWriter().println("<textarea id='data' class='form-control' name='data'></textarea>");
			resp.getWriter().println("<p>Example: {'contact[contact_type_id]': 1, 'contact[name]': 'Joe Bloggs'}</p>");
			resp.getWriter().println("<input name='access_token' type='hidden' value='" + token + "'>");
			resp.getWriter().println("<input type='submit' value='POST' class='btn btn-primary'>");
			resp.getWriter().println("</form>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("<div role='tabpanel' class='tab-pane fade' id='put'>");
			resp.getWriter().println("<h3>PUT request</h3>");
			resp.getWriter().println("<form action='/SageOneSampleApp/sageone_data' method='post'>");
			resp.getWriter().println("<input name='request_method' type='hidden' value='put'>");
			resp.getWriter().println("<label for='endpoint'>Endpoint</label>");
			resp.getWriter().println("<input name='endpoint' type='text' class='form-control' required='true'>");
			resp.getWriter().println("<p>Example: accounts/v1/contacts/:id</p>");
			resp.getWriter().println("<label for='data'>Post data</label>");
			resp.getWriter().println("<textarea id='data' class='form-control' name='data'></textarea>");
			resp.getWriter().println("<p>Example: {'contact[contact_type_id]': 1, 'contact[name]': 'Joe Bloggs'}</p>");
			resp.getWriter().println("<input name='access_token' type='hidden' value='" + token + "'>");
			resp.getWriter().println("<input type='submit' value='PUT' class='btn btn-primary'>");
			resp.getWriter().println("</form>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("<div role='tabpanel' class='tab-pane fade' id='delete'>");
			resp.getWriter().println("<h3>DELETE request</h3>");
			resp.getWriter().println("<form action='/SageOneSampleApp/sageone_data' method='get'>");
			resp.getWriter().println("<input name='request_method' type='hidden' value='delete'>");
			resp.getWriter().println("Endpoint: <input name='endpoint' type='text' class='form-control' required='true'>");
			resp.getWriter().println("<p>Example: accounts/v1/contacts/:id</p>");
			resp.getWriter().println("<input name='data' type='hidden' value=''>");
			resp.getWriter().println("<input name='access_token' type='hidden' value='" + token + "'>");
			resp.getWriter().println("<input type='submit' value='DELETE' class='btn btn-primary'>");
			resp.getWriter().println("</form>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("</div>");
			resp.getWriter().println("</body>");
			resp.getWriter().println("</html>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
