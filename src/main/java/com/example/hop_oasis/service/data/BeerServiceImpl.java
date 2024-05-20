package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.repository.BeerRepository;

import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.repository.ImageRepository;
import com.example.hop_oasis.service.BeerService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


import java.net.MalformedURLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final ImageRepository imageRepository;
    private final BeerMapper beerMapper;
    private final BeerInfoMapper beerInfoMapper;
    private final ImageMapper imageMapper;


    @Override
    public void save(MultipartFile file, BeerDto beerDto) throws IOException {
        byte[] bytesIm = compressImage(file.getBytes());
        Image image = Image.builder()
                .image(bytesIm)
                .build();
        List<ImageDto> images = new ArrayList<>();
        images.add(imageMapper.fromEntity(image));
        beerDto.setImage(images);
        Beer beer = beerMapper.toEntity(beerDto);
        beerRepository.save(beer);
        image.setBeer(beer);
        imageRepository.save(image);


    }

    private byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[2048];
        try {

            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            outputStream.close();
        } catch (Exception e) {
            System.out.println("Error beer problem");
        }
        return outputStream.toByteArray();
    }

    @Override
    public void addImageToBeer(Long beerId, byte[] image) {
        Image image1 = Image.builder()
                .image(compressImage(image))
                .build();

        image1.setBeer(beerRepository.findById(beerId).orElse(null));
        imageRepository.save(image1);
    }

    private static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[2048];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception e) {
            System.out.println("Error beer problem");
        }
        return outputStream.toByteArray();
    }


    @Override
    public BeerInfoDto getBeerById(Long id) {
        Beer beer = beerRepository.findById(id).orElse(null);
        if (beer == null) {
            throw new RuntimeException("Beer not found");
        }

        return beerInfoMapper.fromEntity(beer);
    }

    @Override
    public ImageDto getImage(Long id) throws MalformedURLException {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            throw new RuntimeException("Image not found");
        }
        ImageDto imageDto = imageMapper.fromEntity(image);
        imageDto.setImage(decompressImage(imageDto.getImage()));
        return imageDto;
    }

    @Override
    public List<BeerInfoDto> getAllBeers() {
        return beerInfoMapper.fromEntity(beerRepository.findAll());

    }

    @Override
    public void update(BeerInfoDto beerInfo, Long id) {
        Beer beer = beerRepository.findById(id).orElse(null);
        if (beer == null) {
            throw new RuntimeException("Beer not found");

        }
        if (!beerInfo.getBeerName().isEmpty()) {
            beer.setBeerName(beerInfo.getBeerName());
        }
        if (beerInfo.getVolumeLarge() != 0.0) {
            beer.setVolumeLarge(beerInfo.getVolumeLarge());
        }
        if (beerInfo.getVolumeLarge() != 0.0) {
            beer.setVolumeSmall(beerInfo.getVolumeSmall());
        }
        if (beerInfo.getVolumeSmall() != 0.0) {
            beer.setPriceLarge(beerInfo.getPriceLarge());
        }
        if (beerInfo.getPriceSmall() != 0.0) {
            beer.setPriceSmall(beerInfo.getPriceSmall());
        }
        if (Objects.nonNull(beerInfo.getDescription())) {
            beer.setDescription(beerInfo.getDescription());
        }
        if (Objects.nonNull(beerInfo.getBearColor())) {
            beer.setBearColor(beerInfo.getBearColor());
        }


        beerRepository.save(beer);
    }

    @Override
    public void delete(Long id) {
        beerRepository.deleteById(id);
    }


}
