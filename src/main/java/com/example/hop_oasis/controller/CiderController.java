package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.CiderImageService;
import com.example.hop_oasis.service.CiderService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ciders")
@MultipartConfig
@Validated
public class CiderController {
    private final CiderService ciderService;
    private final CiderImageService ciderImageService;
    private final CiderInfoMapper ciderInfoMapper;

    @GetMapping
    public ResponseEntity<Page<CiderInfoDto>> getAllCiders(@RequestParam(
                                                                   value = "page", defaultValue = "0") int page,
                                                           @RequestParam(
                                                                   value = "size", defaultValue = "10") int size) {
        Page<CiderInfoDto> ciderPage = ciderService.getAllCiders(PageRequest.of(page, size));
        return ResponseEntity.ok().body(ciderPage);
    }
    @PostMapping
    public ResponseEntity<CiderInfoDto> save(@RequestBody CiderDto ciderDto) {
        CiderInfoDto ciderInfoDto = ciderInfoMapper.toDto(ciderService.saveCider(ciderDto));
        return ResponseEntity.ok().body(ciderInfoDto);
    }
    @PostMapping(path = "/{ciderId}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CiderInfoDto> addImageToCider(@PathVariable("ciderId") Long ciderId,
                                                @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().body(ciderImageService.addCiderImageToCider(ciderId, image));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CiderInfoDto> getCiderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(ciderService.getCiderById(id));
    }
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable("id") Long id,
                                       @Valid @RequestBody RatingDto ratingDto) {
        try {
            double ratingValue = ratingDto.getRatingValue();
            CiderInfoDto dto = ciderService.addRatingAndReturnUpdatedCiderInfo(id, ratingValue);
            return ResponseEntity.ok().body(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
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
    @DeleteMapping("/images")
    public ResponseEntity<String> deleteCiderImage(@RequestBody ImageUrlDto name) {
        ciderImageService.deleteCiderImage(name.getName());
        return ResponseEntity.ok().body(name.getName());
    }
}
