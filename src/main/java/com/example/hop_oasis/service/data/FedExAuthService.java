package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.FedExTokenResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class FedExAuthService {

    @Value("${fedex.auth.url}")
    private String authUrl;

    @Value("${fedex.api.key}")
    private String apiKey;

    @Value("${fedex.api.secret}")
    private String apiSecret;

    private final WebClient.Builder webClientBuilder;

    public String getAccessToken() {
        FedExTokenResponse response = webClientBuilder.build()
                .post()
                .uri(authUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters
                        .fromFormData("grant_type", "client_credentials")
                        .with("client_id", apiKey)
                        .with("client_secret", apiSecret))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> {
                                    System.err.println("FedEx error response: " + error);
                                    return Mono.error(new RuntimeException("FedEx Auth failed: " + error));
                                }))
                .bodyToMono(FedExTokenResponse.class)
                .block();

        return response.getAccessToken();
    }


}
