package com.sageone.sample.controllers;

import com.sageone.sample.models.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class SubmissionController {

    @Autowired
    private OAuth2ClientContext context;

    @Autowired
    private OAuth2RestTemplate rest;

    @Value("${sage.client.baseUri}")
    private String baseUri;

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public String error(HttpClientErrorException e) {
        return e.getResponseBodyAsString();
    }

    @RequestMapping(value = "/submit", method = GET)
    public String get(Model model) {

        model.addAttribute("token", context.getAccessToken().getValue());

        return "submit";
    }

    @RequestMapping(
            value = "/submit",
            method = POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String post(@ModelAttribute Submission submission) throws Exception {

        final String method = submission.getMethod();
        final String endpoint = baseUri + submission.getEndpoint();
        final String body = submission.getBody();

        String response;

        // Explicitly setting JSON mime type here
        final HttpHeaders headers =
                new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        switch (method) {
            case "GET":
                response = rest.getForObject(endpoint, String.class);
                break;
            case "POST":
                response = rest.exchange(
                        endpoint,
                        HttpMethod.POST,
                        new HttpEntity<>(body, headers),
                        String.class).getBody();
                break;
            case "PUT":
                response = rest.exchange(
                        endpoint,
                        HttpMethod.PUT,
                        new HttpEntity<>(body, headers),
                        String.class).getBody();
                break;
            case "DELETE":
                response = rest.exchange(
                        endpoint,
                        HttpMethod.DELETE,
                        new HttpEntity<>(body, headers),
                        String.class).getBody();
                break;
            default:
                throw new Exception("No request method specified");
        }

        return response;
    }
}
