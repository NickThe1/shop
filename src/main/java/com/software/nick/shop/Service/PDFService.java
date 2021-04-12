package com.software.nick.shop.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.software.nick.shop.model.Product;
import com.software.nick.shop.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PDFService {

    @Autowired
    private ProductRepo productRepo;

    public void generateReport() throws FileNotFoundException, DocumentException {
        List<Product> products = productRepo.findAll();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(UUID.randomUUID().toString() + "report.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        for (Product product: products){
            Paragraph chunkTitle = new Paragraph("Report from " + ZonedDateTime.now() + "\n", font);
            Paragraph chunk = new Paragraph("Title: " + product.getTitle() + "\n", font);
            Paragraph chunk1 = new Paragraph("Description " + product.getDescription() + "\n", font);
            Paragraph chunk2 = new Paragraph("Author " + product.getAuthor().getUsername() + "\n", font);
            Paragraph chunk3 = new Paragraph("Price " + product.getPrice().toString() + "\n", font);
            document.add(chunkTitle);
            document.add(chunk);
            document.add(Chunk.NEWLINE);
            document.add(chunk1);
            document.add(Chunk.NEWLINE);
            document.add(chunk2);
            document.add(Chunk.NEWLINE);
            document.add(chunk3);
            document.add(Chunk.NEWLINE);
        }
        document.close();
    }
}
