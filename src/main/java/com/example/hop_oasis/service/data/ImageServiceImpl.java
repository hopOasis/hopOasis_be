package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ImageRepository;
import com.example.hop_oasis.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.extractor.ImageNameExtractor.extractName;
import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final BeerRepository beerRepository;
    private final S3Service s3Service;
    private final BeerInfoMapper beerInfoMapper;


    @Override
    public BeerInfoDto addImageToBeer(Long beerId, MultipartFile file) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, beerId));
        String fileName;
        try {
             fileName = "beers/"+ file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        Image image1 = Image.builder()
                .name(s3Service.getFileUrl(fileName).toString())
                .build();

        image1.setBeer(beer);
        imageRepository.save(image1);
        return beerInfoMapper.toDto(beerRepository.findById(beerId).get());
    }
    @Override
    public void deleteImage(String name) {
        Optional<Image> imageOp = imageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        s3Service.deleteFile(extractName(name));
        imageRepository.delete(imageOp.get());
    }
}
