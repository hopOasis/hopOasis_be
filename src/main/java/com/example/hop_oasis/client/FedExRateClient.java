package com.example.hop_oasis.client;

import com.example.hop_oasis.dto.FedExRateApiRequest;
import com.example.hop_oasis.dto.FedExRateApiResponse;
import com.example.hop_oasis.dto.FedExRateRequestDto;
import com.example.hop_oasis.service.data.FedExAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class FedExRateClient {
    private final FedExAuthService authService;
    private final WebClient.Builder webClientBuilder;

    @Value("${fedex.base.url}")
    private String fedexBaseUrl;
    @Value("${fedex.account.number}")
    private String fedexAccNumber;

    public BigDecimal getRate(FedExRateRequestDto requestDto) {
        String token = authService.getAccessToken();

        FedExRateApiRequest body = buildRequest(requestDto);

        try {
            FedExRateApiResponse response = webClientBuilder.build()
                    .post()
                    .uri(fedexBaseUrl + "/rate/v1/rates/quotes")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(FedExRateApiResponse.class)
                    .block();

            return extractAmount(response);

        } catch (Exception e) {
            log.error("Error getting rate from FedEx: {}", e.getMessage());
            return BigDecimal.valueOf(75.00);
        }
    }

    private FedExRateApiRequest buildRequest(FedExRateRequestDto dto) {

        return FedExRateApiRequest.builder()
                .accountNumber(FedExRateApiRequest.AccountNumber.builder()
                        .value(fedexAccNumber)
                        .build())
                .requestedShipment(FedExRateApiRequest.RequestedShipment.builder()
                        .shipper(FedExRateApiRequest.ContactAndAddress.builder()
                                .address(FedExRateApiRequest.Address.builder()
                                        .postalCode("01001")
                                        .countryCode("UA")
                                        .build())
                                .build())
                        .recipient(FedExRateApiRequest.ContactAndAddress.builder()
                                .address(FedExRateApiRequest.Address.builder()
                                        .postalCode(dto.getRecipientPostalCode())
                                        .countryCode("UA")
                                        .build())
                                .build())
                        .pickupType("DROPOFF_AT_FEDEX_LOCATION")
                        .rateRequestType(new String[]{"LIST"})
                        .requestedPackageLineItems(new FedExRateApiRequest.PackageLineItem[]{
                                FedExRateApiRequest.PackageLineItem.builder()
                                        .weight(FedExRateApiRequest.Weight.builder()
                                                .units("KG")
                                                .value(dto.getWeight())
                                                .build())
                                        .build()
                        })
                        .build())
                .build();
    }

    private BigDecimal extractAmount(FedExRateApiResponse response) {
        try {
            return response.getOutput()
                    .getRateReplyDetails()
                    .get(0)
                    .getRatedShipmentDetails()
                    .get(0)
                    .getTotalNetCharge();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract amount from FedEx response", e);
        }
    }
}

