package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProposedProductsDto;
import com.example.hop_oasis.service.advisor.ProposedProducts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {BeerInfoMapper.class, CiderInfoMapper.class, SnackInfoMapper.class, ProductBundleInfoMapper.class})
public interface ProposedProductsMapper extends Mappable<ProposedProducts, ProposedProductsDto> {

}
