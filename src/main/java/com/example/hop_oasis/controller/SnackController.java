package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.SnackImageService;
import com.example.hop_oasis.service.SnackService;
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
@RequestMapping("/snacks")
@MultipartConfig
@Validated
public class SnackController {
    private final SnackService snackService;
    private final SnackImageService imageService;
    private final SnackInfoMapper snackInfoMapper;

    @GetMapping
    public ResponseEntity<Page<SnackInfoDto>> getAllSnacks(@RequestParam(
                                                                   value = "page", defaultValue = "0") int page,
                                                           @RequestParam(
                                                                   value = "size", defaultValue = "10") int size) {
        Page<SnackInfoDto> snackPage = snackService.getAllSnacks(PageRequest.of(page, size));
        return ResponseEntity.ok().body(snackPage);
    }
    @PostMapping
    public ResponseEntity<SnackInfoDto> save(@RequestBody SnackDto snackDto) {
        SnackInfoDto snackInfoDto = snackInfoMapper.toDto(snackService.saveSnack(snackDto));
        return ResponseEntity.ok().body(snackInfoDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SnackInfoDto> getSnackById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(snackService.getSnackById(id));
    }
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable("id") Long id,
                                       @Valid @RequestBody RatingDto ratingDto) {
        try {
            double ratingValue = ratingDto.getRatingValue();
            SnackInfoDto dto = snackService.addRatingAndReturnUpdatedSnackInfo(id, ratingValue);
            return ResponseEntity.ok().body(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PostMapping(path = "/{snackId}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SnackInfoDto> addImageToSnack(@PathVariable("snackId") Long snackId,
                                  @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().body(imageService.addSnackImageToSnack(snackId, image));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SnackInfoDto> updateSnack(@RequestParam("id") Long id,
                                                    @RequestBody SnackDto snackDto) {
        return ResponseEntity.ok().body(snackService.updateSnack(snackDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SnackInfoDto> deleteSnack(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(snackService.deleteSnack(id));
    }
    @DeleteMapping("/images")
    public ResponseEntity<String> deleteSnackImage(@RequestBody ImageUrlDto name) {
        imageService.deleteSnackImage(name.getName());
        return ResponseEntity.ok().body(name.getName());
    }

}
