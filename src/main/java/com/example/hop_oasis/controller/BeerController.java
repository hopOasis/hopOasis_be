package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageUrlDto;
import com.example.hop_oasis.dto.RatingDto;
import com.example.hop_oasis.service.BeerService;
import com.example.hop_oasis.service.ImageService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
  public ResponseEntity<Page<BeerInfoDto>> getAllBeers(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    Page<BeerInfoDto> beerPage = beerService.getAllBeers(PageRequest.of(page, size));
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
      @RequestBody BeerInfoDto beerInfo) {
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

