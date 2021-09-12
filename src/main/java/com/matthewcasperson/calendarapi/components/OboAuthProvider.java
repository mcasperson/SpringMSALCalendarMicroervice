package com.matthewcasperson.calendarapi.components;

import com.google.common.hash.Hashing;
import com.matthewcasperson.calendarapi.exceptions.AuthException;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.OnBehalfOfParameters;
import com.microsoft.aad.msal4j.UserAssertion;
import com.microsoft.graph.authentication.BaseAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Component
class OboAuthProvider extends BaseAuthenticationProvider {

    //@Value("${security.oauth2.client.authority}")
    private String authority;

    //@Value("${security.oauth2.client.client-id}")
    private String clientId;

    //@Value("${security.oauth2.client.client-secret}")
    private String secret;

    //@Value("${aad.graphDefaultScope}")
    private String scope;

    //@Autowired
    CacheManager cacheManager;

    @Override
    public CompletableFuture<String> getAuthorizationTokenAsync(URL url) {

        // Gets incoming access token and generates cache key. The cache key will be used to store
        // the tokens for the incoming request.
        String authToken = this.getAccessTokenFromRequest();
        String cacheKey = Hashing.sha256().hashString(authToken, StandardCharsets.UTF_8).toString();

        IAuthenticationResult authResult;
        ConfidentialClientApplication application;
        try {
            application = ConfidentialClientApplication
                    .builder(clientId, ClientCredentialFactory.createFromSecret(secret))
                    .authority(authority)
                    .build();

            String cachedTokens = cacheManager.getCache("tokens").get(cacheKey, String.class);
            if (cachedTokens != null) {
                application.tokenCache().deserialize(cachedTokens);
            }

            OnBehalfOfParameters parameters =
                    OnBehalfOfParameters.builder(Collections.singleton(scope),
                                    new UserAssertion(authToken))
                            .build();
            authResult = application.acquireToken(parameters).join();

        } catch (Exception ex) {
            throw new AuthException(String.format("Error acquiring token from AAD: %s", ex.getMessage()),
                    ex.getCause());
        }

        cacheManager.getCache("tokens").put(cacheKey, application.tokenCache().serialize());
        return CompletableFuture.completedFuture(authResult.accessToken());
    }

    String getAccessTokenFromRequest() {
        return ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getToken().getTokenValue();
    }
}