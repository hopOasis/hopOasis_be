package com.example.hop_oasis.decoder;

import com.example.hop_oasis.hendler.exception.ImageNotFoundException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.IMAGE_COMPRESS_EXCEPTION;
import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.IMAGE_DECOMPRESS_EXCEPTION;

@Component
public class ImageCompressor {

    public byte[] compressImage(byte[] data) {
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
            throw new ImageNotFoundException(IMAGE_COMPRESS_EXCEPTION, "");
        }
        return outputStream.toByteArray();
    }

    public  byte[] decompressImage(byte[] data, String name) {
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
            throw new ImageNotFoundException(IMAGE_DECOMPRESS_EXCEPTION, name);
        }
        return outputStream.toByteArray();
    }

}
