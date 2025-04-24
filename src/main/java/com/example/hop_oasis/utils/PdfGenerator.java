package com.example.hop_oasis.utils;

import com.lowagie.text.pdf.BaseFont;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

@Component
public class PdfGenerator {
    public byte[] generateFromHtml(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            String fontPath = Objects.requireNonNull(getClass().getClassLoader().getResource("fonts/DejaVuSans.ttf")).getPath();
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }
}
