package com.sageone.sample;

import com.sageone.sample.auth.NonceFactory;
import com.sageone.sample.auth.SageOneAuthorizationDetails;
import com.sageone.sample.auth.SageOneSigningInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.Filter;
import java.util.Collections;


@EnableOAuth2Client
@SpringBootApplication
public class Application extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oAuth2ClientContext;

    private Filter ssoFilter() {

        OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter("/login");

        OAuth2RestTemplate template =
                sageTemplate(
                        sage(),
                        oAuth2ClientContext);

        UserInfoTokenServices userServices =
                new UserInfoTokenServices(
                        sageResource().getUserInfoUri(),
                        sage().getClientId());

        userServices.setRestTemplate(template);
        filter.setRestTemplate(template);
        filter.setTokenServices(userServices);

        return filter;
    }

    @Bean
    @SessionScope
    OAuth2RestTemplate sageTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {

        final OAuth2RestTemplate template = new OAuth2RestTemplate(resource, context);

        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setOutputStreaming(false);

        final ClientHttpRequestFactory bufferedFactory =
                new BufferingClientHttpRequestFactory(factory);

        template.setRequestFactory(bufferedFactory);

        template.setInterceptors(Collections.singletonList(
                interceptor(template.getOAuth2ClientContext())));

        return template;
    }

    @Bean
    SageOneSigningInterceptor interceptor(OAuth2ClientContext context) {
        return new SageOneSigningInterceptor(nonce(), context, sage());
    }

    @Bean
    NonceFactory nonce() {
        return new NonceFactory();
    }

    @Bean
    @ConfigurationProperties("sage.client")
    SageOneAuthorizationDetails sage() {
        return new SageOneAuthorizationDetails();
    }

    @Bean
    @ConfigurationProperties("sage.resource")
    ResourceServerProperties sageResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            // TODO fix CSRF
            .csrf().disable()
            .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/sample_app.css")
                .permitAll()
            .anyRequest()
                .authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
