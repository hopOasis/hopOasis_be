package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ProductBundleImageMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleImage;
import com.example.hop_oasis.repository.ProductBundleImageRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.service.ProductBundleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class ProductBundleImageServiceImpl implements ProductBundleImageService {
    private final ProductBundleRepository productBundleRepository;
    private final ProductBundleImageMapper productBundleImageMapper;
    private final ImageCompressor imageCompressor;
    private final ProductBundleImageRepository productBundleImageRepository;
    @Override
    public ProductBundleImageDto getProductBundleImage(String name) {
        Optional<ProductBundleImage> imageOp = productBundleImageRepository.findFirstByName(name);
        if(imageOp.isEmpty()){
            throw  new ResourceNotFoundException(RESOURCE_NOT_FOUND ,name);
        }
        ProductBundleImage image = imageOp.get();
        image.setImage(imageCompressor.decompressImage(image.getImage(), name));
        return productBundleImageMapper.toDto(image);
    }
    @Override
    public ProductBundleImage addProductBundleImage(Long id, MultipartFile file) {
        byte[] image;
        try{
            image = imageCompressor.compressImage(file.getBytes());
        }catch (IOException e){
            throw new  ResourceNotFoundException(RESOURCE_NOT_FOUND ,"");
        }

        ProductBundleImage image1 = ProductBundleImage.builder()
                .image(image)
                .name(file.getOriginalFilename())
                .build();
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND,""));
        image1.setProductBundle(productBundle);
        productBundleImageRepository.save(image1);
        return image1;
    }
    @Override
    public void deleteProductBundleImage(String name) {
        Optional<ProductBundleImage> imageOp = productBundleImageRepository.findFirstByName(name);
        if(imageOp.isEmpty()){
            throw  new ResourceNotFoundException(RESOURCE_DELETED,name);
        }
        productBundleImageRepository.delete(imageOp.get());
    }
}
