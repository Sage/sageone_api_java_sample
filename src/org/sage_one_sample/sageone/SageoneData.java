package org.sage_one_sample.sageone;

import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class GetData
 */
@WebServlet("/SageoneData")
public class SageoneData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestMethod = req.getParameter("request_method").toUpperCase();
		String endpoint = "https://api.sageone.com/" + req.getParameter("endpoint");
		TreeMap<String, String> params = new TreeMap<String,String>();
		String nonce = Nonce.generateNonce();
		String signingSecret = SageoneConstants.SIGNING_SECRET;
		String accessToken = req.getParameter("access_token");

		// Generate the signature
		SageoneSigner s = new SageoneSigner(requestMethod, endpoint, params, nonce, signingSecret, accessToken);
		String signature = s.signature();

		//build the request
		HttpClient httpclient = HttpClients.createDefault();
		try {
			URIBuilder builder = new URIBuilder(endpoint);
			URI uri = builder.build();
			HttpRequestBase request = requestMethod.equals("GET") ? new HttpGet(uri) : new HttpDelete(uri);

			// set the request headers
			request.addHeader("X-Signature", signature);
			request.addHeader("X-Nonce", nonce);
			request.addHeader("Authorization", "Bearer " + accessToken);
			request.addHeader("Accept", "*/*");
			request.addHeader("Content-Type", "application/x-www-form-urlencoded");
			request.addHeader("User-Agent", "SageOneSampleApp");

			// Make request
			HttpResponse response = httpclient.execute(request);

			// Render the response
			renderResponse(resp, response);
			request.releaseConnection();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestMethod = req.getParameter("request_method").toUpperCase();
		String endpoint = "https://api.sageone.com/" + req.getParameter("endpoint");
		TreeMap<String, String> params;
		String nonce = Nonce.generateNonce();
		String signingSecret = SageoneConstants.SIGNING_SECRET;
		String accessToken = req.getParameter("access_token");

		// get the body params as a TreeMap
		String jsonBody = req.getParameter("data");
		params = new Gson().fromJson(jsonBody, new TypeToken<TreeMap<String, String>>() {}.getType());

		// Generate the signature
		SageoneSigner s = new SageoneSigner(requestMethod, endpoint, params, nonce, signingSecret, accessToken);
		String signature = s.signature();

		try {
			URIBuilder builder = new URIBuilder(endpoint);
			URI uri = builder.build();

			// Set the post body params
			ArrayList<NameValuePair> postParameters;
			postParameters = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				postParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			// Build the request
			HttpRequestBase request;

			if (requestMethod.equals("POST")) {
				request = new HttpPost(uri);
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(postParameters));
			}
			else
			{
				request = new HttpPut(uri);
				((HttpPut) request).setEntity(new UrlEncodedFormEntity(postParameters));
			}

			// set the request headers
			request.addHeader("X-Signature", signature);
			request.addHeader("X-Nonce", nonce);
			request.addHeader("Authorization", "Bearer " + accessToken);
			request.addHeader("Accept", "*/*");
			request.addHeader("Content-Type", "application/x-www-form-urlencoded");
			request.addHeader("User-Agent", "SageOneSampleApp");

			// Make Request
			HttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(request);

			// Render the response
			renderResponse(resp, response);
			request.releaseConnection();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void renderResponse(HttpServletResponse resp, HttpResponse response) throws IOException {
		// get the response into pretty json for output
		HttpEntity entity = response.getEntity();
		JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(jsonResponse);

		// Render the response
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head>");
		resp.getWriter().println("<link type=\"text/css\" rel=\"stylesheet\" href=\"sample_app.css\">");
		resp.getWriter().println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>");
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
	}
}
