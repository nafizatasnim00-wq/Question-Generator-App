package utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

public class FileUtils {

    
    // Read
   

    public static String readFile(File file) throws IOException {
        String name = file.getName().toLowerCase();

        if      (name.endsWith(".txt"))  return Files.readString(file.toPath());
        else if (name.endsWith(".pdf"))  return readPdf(file);
        else if (name.endsWith(".doc"))  return readDoc(file);
        else if (name.endsWith(".docx")) return readDocx(file);
        else throw new IOException("Unsupported file type");
    }

    // PDF
    private static String readPdf(File file) throws IOException {
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    // DOC
    private static String readDoc(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);
            return extractor.getText();
        }
    }

    // DOCX
    private static String readDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument doc = new XWPFDocument(fis)) {
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText();
        }
    }

   
    // Write
  

    public static void writeFile(String name, String content) throws IOException {
        try (FileWriter writer = new FileWriter(name)) {
            writer.write(content);
        }
    }

 
    // Upload Directory Management


    // Returns the upload directory, creating it if it doesn't exist
    public static File getUploadDir() {
        File dir = new File("uploaded_files");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    // Copies a file into the upload directory, avoiding name collisions
    public static File copyToUploadDir(File source) throws IOException {
        File uploadDir = getUploadDir();
        String filename = source.getName();
        File dest = new File(uploadDir, filename);

        // If same name exists, append index e.g. file(1).txt
        if (dest.exists()) {
            String base = filename;
            String ext  = "";
            int dot = filename.lastIndexOf('.');
            if (dot > 0) {
                base = filename.substring(0, dot);
                ext  = filename.substring(dot);
            }
            int index = 1;
            while (dest.exists()) {
                dest = new File(uploadDir, base + "(" + index++ + ")" + ext);
            }
        }

        Files.copy(source.toPath(), dest.toPath());
        return dest;
    }

    // Returns all files in the upload directory, sorted alphabetically
    public static List<File> listUploadFiles() {
        File uploadDir = getUploadDir();
        List<File> list = new ArrayList<>();

        File[] files = uploadDir.listFiles();
        if (files == null) return list;

        for (File f : files) {
            if (f.isFile()) list.add(f);
        }

        list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return list;
    }
    // Add this new method to FileUtils.java
    public static boolean deleteUploadedFile(File file) {
       if (file != null && file.exists()) {
          return file.delete();
      }
      return false;
    }
}