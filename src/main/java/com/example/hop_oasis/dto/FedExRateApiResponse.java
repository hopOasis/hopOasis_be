package com.example.hop_oasis.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FedExRateApiResponse {
    private Output output;

    @Data
    public static class Output {
        private List<RateReplyDetail> rateReplyDetails;
    }

    @Data
    public static class RateReplyDetail {
        private List<RatedShipmentDetail> ratedShipmentDetails;
    }

    @Data
    public static class RatedShipmentDetail {
        private BigDecimal totalNetCharge;
    }

}
