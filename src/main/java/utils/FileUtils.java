package utils;

import java.io.*;
import java.nio.file.Files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

public class FileUtils {

    public static String readFile(File file) throws IOException {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".txt")) {
            return Files.readString(file.toPath());
        }
        else if (name.endsWith(".pdf")) {
            return readPdf(file);
        }
        else if (name.endsWith(".doc")) {
            return readDoc(file);
        }
        else if (name.endsWith(".docx")) {
            return readDocx(file);
        }
        else {
            throw new IOException("Unsupported file type");
        }
    }

    // ---------- PDF ----------
    private static String readPdf(File file) throws IOException {
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    // ---------- DOC ----------
    private static String readDoc(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);
            return extractor.getText();
        }
    }

    // ---------- DOCX ----------
    private static String readDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument doc = new XWPFDocument(fis)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText();
        }
    }

    public static void writeFile(String name, String content)
            throws IOException {
        try (FileWriter writer = new FileWriter(name)) {
            writer.write(content);
        }
    }
}
