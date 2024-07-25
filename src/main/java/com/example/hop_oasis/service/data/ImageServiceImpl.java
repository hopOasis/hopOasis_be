package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.ImageUrlDto;
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

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final BeerRepository beerRepository;
    private final S3Service s3Service;
    @Override
    public ImageUrlDto getImageByName(String name) {
        return new ImageUrlDto(s3Service.getFileUrl(name).toString());
    }

    @Override
    public ImageUrlDto addImageToBeer(Long beerId, MultipartFile file) {
        try {
            String fileName ="beers/" + beerId.toString() + "/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image image1 = Image.builder()
                .name(file.getOriginalFilename())
                .build();
        Beer beer = beerRepository.findById(beerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, beerId));

        image1.setBeer(beer);
        image1 = imageRepository.save(image1);
        return new ImageUrlDto(s3Service.getFileUrl(image1.getName()).toString());
    }
    @Override
    public void deleteImage(String name) {
        Optional<Image> imageOp = imageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        s3Service.deleteFile(name);
        imageRepository.delete(imageOp.get());
    }
}
