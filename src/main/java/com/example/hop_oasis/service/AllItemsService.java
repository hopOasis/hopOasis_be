package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ItemInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AllItemsService {
    Page<ItemInfoDto> getAllItems(Pageable pageable);

}
