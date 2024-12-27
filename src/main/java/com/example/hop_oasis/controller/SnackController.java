package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.data.SnackImageServiceImpl;
import com.example.hop_oasis.service.data.SnackServiceImpl;
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
@RequestMapping("/snacks")
@MultipartConfig
@Validated
public class SnackController {
    private final SnackServiceImpl snackService;
    private final SnackImageServiceImpl imageService;
    private final SnackInfoMapper snackInfoMapper;

    @GetMapping
    public ResponseEntity<Page<SnackInfoDto>> getAllSnacks(@ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable,
                                                           @RequestParam(value = "snackName", required = false) String snackName,
                                                           @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        Page<SnackInfoDto> snackPage = snackService.getAllSnacksWithFilter(snackName, pageable, sortDirection);
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
    public ResponseEntity<SnackInfoDto> updateSnack(@PathVariable("id") Long id,
                                                    @Valid @RequestBody SnackDto snackDto) {
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
