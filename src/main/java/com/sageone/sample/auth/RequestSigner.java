package com.sageone.sample.auth;

import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.util.UriUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestSigner {

    private final String nonce;
    private final String accessToken;
    private final String secret;

    public RequestSigner(String nonce, String accessToken, String secret) {
        this.nonce = nonce;
        this.accessToken = accessToken;
        this.secret = secret;
    }

    public String sign(HttpMethod method, URI uri, byte[] body) throws Exception {

        String baseString =
                method.name().toUpperCase() +
                '&' +
                encode(baseUrl(uri)) +
                '&' +
                encode(paramString(uri, body)) +
                '&' +
                encode(nonce);

        String signingKey =
                encode(secret) +
                '&' +
                encode(accessToken);

        System.out.println(baseString);

        final SecretKeySpec keySpec = new SecretKeySpec(
                signingKey.getBytes(), "HmacSHA1");

        final Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);

        return new String(Base64.encode(mac.doFinal(baseString.getBytes())), StandardCharsets.UTF_8);
    }

    private String baseUrl(URI uri) {

        return uri.getScheme() +
                "://" +
                uri.getAuthority() +
                uri.getPath();
    }

    private String paramString(URI uri, byte[] bytes) {

        // TODO: Formatting?
        final Stream<String> queryStream =
                null == uri.getQuery() ?
                        Stream.empty() :
                        Arrays.stream(uri.getQuery().split("&")).map(
            (String pair) -> Arrays.stream(pair.split("="))
                .map(this::encode)
                .collect(Collectors.joining("="))
        );

        final String body = "body=" +
                encode(new String(Base64.encode(bytes), StandardCharsets.UTF_8));

        return Stream.concat(queryStream, Stream.of(body))
                .sorted()
                .collect(Collectors.joining("&"));
    }

    // This may need to catch some edge cases
    @SuppressWarnings("deprecation")
    private String encode(String str) {
        try {
            return UriUtils.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(str);
        }
    }
}
