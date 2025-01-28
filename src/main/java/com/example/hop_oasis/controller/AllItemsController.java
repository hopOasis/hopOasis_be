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
@RequestMapping("/items") // Changed endpoint to "/items" for clarity
@RequiredArgsConstructor
public class AllItemsController {
    private final AllItemsServiceImpl allItemsService;

    @GetMapping
    public ResponseEntity<Page<ItemInfoDto>> getAllItems( //Renamed getAllProducts to getAllItems to align with the service method (allItemsService.getAllItems)
        @ParameterObject 
        @PageableDefault(size = 10, page = 0) Pageable pageable) {
        // Fetch items with pagination
        Page<ItemInfoDto> allItems = allItemsService.getAllItems(pageable);
        return ResponseEntity.ok().body(allItems);

    }
}
