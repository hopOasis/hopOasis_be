package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.SnackImage;
import com.example.hop_oasis.service.SnackImageService;
import com.example.hop_oasis.service.SnackService;
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
@RequestMapping("/snacks")
@MultipartConfig
public class SnackController {
    private final SnackService snackService;
    private final SnackImageService imageService;
    private final SnackInfoMapper snackInfoMapper;

    @GetMapping
    public ResponseEntity<Page<SnackInfoDto>> getAllSnacks(@RequestParam(value = "page",defaultValue = "0") int page,
                                                           @RequestParam(value = "size",defaultValue = "10") int size) {
        Page<SnackInfoDto> snackPage = snackService.getAllSnacks(PageRequest.of(page, size));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "items " + page * size + "-" + ((page + 1) * size - 1) + "/" + snackPage.getTotalElements());

        return ResponseEntity.ok().headers(headers).body(snackPage);
    }
    @PostMapping
    public ResponseEntity<SnackInfoDto> save(@RequestParam("name") String name,
                       @RequestParam("weightLarge") double weightLarge,
                       @RequestParam("weightSmall") double weightSmall,
                       @RequestParam("priceLarge") double priceLarge,
                       @RequestParam("priceSmall")double priceSmall,
                       @RequestParam("description") String description,
                       @RequestParam("image") MultipartFile image){
        SnackDto snackDto = new SnackDto();
        snackDto.setSnackName(name);
        snackDto.setWeightLarge(weightLarge);
        snackDto.setWeightSmall(weightSmall);
        snackDto.setPriceLarge(priceLarge);
        snackDto.setPriceSmall(priceSmall);
        snackDto.setDescription(description);

        SnackInfoDto snackInfoDto = snackInfoMapper.toDto(snackService.saveSnack(image, snackDto));
        return ResponseEntity.ok().body(snackInfoDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SnackInfoDto> getSnackById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(snackService.getSnackById(id));
    }
    @PostMapping("/{id}/ratings")
    public ResponseEntity<SnackInfoDto> addRating(@PathVariable("id") Long id,
                                                  @RequestBody RatingDto ratingDto) {
        double ratingValue = ratingDto.getRatingValue();
        SnackInfoDto dto = snackService.addRatingAndReturnUpdatedSnackInfo(id, ratingValue);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping("/add/image")
    public ResponseEntity<byte[]> addImageToSnack(@RequestParam("snackId") Long snackId,
                                  @RequestParam("image") MultipartFile image){
       SnackImage i = imageService.addSnackImageToSnack(snackId, image);
       SnackImageDto imag = imageService.getSnackImageByName(i.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imag.getImage());
    }
    @GetMapping("/images/{name}")
    public ResponseEntity<byte[]> getSnackImageByName(@PathVariable("name")String name){
        SnackImageDto imageDto = imageService.getSnackImageByName(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageDto.getImage());
    }
    @PutMapping("/{id}")
    public ResponseEntity<SnackInfoDto> updateSnack(@RequestParam("id")Long id,@RequestBody SnackInfoDto snackInfoDto){
        return ResponseEntity.ok().body(snackService.updateSnack(snackInfoDto, id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<SnackInfoDto> deleteSnack(@PathVariable("id")Long id){
        return ResponseEntity.ok().body(snackService.deleteSnack(id));
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<String>deleteSnackImage(@PathVariable("name")String name){
        imageService.deleteSnackImage(name);
        return ResponseEntity.ok().body(name);
    }

}
