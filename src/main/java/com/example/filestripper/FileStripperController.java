package com.example.filestripper;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.Locale;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileStripperController {
    @GetMapping ("/extract")
    public ResponseEntity<String> extractContent() {
        return ResponseEntity.ok("Post to this URL with file param 'file'");
    }

    @PostMapping("/extract")
    public ResponseEntity<String> extractContent(@RequestParam("file") MultipartFile file) {
        StringBuilder strText = new StringBuilder();
        try {
            String originalFileName = file.getOriginalFilename();
            int idx = originalFileName.lastIndexOf(".");
            String docType = originalFileName.substring(idx + 1);
            InputStream is = file.getInputStream();
            switch (docType.toLowerCase(Locale.ROOT)) {
                case "docx":
                    try(XWPFDocument docx = new XWPFDocument(is)) {
                        XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                        String[] docLines = extractor.getText().split("\n");
                        for (int i = 0; i < docLines.length; i++) {
                            strText.append(" ").append(docLines[i]);
                        }
                    }
                    break;
                case "doc":
                    HWPFDocument document = new HWPFDocument(is);
                    WordExtractor wordExtractor = new WordExtractor(document);
                    strText.append(wordExtractor.getText());
                    break;
                case "pdf":
                    try (PDDocument doc = Loader.loadPDF(file.getBytes(), "")) {
                        PDFTextStripper pdfStripper = new PDFTextStripper();
                        strText.append(new PDFTextStripper().getText(doc));
                    }
                    break;
                default:
                    throw new Exception("File type not supported. Only supported types are PDF,DOC and DOCX.");
            }
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), e.getMessage());
        }
        return ResponseEntity.ok(strText.toString());
    }
}
