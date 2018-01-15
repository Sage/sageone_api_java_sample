package org.sage_one_sample.sageone;

public class SageoneConstants {
	public static final String CLIENT_ID = "YOUR_CLIENT_ID";
	public static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
	public static final String SIGNING_SECRET = "YOUR_SIGNING_SECRET";
	public static final String APIM_SUBSCRIPTION_KEY = "YOUR_APIM_SUBSCRIPTION_KEY";
	public static final String CALLBACK_URL = "YOUR_CALLBACK_URL";
	public static final String SCOPE = "full_access";
	public static final String AUTH_ENDPOINT = "https://www.sageone.com/oauth2/auth/central";

	// use the right endpoint for your country

	// CA, US:
	// public static final String TOKEN_ENDPOINT = "https://oauth.na.sageone.com/token";
	// DE, ES, FR:
	// public static final String TOKEN_ENDPOINT = "https://oauth.eu.sageone.com/token";
	// GB, IE
	public static final String TOKEN_ENDPOINT = "https://app.sageone.com/oauth2/token";

	// CA
	// public static final String BASE_ENDPOINT = "https://api.columbus.sage.com/ca/sageone/";
	// DE
	// public static final String BASE_ENDPOINT = "https://api.columbus.sage.com/de/sageone/";
	// ES
	// public static final String BASE_ENDPOINT = "https://api.columbus.sage.com/es/sageone/";
	// FR
	// public static final String BASE_ENDPOINT = "https://api.columbus.sage.com/fr/sageone/";
	// GB, IE
	public static final String BASE_ENDPOINT = "https://api.columbus.sage.com/uki/sageone/";
	// US
	// public static final String BASE_ENDPOINT = "https://api.columbus.sage.com/us/sageone/";
}
