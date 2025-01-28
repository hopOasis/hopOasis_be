package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.data.BeerServiceImpl;
import com.example.hop_oasis.service.data.ImageServiceImpl;
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
    private final BeerServiceImpl beerService;
    private final ImageServiceImpl imageService;
    private final BeerInfoMapper beerInfoMapper;

    // Fetch a paginated list of beers with optional filters
    @GetMapping
    public ResponseEntity<Page<BeerInfoDto>> getAllBeers(
            @ParameterObject 
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "sortDirection", required = false) String sortDirection) {
            Page<BeerInfoDto> beerPage = beerService.getAllBeersWithFilter(beerName, pageable, sortDirection);
        return ResponseEntity.ok().body(beerPage);
    }

    // Create a new beer
    @PostMapping
    public ResponseEntity<BeerInfoDto> save(@RequestBody BeerDto beerDto) {
        BeerInfoDto beerInfoDto = beerInfoMapper.toDto(beerService.save(beerDto));
        return ResponseEntity.ok().body(beerInfoDto);
    }

    // Add an image to a specific beer
    @PostMapping(path = "/{beerId}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BeerInfoDto> addImageToBeer(@PathVariable("beerId") Long beerId,
                                                      @RequestParam("image") MultipartFile image) {
        BeerInfoDto updatedBeer = imageService.addImageToBeer(beerId, image);
        return ResponseEntity.ok().body(updatedBeer); // changed response for consistency
    }

    // Fetch a specific beer by its ID
    @GetMapping("/{id}")
    public ResponseEntity<BeerInfoDto> getBeerById(@PathVariable("id") Long id) {
        BeerInfoDto beerInfo = beerService.getBeerById(id);
        return ResponseEntity.ok().body(beerInfo); // changed response for consistency
    }

    // To add a rating to a beer
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable("id") Long id,
                                       @Valid @RequestBody RatingDto ratingDto) {
        try {
            // removed variable and changed name for clarity
            BeerInfoDto updatedBeer = beerService.addRatingAndReturnUpdatedBeerInfo(id, ratingDto.getRatingValue());
            return ResponseEntity.ok(updatedBeer); 
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // To update an existing beer
    @PutMapping("/{id}")
    public ResponseEntity<BeerInfoDto> updateBeer(@PathVariable("id") Long id,
                                                  @Valid @RequestBody BeerDto beerInfo) {
        BeerInfoDto updatedBeer = beerService.update(beerInfo, id);
        return ResponseEntity.ok().body(updatedBeer);
    }

    // To delete a beer by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<BeerInfoDto> delete(@PathVariable("id") Long id) {
        BeerInfoDto deletedBeer = beerService.delete(id);
        return ResponseEntity.ok().body(deletedBeer);
    }

    // To delete an image by its name for a specific beer
    @DeleteMapping("/{beerId}/images") // Updated the endpoint path for clarity
    public ResponseEntity<String> deleteImage(@PathVariable("beerId") Long beerId, 
                                              @RequestBody ImageUrlDto imageUrl) {
        imageService.deleteImage(imageUrl.getName());
        return ResponseEntity.ok().body("Image deleted successfully: " + imageUrl.getName()); // Return String for clarity
    }
}

