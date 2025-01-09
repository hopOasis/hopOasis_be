package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ReviewDto;
import com.example.hop_oasis.dto.ReviewInfoDto;
import com.example.hop_oasis.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "user", ignore = true)
    Review toEntity(ReviewDto reviewDto);

    @Mapping(target = "userId", source = "user.id")
    ReviewDto toReviewDto(Review review);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    ReviewInfoDto toReviewInfoDto(Review review);
    List<ReviewInfoDto> toDtos(List<Review> reviews);

}



