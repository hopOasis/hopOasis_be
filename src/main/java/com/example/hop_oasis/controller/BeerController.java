package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;

import com.example.hop_oasis.service.data.BeerServiceImpl;
import com.example.hop_oasis.service.data.ImageServiceImpl;
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
    private final BeerServiceImpl beerService;
    private final ImageServiceImpl imageService;
    @GetMapping
    private ResponseEntity<List<BeerInfoDto>> getAllBeers() {
        return ResponseEntity.ok().body(beerService.getAllBeers());
    }
    @PostMapping("/beer")
    public String save(@RequestParam("name") String name,
                       @RequestParam("volume_large") double volumeLarge,
                       @RequestParam("volume_small") double volumeSmall,
                       @RequestParam("price_large") double priceLarge,
                       @RequestParam("price_small") double priceSmall,
                       @RequestParam("description") String description,
                       @RequestParam("color_beer") String colorBeer,
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
        return "Done";
    }
    @PostMapping("/beer/add/image")
    public String addImageToBeer(@RequestParam("beerId") Long beerId,
                                 @RequestParam("image") MultipartFile image){
        imageService.addImageToBeer(beerId, image);
        return "Done";
    }
    @GetMapping("/beer/{id}")
    public ResponseEntity<BeerInfoDto> getBeerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(beerService.getBeerById(id));
    }
    @GetMapping("/beer/image/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        ImageDto imajeDto = imageService.getImageByName(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imajeDto.getImage());
    }
    @PutMapping("/beer/{id}")
    public ResponseEntity<String>updateBeer(@RequestParam("id") Long id,@RequestBody BeerInfoDto beerInfo) {
        beerService.update(beerInfo, id);
        return ResponseEntity.ok().body("Done");
    }
    @DeleteMapping("/beer/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        beerService.delete(id);
        return ResponseEntity.ok().body("Done");
    }
    @DeleteMapping("beer/image/{name}")
    public ResponseEntity<String> deleteImage(@PathVariable("name") String name) {
        imageService.deleteImage(name);
        return ResponseEntity.ok().body("Done");
    }
}

