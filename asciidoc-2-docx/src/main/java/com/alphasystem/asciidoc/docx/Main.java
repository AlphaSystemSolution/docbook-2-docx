package com.alphasystem.asciidoc.docx;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.util.DocumentConverter;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws SystemException, IOException {
        final var basePath = "/Users/sfali/Documents/Arabic/AsciiDocs";
        final var srcPath = Paths.get(basePath + "/full.adoc");
        final var asciiDocumentInfo = DocumentConverter.convertToXhtml(srcPath);
        final var documentInfo = asciiDocumentInfo.getDocumentInfo();
        parseDocument(documentInfo.getContent());
        Files.writeString(Paths.get(basePath + "/full.html"), documentInfo.getContent());
    }

    private static void parseDocument(String content) {
        var document = Jsoup.parse(content);
        System.out.println(document.title());
        var body = document.getElementsByTag("body").get(0).child(0);
        System.out.printf("name: %s\n", body.html());
    }
}
