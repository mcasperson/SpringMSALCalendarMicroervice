package com.matthewcasperson.calendarapi.components;

import com.matthewcasperson.calendarapi.exceptions.AuthException;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.OnBehalfOfParameters;
import com.microsoft.aad.msal4j.UserAssertion;
import com.microsoft.graph.authentication.BaseAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Component
public class OboAuthProvider extends BaseAuthenticationProvider {

    @Value("${azuread.client-id}")
    private String clientId;

    @Value("${azuread.client-secret}")
    private String secret;

    @Value("${azuread.graph-default-scope}")
    private String scope;

    @Override
    public CompletableFuture<String> getAuthorizationTokenAsync(URL url) {

        // Gets incoming access token and generates cache key. The cache key will be used to store
        // the tokens for the incoming request.
        String authToken = this.getAccessTokenFromRequest();

        IAuthenticationResult authResult;
        ConfidentialClientApplication application;
        try {
            application = ConfidentialClientApplication
                    .builder(clientId, ClientCredentialFactory.createFromSecret(secret))
                    .build();

            OnBehalfOfParameters parameters =
                    OnBehalfOfParameters.builder(Collections.singleton(scope),
                                    new UserAssertion(authToken))
                            .build();
            authResult = application.acquireToken(parameters).join();

        } catch (Exception ex) {
            throw new AuthException(String.format("Error acquiring token from AAD: %s", ex.getMessage()),
                    ex.getCause());
        }

        return CompletableFuture.completedFuture(authResult.accessToken());
    }

    String getAccessTokenFromRequest() {
        return ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getToken().getTokenValue();
    }
}