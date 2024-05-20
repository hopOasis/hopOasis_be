package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;

import com.example.hop_oasis.service.data.BeerServiceImpl;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@MultipartConfig
public class BeerController {
    private final BeerServiceImpl service;
    @GetMapping("/all")
    private List<BeerInfoDto> getAllBeers() {
        return service.getAllBeers();
    }
    @PostMapping("/save")
    public String save(@RequestParam("name") String name,
                       @RequestParam("volume_large") double volumeLarge,
                       @RequestParam("volume_small") double volumeSmall,
                       @RequestParam("price_large") double priceLarge,
                       @RequestParam("price_small") double priceSmall,
                       @RequestParam("description") String description,
                       @RequestParam("color_beer") String colorBeer,
                       @RequestParam("image") MultipartFile image) throws IOException {
        BeerDto beerDto = new BeerDto();
        beerDto.setBeerName(name);
        beerDto.setVolumeLarge(volumeLarge);
        beerDto.setVolumeSmall(volumeSmall);
        beerDto.setPriceLarge(priceLarge);
        beerDto.setPriceSmall(priceSmall);
        beerDto.setDescription(description);
        beerDto.setBearColor(colorBeer);

        service.save(image, beerDto);
        return "Done";
    }
    @PostMapping("/add_image")
    public String addImageToBeer(@RequestParam("beerId") Long beerId,
                                 @RequestParam("image") MultipartFile image) throws IOException {
        service.addImageToBeer(beerId, image);
        return "Done";
    }
    @GetMapping("/beer/{id}")
    public ResponseEntity<BeerInfoDto> getBeerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.getBeerById(id));
    }
    @GetMapping("image/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) throws MalformedURLException {
        ImageDto imajeDto = service.getImageByName(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imajeDto.getImage());
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String>updateBeer(@RequestParam("id") Long id,@RequestBody BeerInfoDto beerInfo) {
        service.update(beerInfo, id);
        return ResponseEntity.ok().body("Done");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().body("Done");
    }
}

