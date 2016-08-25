package com.sageone.sample.auth;

import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class SageOneAuthorizationDetails extends AuthorizationCodeResourceDetails {

    private String signingSecret;

    public String getSigningSecret() {
        return this.signingSecret;
    }

    public void setSigningSecret(String signingSecret) {
        this.signingSecret = signingSecret;
    }
}
