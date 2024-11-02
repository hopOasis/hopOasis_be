package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.BeerService;
import com.example.hop_oasis.service.ImageService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/beers")
@MultipartConfig
@Validated
public class BeerController {
    private final BeerService beerService;
    private final ImageService imageService;
    private final BeerInfoMapper beerInfoMapper;

    @GetMapping
    public ResponseEntity<Page<BeerInfoDto>> getAllBeers(@ParameterObject @PageableDefault (size = 10, page = 0) Pageable pageable,
                                                         @RequestParam(value = "beerName", required = false) String beerName,
                                                         @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        Page<BeerInfoDto> beerPage = beerService.getAllBeersWithFilter(beerName, pageable, sortDirection);
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
                                                  @RequestBody BeerDto beerInfo) {
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

