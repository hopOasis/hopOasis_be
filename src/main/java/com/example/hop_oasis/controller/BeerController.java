package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.data.BeerServiceImpl;
import com.example.hop_oasis.service.data.ImageServiceImpl;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/beers")
@MultipartConfig
@Validated
public class BeerController {
    private final BeerServiceImpl beerService;
    private final ImageServiceImpl imageService;
    private final BeerInfoMapper beerInfoMapper;

    @GetMapping
    public ResponseEntity<Page<BeerInfoDto>> getAllBeers(@RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
                                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                                         @RequestParam(value = "beerName", required = false) String beerName,
                                                         @RequestParam(value = "sortDirection", required = false) String sortDirection,
                                                         @RequestParam Map<String, String> allParams) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeerInfoDto> beerPage = beerService.getAllBeersWithFilter(beerName, pageable, sortDirection, allParams);
        return ResponseEntity.ok().body(beerPage);
    }

    @PostMapping
    public ResponseEntity<BeerInfoDto> save(@RequestBody BeerDto beerDto) {
        BeerInfoDto beerInfoDto = beerInfoMapper.toDto(beerService.save(beerDto));
        return ResponseEntity.ok().body(beerInfoDto);
    }

    @PostMapping(path = "/{beerId}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BeerInfoDto> addImageToBeer(@PathVariable("beerId") Long beerId,
                                                      @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().body(imageService.addImageToBeer(beerId, image));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeerInfoDto> getBeerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(beerService.getBeerById(id));
    }

    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable("id") Long id,
                                       @Valid @RequestBody RatingDto ratingDto) {
        try {
            double ratingValue = ratingDto.getRatingValue();
            BeerInfoDto dto = beerService.addRatingAndReturnUpdatedBeerInfo(id, ratingValue);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeerInfoDto> updateBeer(@PathVariable("id") Long id,
                                                  @Valid @RequestBody BeerDto beerInfo) {
        BeerInfoDto dto = beerService.update(beerInfo, id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BeerInfoDto> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(beerService.delete(id));
    }

    @DeleteMapping("/images")
    public ResponseEntity<String> deleteImage(@RequestBody ImageUrlDto name) {
        imageService.deleteImage(name.getName());
        return ResponseEntity.ok().body(name.getName());
    }
}

