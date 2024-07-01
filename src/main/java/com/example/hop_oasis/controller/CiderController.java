package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderImageDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.service.CiderImageService;
import com.example.hop_oasis.service.CiderService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ciders")
@MultipartConfig
public class CiderController {
    private final CiderService ciderService;
    private final CiderImageService ciderImageService;

    @GetMapping
    public ResponseEntity<List<CiderInfoDto>> getAllCiders(@RequestParam(value = "page",defaultValue = "0") int page,
                                                           @RequestParam(value = "size",defaultValue = "10") int size) {
        Page<CiderInfoDto> ciderPage = ciderService.getAllCiders(PageRequest.of(page, size));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "items " + page * size + "-" + ((page + 1) * size - 1) + "/" + ciderPage.getTotalElements());

        return ResponseEntity.ok().headers(headers).body(ciderPage.getContent());
    }
    @PostMapping
    public ResponseEntity<Void> save(@RequestParam("name") String name,
                                     @RequestParam("volumeLarge") double volumeLarge,
                                     @RequestParam("volumeSmall") double volumeSmall,
                                     @RequestParam("priceLarge") double priceLarge,
                                     @RequestParam("priceSmall") double priceSmall,
                                     @RequestParam("description") String description,
                                     @RequestParam("image") MultipartFile image) {

        CiderDto ciderDto = new CiderDto();
        ciderDto.setCiderName(name);
        ciderDto.setVolumeLarge(volumeLarge);
        ciderDto.setVolumeSmall(volumeSmall);
        ciderDto.setPriceLarge(priceLarge);
        ciderDto.setPriceSmall(priceSmall);
        ciderDto.setDescription(description);

        ciderService.saveCider(image, ciderDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/add/image")
    public ResponseEntity<Void> addImageToCider(@RequestParam("ciderId") Long ciderId,
                                                @RequestParam("image") MultipartFile image) {
        ciderImageService.addCiderImageToCider(ciderId, image);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<CiderInfoDto> getCiderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(ciderService.getCiderById(id));
    }
    @GetMapping("/images/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        CiderImageDto ciderImageDto = ciderImageService.getCiderImageByName(name);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(ciderImageDto.getImage());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCider(@RequestParam("id") Long id,
                                            @RequestBody CiderInfoDto ciderInfo) {
        ciderService.update(ciderInfo, id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCider(@PathVariable("id") Long id) {
        ciderService.deleteCider(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<Void> deleteCiderImage(@PathVariable("name") String name) {
        ciderImageService.deleteCiderImage(name);
        return ResponseEntity.ok().build();
    }
}
