package com.example.hop_oasis.service.data;
import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.decoder.ExtractImageName;
import com.example.hop_oasis.dto.ImageUrlDto;

import com.example.hop_oasis.dto.ProductBundleInfoDto;
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
    private final ProductBundleImageRepository productBundleImageRepository;
    private final S3Service s3Service;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    private final ExtractImageName extractImageName;
    @Override
    public ImageUrlDto getProductBundleImage(String name) {
       return new ImageUrlDto(s3Service.getFileUrl(name).toString());
    }
    @Override
    public ProductBundleInfoDto addProductBundleImage(Long id, MultipartFile file) {
        try{
            String fileName ="productBundles/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
        }catch (IOException e){
            throw new  ResourceNotFoundException(RESOURCE_NOT_FOUND ,"");
        }
        ProductBundleImage image1 = ProductBundleImage.builder()
                .name(s3Service.getFileUrl(file.getOriginalFilename()).toString())
                .build();
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND,""));
        image1.setProductBundle(productBundle);
        productBundleImageRepository.save(image1);
        return productBundleInfoMapper.toDto(productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND,"")));
    }
    @Override
    public void deleteProductBundleImage(String name) {
        Optional<ProductBundleImage> imageOp = productBundleImageRepository.findFirstByName(name);
        if(imageOp.isEmpty()){
            throw  new ResourceNotFoundException(RESOURCE_DELETED,name);
        }
        s3Service.deleteFile("productBundles/"+ extractImageName.extractName(name));
        productBundleImageRepository.delete(imageOp.get());
    }
}
