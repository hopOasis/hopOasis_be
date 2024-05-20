package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.model.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageMapper {
    public ImageDto fromEntity(Image image){

        return ImageDto.builder()
                .id(image.getId())
                .image(image.getImage())
                .build();
    }
    public List<ImageDto> fromEntity(Iterable<Image>images){

        List<ImageDto> dtos = new ArrayList<>();
        images.forEach(image -> dtos.add(fromEntity(image)));
        return dtos;
    }
    public Image toEntity(ImageDto imageDto){
        return Image.builder()
                .image(imageDto.getImage())
                .build();
    }
    public List<Image> toEntity(List<ImageDto> imageDtos){
        List<Image> images = new ArrayList<>();
        imageDtos.forEach(imageDto -> images.add(toEntity(imageDto)));
        return images;
    }
}
