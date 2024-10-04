package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "itemType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ItemRequestDtoWithMeasure.class, name = "BEER"),
        @JsonSubTypes.Type(value = ItemRequestDtoWithMeasure.class, name = "CIDER"),
        @JsonSubTypes.Type(value = ItemRequestDtoWithMeasure.class, name = "SNACK"),
        @JsonSubTypes.Type(value = ProductBundleRequestDto.class, name = "PRODUCT_BUNDLE")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class ItemRequestDto {
    private Long itemId;
    private int quantity;
    private ItemType itemType;
}
