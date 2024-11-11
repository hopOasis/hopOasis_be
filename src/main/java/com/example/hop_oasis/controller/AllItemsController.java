package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.ItemInfoDto;
import com.example.hop_oasis.service.data.AllItemsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/all-products")
@RequiredArgsConstructor
public class AllItemsController {
    private final AllItemsServiceImpl allItemsService;
    @GetMapping
    public ResponseEntity<Page<ItemInfoDto>> getAllProducts(@ParameterObject
                                                                @PageableDefault(size = 10, page = 0)Pageable pageable) {
        Page<ItemInfoDto> allItems = allItemsService.getAllItems(pageable);
        return ResponseEntity.ok().body(allItems);

    }
}
