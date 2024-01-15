package com.alphasystem.docx.cli;

import com.alphasystem.asciidoc.util.DocumentConverter;
import com.alphasystem.docbook.DocumentBuilder;
import com.alphasystem.docbook.util.FileUtil;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.ZipUtil;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        final var options = new Options();

        final var srcOption = Option
                .builder("s")
                .argName("srcPath")
                .hasArg()
                .required()
                .desc("Specify source Asciidoc file path")
                .build();
        options.addOption(srcOption);

        final var destOption = Option
                .builder("d")
                .argName("destPath")
                .hasArg()
                .required(false)
                .desc("Specify destination Docx file path")
                .build();
        options.addOption(destOption);

        final var openDocument = Option
                .builder("o")
                .type(Boolean.class)
                .argName("openDocument")
                .required(false)
                .desc("Open document")
                .build();
        options.addOption(openDocument);

        final var extractPackage = Option
                .builder("e")
                .argName("extractedDocumentPath")
                .hasArg()
                .required(false)
                .desc("Extracted document path in the given directory")
                .build();
        options.addOption(extractPackage);

        final var saveDocBookContent = Option
                .builder("x")
                .argName("saveDocBookContent")
                .hasArg()
                .required(false)
                .desc("Save DocBook content in the given directory")
                .build();
        options.addOption(saveDocBookContent);


        // define parser
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);

            Path srcPath = null;
            if (cmd.hasOption(srcOption)) {
                srcPath = toPath(cmd.getOptionValue(srcOption));
                if (srcPath == null || !Files.exists(srcPath)) {
                    printHelp("Source path does not exists", helper, options);
                }
            }

            Path docxPath = null;
            if (cmd.hasOption(destOption)) {
                docxPath = toPath(cmd.getOptionValue(destOption));
            }

            var documentInfo = DocumentConverter.convertToDocBook(srcPath).getDocumentInfo();

            if(cmd.hasOption(saveDocBookContent)) {
                final var value = cmd.getOptionValue(saveDocBookContent);
                try {
                    saveDocBookContent(value, docxPath, documentInfo.getContent());
                } catch (IOException ex) {
                    System.out.printf("Unable save DocBook content.%n");
                }
            }

            Path destPath;
            if (docxPath == null) {
                destPath = DocumentBuilder.buildDocument(documentInfo);
            } else {
                destPath = DocumentBuilder.buildDocument(documentInfo, docxPath);
            }
            if (cmd.hasOption(extractPackage)) {
                extractPackage(docxPath, toPath(cmd.getOptionValue(extractPackage)));
            }

            if (cmd.hasOption(openDocument)) {
                Desktop.getDesktop().open(destPath.toFile());
            }
        } catch (Exception ex) {
            System.err.println(AppUtil.getStackTrace(ex));
            printHelp(ex.getMessage(), helper, options);
        }
    }

    private static Path toPath(String s) {
        if (s != null && !s.isBlank()) {
            return Paths.get(s);
        }
        return null;
    }

    private static void printHelp(String s, HelpFormatter helper, Options options) {
        System.err.println(s);
        helper.printHelp("Usage:", options);
        System.exit(0);
    }

    private static void extractPackage(Path docxPath, Path dir) throws Exception {
        if (Files.exists(dir)) {
            System.out.printf("Path \"%s\" exists, deleting it before processing it%n", dir);
            FileUtils.cleanDirectory(dir.toFile());
        }
        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }
        ZipUtil.extractZipFile(dir.toFile(), docxPath.toString());
    }

    private static void saveDocBookContent(String value, Path docxPath, String content) throws IOException {
        if (StringUtils.isNotBlank(value)) {
            final var parentDir = toPath(value);
            if (Files.isDirectory(parentDir)) {
                final var fileName = FileUtil.getDocBookFile(docxPath).getFileName().toString();
                final var dockBookFile = Paths.get(value, fileName);
                Files.writeString(dockBookFile, content);
            } else {
                System.err.printf("\"%s\" is not a directory%n", parentDir);
            }

        }
    }
}
