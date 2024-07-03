package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderImageDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.CiderImage;
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
    private final CiderInfoMapper ciderInfoMapper;

    @GetMapping
    public ResponseEntity<Page<CiderInfoDto>> getAllCiders(@RequestParam(value = "page",defaultValue = "0") int page,
                                                           @RequestParam(value = "size",defaultValue = "10") int size) {
        Page<CiderInfoDto> ciderPage = ciderService.getAllCiders(PageRequest.of(page, size));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "items " + page * size + "-" + ((page + 1) * size - 1) + "/" + ciderPage.getTotalElements());

        return ResponseEntity.ok().headers(headers).body(ciderPage);
    }
    @PostMapping
    public ResponseEntity<CiderInfoDto> save(@RequestParam("name") String name,
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

       CiderInfoDto ciderInfoDto = ciderInfoMapper.toDto(ciderService.saveCider(image, ciderDto));
        return ResponseEntity.ok().body(ciderInfoDto);
    }
    @PostMapping("/add/image")
    public ResponseEntity<byte[]> addImageToCider(@RequestParam("ciderId") Long ciderId,
                                                @RequestParam("image") MultipartFile image) {
        CiderImage i = ciderImageService.addCiderImageToCider(ciderId, image);
        CiderImageDto imag = ciderImageService.getCiderImageByName(i.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imag.getImage());
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
    public ResponseEntity<CiderInfoDto> updateCider(@RequestParam("id") Long id,
                                            @RequestBody CiderInfoDto ciderInfo) {

        return ResponseEntity.ok().body(ciderService.update(ciderInfo, id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CiderInfoDto> deleteCider(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(ciderService.deleteCider(id));
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<String> deleteCiderImage(@PathVariable("name") String name) {
        ciderImageService.deleteCiderImage(name);
        return ResponseEntity.ok().body(name);
    }
}
