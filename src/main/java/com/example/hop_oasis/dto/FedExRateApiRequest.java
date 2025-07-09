package com.example.hop_oasis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FedExRateApiRequest {
    private AccountNumber accountNumber;
    private RequestedShipment requestedShipment;

    @Data
    @Builder
    public static class AccountNumber {
        private String value;
    }

    @Data
    @Builder
    public static class RequestedShipment {
        private ContactAndAddress shipper;
        private ContactAndAddress recipient;
        private String pickupType;
        private String[] rateRequestType;
        private PackageLineItem[] requestedPackageLineItems;
    }

    @Data
    @Builder
    public static class ContactAndAddress {
        private Address address;
    }

    @Data
    @Builder
    public static class Address {
        private String postalCode;
        private String countryCode;
    }

    @Data
    @Builder
    public static class PackageLineItem {
        private Weight weight;
    }

    @Data
    @Builder
    public static class Weight {
        private String units;
        private double value;
    }
}
