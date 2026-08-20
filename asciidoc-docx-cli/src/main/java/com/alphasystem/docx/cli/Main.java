package com.alphasystem.docx.cli;

import com.alphasystem.asciidoc.model.Backend;
import com.alphasystem.asciidoc.util.DocumentConverter;
import com.alphasystem.commons.SystemException;
import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.commons.util.ZipUtil;
import com.alphasystem.docbook.DocumentBuilder;
import com.alphasystem.docbook.util.FileUtil;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class Main {

    public static void main(String[] args) {
        final var options = new Options();

        final var srcOption =
                Option.builder("s")
                        .argName("srcPath")
                        .hasArg()
                        .required()
                        .desc("Specify source Asciidoc file path")
                        .build();
        options.addOption(srcOption);

        final var destOption =
                Option.builder("d")
                        .argName("destPath")
                        .hasArg()
                        .required(false)
                        .desc("Specify destination Docx file path")
                        .build();
        options.addOption(destOption);

        final var backendOpt =
                Option.builder("b")
                        .type(String.class)
                        .argName("backend")
                        .hasArg()
                        .required(false)
                        .desc("Specify backend; allowed values: word, html, xhtml")
                        .build();
        options.addOption(backendOpt);

        final var openDocumentOpt =
                Option.builder("o")
                        .type(Boolean.class)
                        .argName("openDocument")
                        .required(false)
                        .desc("Open document")
                        .build();
        options.addOption(openDocumentOpt);

        final var extractPackageOpt =
                Option.builder("e")
                        .argName("extractedDocumentPath")
                        .hasArg()
                        .required(false)
                        .desc("Extracted document path in the given directory")
                        .build();
        options.addOption(extractPackageOpt);

        final var saveDocBookContentOpt =
                Option.builder("x")
                        .argName("saveDocBookContent")
                        .hasArg()
                        .required(false)
                        .desc("Save DocBook content in the given directory")
                        .build();
        options.addOption(saveDocBookContentOpt);

        // define parser
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        Backend backend = Backend.WORD;

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption(backendOpt)) {
                final var value = cmd.getOptionValue(backendOpt);
                try {
                    backend = Backend.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    System.err.printf("Invalid backend: %s%n", value);
                }
            }

            Path srcPath = null;
            if (cmd.hasOption(srcOption)) {
                srcPath = toPath(cmd.getOptionValue(srcOption));
                if (srcPath == null || !Files.exists(srcPath)) {
                    printHelp("Source path does not exists", helper, options);
                }
            }

            Path destPath = null;
            if (cmd.hasOption(destOption)) {
                destPath = toPath(cmd.getOptionValue(destOption));
            }

            String docBookContentPath = null;
            if (cmd.hasOption(saveDocBookContentOpt)) {
                docBookContentPath = cmd.getOptionValue(saveDocBookContentOpt);
            }

            Path extractPackage = null;
            if (cmd.hasOption(extractPackageOpt)) {
                extractPackage = toPath(cmd.getOptionValue(extractPackageOpt));
            }
            final var openDocument = cmd.hasOption(openDocumentOpt);

            if (backend == Backend.WORD) {
                convertToWord(srcPath, destPath, docBookContentPath, extractPackage, openDocument);
            } else {
                convertFile(backend, srcPath, destPath);
            }

        } catch (Exception ex) {
            System.err.println(AppUtil.getStackTrace(ex));
            printHelp(ex.getMessage(), helper, options);
        }
    }

    private static void convertToWord(
            final Path srcPath,
            final Path docxPath,
            final String docBookContentPath,
            final Path extractPackagePath,
            final boolean openDocument)
            throws Exception {
        var documentInfo = DocumentConverter.convertToDocBook(srcPath).getDocumentInfo();

        if (StringUtils.isNotBlank(docBookContentPath)) {
            try {
                saveDocBookContent(docBookContentPath, docxPath, documentInfo.getContent());
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
        if (extractPackagePath != null && Files.exists(extractPackagePath)) {
            extractPackage(docxPath, extractPackagePath);
        }

        if (openDocument) {
            Desktop.getDesktop().open(destPath.toFile());
        }
    }

    private static void convertFile(final Backend backend, final Path srcPath, final Path destPath)
            throws SystemException, IOException {
        var asciiDocumentInfo =
                switch (backend) {
                    case HTML -> DocumentConverter.convertToHtml(srcPath);
                    case XHTML -> DocumentConverter.convertToXhtml(srcPath);
                    default -> throw new SystemException("Unsupported backend: " + backend);
                };
        FileUtils.write(destPath.toFile(), asciiDocumentInfo.getDocumentInfo().getContent());
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

    private static void saveDocBookContent(String value, Path docxPath, String content)
            throws IOException {
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
