package de.dhbw.elinor2.utils;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class JwtInterceptor implements ClientHttpRequestInterceptor
{

    private final String jwtToken;

    public JwtInterceptor(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        request.getHeaders().set("Authorization", "Bearer " + jwtToken);
        return execution.execute(request, body);
    }
}
