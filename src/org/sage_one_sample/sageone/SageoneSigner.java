package org.sage_one_sample.sageone;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SageoneSigner {
	private String method;
	private String url;
	private TreeMap<String, String> params;
	private String nonce;
	private String secret;
	private String token;
	private String guid;

	/**
	* @param $request_method The request method (String)
	* @param $url The url of the request (String)
	* @param $request_body_params TreeMap<String, String> of key value pairs representing the request body
	* @param $nonce The nonce (String)
	* @param $secret Your application's signing_secret (String)
	* @param $token Your access_token obtained during authentication (String)
	* @param $resourceOwnerId Your resource_owner_id obtained during authentication (String)
	*/
	SageoneSigner(String requestMethod, String requestUrl, TreeMap<String, String> requestBodyParams, String requestNonce, String signingSecret, String accessToken, String resourceOwnerId) {
		this.method = requestMethod;
		this.url = requestUrl;
		this.params = requestBodyParams;
		this.nonce = requestNonce;
		this.secret = signingSecret;
		this.token = accessToken;
		this.guid = resourceOwnerId;
	}

	/* Generate the request signature */
	public String signature() {
		String result = null;
		try {
			result = Base64.getEncoder().encodeToString(hmacSha1());
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private byte[] hmacSha1() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		final String type = "HmacSHA1";
		final SecretKeySpec secret = new SecretKeySpec(signingKey().getBytes(), type);
		final Mac mac = Mac.getInstance(type);
		mac.init(secret);
		return mac.doFinal(signatureBaseString().getBytes());
	}

	private String signatureBaseString() {
		String signatureBaseString = "";
		signatureBaseString += method.toUpperCase();
		signatureBaseString += "&";
		signatureBaseString += percentEncode(baseUrl());
		signatureBaseString += "&";
		signatureBaseString += percentEncode(parameterString());
		signatureBaseString += "&";
		signatureBaseString += percentEncode(nonce);
		signatureBaseString += "&";
		signatureBaseString += percentEncode(guid);
		return signatureBaseString;
	}

	private String signingKey() {
		String encodedSigningSecret = percentEncode(secret);
		String encodedAccessToken = percentEncode(token);
		return String.format("%s&%s", encodedSigningSecret, encodedAccessToken);
	}

	private String baseUrl() {
		URL parsedUrl = parsedUrl();
		final String baseUrl = parsedUrl.getProtocol() + "://"
		+ parsedUrl.getAuthority()
		+ parsedUrl.getPath();
		return baseUrl;
	}

	private String parameterString() {
		String parameterString = "";
		TreeMap<String, String> combinedParams = new TreeMap<String,String>();
		combinedParams.putAll(encodedQueryParams());
		combinedParams.putAll(encodedBodyParams());

		for (Map.Entry<String, String> entry : combinedParams.entrySet())
		{
			parameterString += entry.getKey();
			parameterString += "=";
			parameterString += entry.getValue();
			parameterString += "&";
		}
		if (parameterString.length() > 0) {
			parameterString = parameterString.substring(0,parameterString.length()-1);
		}
		return parameterString;
	}

	private TreeMap<String, String> encodedQueryParams() {
		TreeMap<String, String> encodedQueryParams = new TreeMap<String, String>();
		if (parsedUrl().getQuery() != null) {
			String[] queryParams = parsedUrl().getQuery().split("&");

			for( int i = 0; i < queryParams.length; i++)
			{
				String[] param = queryParams[i].split("=");
				encodedQueryParams.put(percentEncode(param[0]), percentEncode(param[1]));
			}
		}
		return encodedQueryParams;
	}

	private TreeMap<String, String> encodedBodyParams() {
		TreeMap<String, String> encodedBodyParams = new TreeMap<String, String>();
		String bodyParams = mapToString(params);
		String encodedBody = Base64.getEncoder().encodeToString(bodyParams.getBytes());
		encodedBodyParams.put("body", percentEncode(encodedBody));

		return encodedBodyParams;
	}

	private URL parsedUrl() {
		URL parsedUrl = null;
		try {
			parsedUrl = new URL(url);
		} catch(MalformedURLException ex) {
			ex.printStackTrace();
		}
		return parsedUrl;
	}

	private String percentEncode(final String string) {
		String encoded = URLEncoder.encode(string).replaceAll("\\+", "%20");
		return encoded;
	}

	private static String mapToString(TreeMap<String, String> map) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String key : map.keySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("&");
			}
			String value = map.get(key);
			try {
				stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
				stringBuilder.append("=");
				stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("This method requires UTF-8 encoding support", e);
			}
		}

		return stringBuilder.toString();
	}
}
