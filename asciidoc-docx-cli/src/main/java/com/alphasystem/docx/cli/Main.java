package com.alphasystem.docx.cli;

import com.alphasystem.asciidoc.util.DocumentConverter;
import com.alphasystem.docbook.DocumentBuilder;
import org.apache.commons.cli.*;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        var options = new Options();

        var srcOption = Option
                .builder("s")
                .argName("srcPath")
                .hasArg()
                .required()
                .desc("Specify source Asciidoc file path")
                .build();
        options.addOption(srcOption);

        var destOption = Option
                .builder("d")
                .argName("destPath")
                .hasArg()
                .required(false)
                .desc("Specify destination Docx file path")
                .build();
        options.addOption(destOption);

        // define parser
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);

            Path srcPath = null;
            if(cmd.hasOption(srcOption)) {
                srcPath = toPath(cmd.getOptionValue(srcOption));
                if (srcPath == null || !Files.exists(srcPath)) {
                    printHelp("Source path does not exists", helper,options);
                }
            }

            Path docxPath = null;
            if(cmd.hasOption(destOption)) {
                docxPath = toPath(cmd.getOptionValue(destOption));
            }

            var documentInfo = DocumentConverter.convertToDocBook(srcPath).getDocumentInfo();
            /*final var docBookFile = FileUtil.getDocBookFile(docxPath);
            Files.writeString(docBookFile, documentInfo.getContent());*/
            System.out.println(documentInfo.getContent());
            Path destPath;
            if (docxPath == null) {
                destPath = DocumentBuilder.buildDocument(documentInfo);
            } else {
                destPath = DocumentBuilder.buildDocument(documentInfo, docxPath);
            }
            Desktop.getDesktop().open(destPath.toFile());
        } catch (Exception ex) {
            printHelp(ex.getMessage(), helper, options);
        }
    }

    private static Path toPath(String s) {
        if(s != null && !s.isBlank()) {
             return Paths.get(s);
        }
        return null;
    }

    private static void printHelp(String s, HelpFormatter helper, Options options) {
        System.err.println(s);
        helper.printHelp("Usage:", options);
        System.exit(0);
    }
}
