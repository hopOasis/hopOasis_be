package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;

import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.service.BeerService;
import com.example.hop_oasis.service.ImageService;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/beers")
@MultipartConfig
public class BeerController {
    private final BeerService beerService;
    private final ImageService imageService;
    private final BeerInfoMapper beerInfoMapper;

    @GetMapping
    public ResponseEntity<Page<BeerInfoDto>> getAllBeers(@RequestParam(value = "page",defaultValue = "0") int page,
                                                         @RequestParam(value = "size",defaultValue = "10") int size){
      Page<BeerInfoDto> beerPage = beerService.getAllBeers(PageRequest.of(page, size));
      HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "items " + page * size + "-"
                + ((page + 1) * size - 1) + "/" + beerPage.getTotalElements());

        return ResponseEntity.ok().headers(headers).body(beerPage);
    }
    @PostMapping
    public ResponseEntity<BeerInfoDto> save(@RequestParam("name") String name,
                       @RequestParam("volumeLarge") double volumeLarge,
                       @RequestParam("volumeSmall") double volumeSmall,
                       @RequestParam("priceLarge") double priceLarge,
                       @RequestParam("priceSmall") double priceSmall,
                       @RequestParam("description") String description,
                       @RequestParam("beerColor") String beerColor,
                       @RequestParam("image") MultipartFile image) {
        BeerDto beerDto = new BeerDto();
        beerDto.setBeerName(name);
        beerDto.setVolumeLarge(volumeLarge);
        beerDto.setVolumeSmall(volumeSmall);
        beerDto.setPriceLarge(priceLarge);
        beerDto.setPriceSmall(priceSmall);
        beerDto.setDescription(description);
        beerDto.setBeerColor(beerColor);

        BeerInfoDto beerInfoDto = beerInfoMapper.toDto(beerService.save(image, beerDto));

        return ResponseEntity.ok().body(beerInfoDto);
    }
    @PostMapping("/add/image")
    public ResponseEntity<byte[]> addImageToBeer(@RequestParam("beerId") Long beerId,
                                 @RequestParam("image") MultipartFile image){
        Image i = imageService.addImageToBeer(beerId, image);
       ImageDto imag = imageService.getImageByName(i.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imag.getImage());
    }
    @GetMapping("/{id}")
    public ResponseEntity<BeerInfoDto> getBeerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(beerService.getBeerById(id));
    }
    @PostMapping("/{id}/rating")
    public ResponseEntity<BeerInfoDto> addRating(@PathVariable("id") Long id,
                                          @RequestParam("rating") double rating){
       BeerInfoDto dto = beerService.addRatingAndReturnUpdatedBeerInfo(id, rating);
        return ResponseEntity.ok().body(dto);

    }
    @GetMapping("/images/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        ImageDto imajeDto = imageService.getImageByName(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imajeDto.getImage());
    }
    @PutMapping("/{id}")
    public ResponseEntity<BeerInfoDto>updateBeer(@PathVariable("id")Long id,
                                          @RequestBody BeerInfoDto beerInfo) {
       BeerInfoDto dto = beerService.update(beerInfo, id);
        return ResponseEntity.ok().body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BeerInfoDto> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(beerService.delete(id));
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<String> deleteImage(@PathVariable("name") String name) {
        imageService.deleteImage(name);
        return ResponseEntity.ok().body(name);
    }
}

