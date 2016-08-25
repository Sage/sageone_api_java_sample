package com.sageone.sample.auth;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class SageOneSigningInterceptor implements ClientHttpRequestInterceptor {

    final private NonceFactory nonceFactory;
    final private OAuth2ClientContext context;
    final private String signingSecret;

    public SageOneSigningInterceptor(NonceFactory nonceFactory, OAuth2ClientContext context, SageOneAuthorizationDetails details) {
        this.nonceFactory = nonceFactory;
        this.context = context;
        this.signingSecret = details.getSigningSecret();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        final OAuth2AccessToken accessToken =
                context.getAccessToken();

        final String nonce = nonceFactory.getNonce();

        final RequestSigner signer =
                new RequestSigner(nonce, accessToken.getValue(), signingSecret);

        try {

            final String signature = signer.sign(request.getMethod(), request.getURI(), body);

            request.getHeaders().add("X-Signature", signature);
            request.getHeaders().add("X-Nonce", nonce);
        } catch (Exception e) {

            e.printStackTrace();
        }

        final ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
}
