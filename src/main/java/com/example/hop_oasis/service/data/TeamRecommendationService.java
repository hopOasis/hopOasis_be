package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ItemMapper;
import com.example.hop_oasis.convertor.TeamRecommendationMapper;
import com.example.hop_oasis.dto.ItemShortInfoDto;
import com.example.hop_oasis.dto.RecommendationRequestDto;
import com.example.hop_oasis.dto.RecommendationResponseDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.TeamRecommendation;
import com.example.hop_oasis.repository.TeamRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamRecommendationService {
    private final TeamRecommendationRepository recommendationRepository;
    private final TeamRecommendationMapper recommendationMapper;
    private final BeerServiceImpl beerService;
    private final CiderServiceImpl ciderService;
    private final ItemMapper itemMapper;

    public RecommendationResponseDto addRecommendation(RecommendationRequestDto recommendationRequestDto) {
        TeamRecommendation recommendation = recommendationMapper.toEntity(recommendationRequestDto);
        recommendationRepository.save(recommendation);
        return mapToResponseDto(recommendation);

    }

    public RecommendationResponseDto updateRecommendation(RecommendationRequestDto recommendationRequestDto, Long id) {
        TeamRecommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        if (Objects.nonNull(recommendationRequestDto.getItemId())) {
            recommendation.setItemId(recommendationRequestDto.getItemId());
        }
        if (Objects.nonNull(recommendationRequestDto.getItemType())) {
            recommendation.setItemType(recommendationRequestDto.getItemType());
        }
        if (Objects.nonNull(recommendationRequestDto.getAuthorName())) {
            recommendation.setAuthorName(recommendationRequestDto.getAuthorName());
        }
        if (Objects.nonNull(recommendationRequestDto.getTeamRole())) {
            recommendation.setTeamRole(recommendationRequestDto.getTeamRole());
        }
        if (Objects.nonNull(recommendationRequestDto.getTextRecommendation())) {
            recommendation.setTextRecommendation(recommendationRequestDto.getTextRecommendation());
        }
        recommendationRepository.save(recommendation);
        return mapToResponseDto(recommendation);
    }

    public List<RecommendationResponseDto> getAllRecommendations() {
        List<TeamRecommendation> recommendations = recommendationRepository.findAll();
        if (recommendations.isEmpty()) {
            throw new ResourceNotFoundException("There are no recommendations", "");
        } else return recommendations.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public RecommendationResponseDto getRecommendationById(Long id) {
        TeamRecommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return mapToResponseDto(recommendation);
    }

    public RecommendationResponseDto deleteRecommendationById(Long id) {
        TeamRecommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_DELETED, id));
        recommendationRepository.deleteById(id);
        return mapToResponseDto(recommendation);
    }

    private String resolveImageUrl(ItemType itemType, Long itemId) {
        return switch (itemType) {
            case BEER -> Optional.ofNullable(beerService.getBeerById(itemId).getImageName())
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElse(null);
            case CIDER -> Optional.ofNullable(ciderService.getCiderById(itemId).getCiderImageName())
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .orElse(null);
            default -> throw new IllegalStateException("Unexpected value: " + itemType);
        };

    }

    private RecommendationResponseDto mapToResponseDto(TeamRecommendation recommendation) {
        return RecommendationResponseDto.builder()
                .id(recommendation.getId())
                .itemId(recommendation.getItemId())
                .itemShortInfoDto(getItemInfo(recommendation.getItemType(), recommendation.getItemId()))
                .authorName(recommendation.getAuthorName())
                .teamRole(recommendation.getTeamRole())
                .textRecommendation(recommendation.getTextRecommendation())
                .imageUrl(resolveImageUrl(recommendation.getItemType(), recommendation.getItemId()))
                .createdAt(recommendation.getCreatedAt())
                .link(buildItemLink(recommendation.getItemType(), recommendation.getItemId()))
                .build();
    }

    private ItemShortInfoDto getItemInfo(ItemType itemType, Long itemId) {
        return switch (itemType) {
            case BEER -> itemMapper.beerToItemInfoDto(beerService.getBeerById(itemId));
            case CIDER -> itemMapper.ciderToItemInfoDto(ciderService.getCiderById(itemId));
            default -> throw new IllegalArgumentException("Unsupported ItemType " + itemType);
        };
    }

    private String buildItemLink(ItemType itemType, Long itemId) {
        return switch (itemType) {
            case BEER -> "/beers/" + itemId;
            case CIDER -> "/ciders/" + itemId;
            default -> throw new IllegalStateException("Unexpected value: " + itemType);
        };
    }
}

