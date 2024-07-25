package com.example.hop_oasis.service.data;
import com.example.hop_oasis.dto.ProductBundleImageUrlDto;
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
    @Override
    public ProductBundleImageUrlDto getProductBundleImage(String name) {
       return new ProductBundleImageUrlDto(s3Service.getFileUrl(name).toString());
    }
    @Override
    public ProductBundleImageUrlDto addProductBundleImage(Long id, MultipartFile file) {
        try{
            String fileName ="productBundles/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
        }catch (IOException e){
            throw new  ResourceNotFoundException(RESOURCE_NOT_FOUND ,"");
        }
        ProductBundleImage image1 = ProductBundleImage.builder()
                .name(file.getOriginalFilename())
                .build();
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND,""));
        image1.setProductBundle(productBundle);
        productBundleImageRepository.save(image1);
        return new ProductBundleImageUrlDto(s3Service.getFileUrl(image1.getName()).toString());
    }
    @Override
    public void deleteProductBundleImage(String name) {
        Optional<ProductBundleImage> imageOp = productBundleImageRepository.findFirstByName(name);
        if(imageOp.isEmpty()){
            throw  new ResourceNotFoundException(RESOURCE_DELETED,name);
        }
        s3Service.deleteFile("productBundles/"+name);
        productBundleImageRepository.delete(imageOp.get());
    }
}
