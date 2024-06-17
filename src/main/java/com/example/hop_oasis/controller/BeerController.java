package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;

import com.example.hop_oasis.service.BeerService;
import com.example.hop_oasis.service.ImageService;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/beers")
@MultipartConfig
public class BeerController {
    private final BeerService beerService;
    private final ImageService imageService;
    @GetMapping
    public ResponseEntity<List<BeerInfoDto>> getAllBeers() {
        return ResponseEntity.ok().body(beerService.getAllBeers());
    }
    @PostMapping
    public ResponseEntity<Void> save(@RequestParam("name") String name,
                       @RequestParam("volumeLarge") double volumeLarge,
                       @RequestParam("volumeSmall") double volumeSmall,
                       @RequestParam("priceLarge") double priceLarge,
                       @RequestParam("priceSmall") double priceSmall,
                       @RequestParam("description") String description,
                       @RequestParam("colorBeer") String colorBeer,
                       @RequestParam("image") MultipartFile image) {
        BeerDto beerDto = new BeerDto();
        beerDto.setBeerName(name);
        beerDto.setVolumeLarge(volumeLarge);
        beerDto.setVolumeSmall(volumeSmall);
        beerDto.setPriceLarge(priceLarge);
        beerDto.setPriceSmall(priceSmall);
        beerDto.setDescription(description);
        beerDto.setBearColor(colorBeer);

        beerService.save(image, beerDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/add/image")
    public ResponseEntity<Void> addImageToBeer(@RequestParam("beerId") Long beerId,
                                 @RequestParam("image") MultipartFile image){
        imageService.addImageToBeer(beerId, image);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<BeerInfoDto> getBeerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(beerService.getBeerById(id));
    }
    @GetMapping("/images/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        ImageDto imajeDto = imageService.getImageByName(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imajeDto.getImage());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void>updateBeer(@RequestParam("id") Long id,
                                          @RequestBody BeerInfoDto beerInfo) {
        beerService.update(beerInfo, id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        beerService.delete(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<Void> deleteImage(@PathVariable("name") String name) {
        imageService.deleteImage(name);
        return ResponseEntity.ok().build();
    }
}

