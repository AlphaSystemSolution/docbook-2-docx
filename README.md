# AsciiDoc to Word Converter

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE.md)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-9.4.1-green.svg)](https://gradle.org/)

A powerful library and command-line tool that converts AsciiDoc documents into Microsoft Word (DOCX) format. This tool uses DocBook as an intermediate format to ensure robust and accurate conversion while preserving document structure, formatting, and styling.

## Features

- ✅ **Full AsciiDoc Support** - Comprehensive support for AsciiDoc elements
- 📄 **DocBook Pipeline** - Reliable conversion via DocBook XML intermediate format
- 🎨 **Rich Formatting** - Preserves text styles, lists, tables, admonitions, and more
- 🌐 **Arabic Text Support** - Built-in support for Arabic text rendering
- ⚡ **CLI Tool** - Easy-to-use command-line interface
- 🔧 **Programmatic API** - Use as a library in your Java applications
- 📦 **Shadow JAR** - Single executable JAR with all dependencies

## Quick Start

### Prerequisites

- Java 17 or higher
- Gradle 9.4.1 (wrapper included)

### Building

Clone and build the project:

```bash
git clone https://github.com/AlphaSystemSolution/docbook-2-docx.git
cd docbook-2-docx
./gradlew build
```

### Creating the CLI Tool

Build the shadow JAR (fat JAR with all dependencies):

```bash
./gradlew :asciidoc-docx-cli:shadowJar
```

The executable JAR will be created at:
```
asciidoc-docx-cli/build/libs/asciidoc-docx-cli-0.5.5-SNAPSHOT-all.jar
```

### Basic Usage

Convert an AsciiDoc file to Word:

```bash
java -jar asciidoc-docx-cli/build/libs/asciidoc-docx-cli-0.5.5-SNAPSHOT-all.jar -s input.adoc
```

This creates `input.docx` in the same directory as the source file.

## Documentation

📚 **Comprehensive documentation is available in the [docs](docs/) directory:**

- **[Main Documentation](docs/index.adoc)** - Project overview and quick start
- **[CLI User Guide](docs/cli-guide.adoc)** - Complete command-line reference
- **[Developer Guide](docs/developer-guide.adoc)** - Architecture and contribution guidelines
- **[API Reference](docs/api-reference.adoc)** - Programmatic usage
- **[Supported Elements](docs/elements-reference.adoc)** - Complete element reference
- **[Examples](docs/examples/)** - Sample AsciiDoc documents

## Command-Line Options

```bash
java -jar asciidoc-docx-cli-{version}-all.jar [OPTIONS]

Required:
  -s <srcPath>              Source AsciiDoc file path

Optional:
  -d <destPath>             Destination DOCX file path
  -o                        Open document after conversion
  -e <extractPath>          Extract DOCX package to directory
  -x <docbookPath>          Save intermediate DocBook XML
```

### Examples

```bash
# Basic conversion
java -jar asciidoc-docx-cli-0.5.5-SNAPSHOT-all.jar -s document.adoc

# Custom output path and auto-open
java -jar asciidoc-docx-cli-0.5.5-SNAPSHOT-all.jar \
  -s input.adoc \
  -d output.docx \
  -o

# Debug mode with DocBook extraction
java -jar asciidoc-docx-cli-0.5.5-SNAPSHOT-all.jar \
  -s document.adoc \
  -x ./debug \
  -e ./extracted
```

## Project Structure

The project is organized into six modules:

```
docbook-2-docx-parent/
├── docbook-2-docx-common/     # Shared models and utilities
├── docbook-model/             # JAXB-generated DocBook models
├── asciidoctor-adapter/       # AsciiDoc to DocBook conversion
├── docbook-2-docx/            # Core conversion engine
├── arabic-handler/            # Arabic text support
└── asciidoc-docx-cli/         # Command-line interface
```

## How It Works

The conversion follows a three-stage pipeline:

```
AsciiDoc → DocBook XML → DOCX (Word)
```

1. **AsciiDoc to DocBook**: AsciidoctorJ converts AsciiDoc to DocBook XML
2. **DocBook to DOCX**: Custom conversion engine transforms DocBook elements to Word structures
3. **Output**: Fully formatted DOCX file ready for Microsoft Word

## Programmatic Usage

### Maven

```xml
<dependency>
    <groupId>io.github.sfali23</groupId>
    <artifactId>asciidoctor-adapter</artifactId>
    <version>0.5.5-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>io.github.sfali23</groupId>
    <artifactId>docbook-2-docx</artifactId>
    <version>0.5.5-SNAPSHOT</version>
</dependency>
```

### Gradle

```groovy
dependencies {
    implementation 'io.github.sfali23:asciidoctor-adapter:0.5.5-SNAPSHOT'
    implementation 'io.github.sfali23:docbook-2-docx:0.5.5-SNAPSHOT'
}
```

### Example Code

```java
import com.alphasystem.asciidoc.util.DocumentConverter;
import com.alphasystem.docbook.DocumentBuilder;
import java.nio.file.Paths;

public class Example {
    public static void main(String[] args) throws Exception {
        // Convert AsciiDoc to DocBook
        var docInfo = DocumentConverter.convertToDocBook(
            Paths.get("input.adoc")
        );
        
        // Convert DocBook to Word
        var outputPath = DocumentBuilder.buildDocument(
            docInfo.getDocumentInfo(),
            Paths.get("output.docx")
        );
        
        System.out.println("Created: " + outputPath);
    }
}
```

## Supported Elements

The converter supports a comprehensive set of AsciiDoc elements:

- **Block Elements**: Paragraphs, lists (ordered, unordered, definition), tables, code blocks, admonitions (note, tip, warning, caution, important), examples, sidebars
- **Inline Elements**: Bold, italic, monospace, superscript, subscript, links, cross-references
- **Special Features**: Arabic text support, custom styling, images, includes

See the [Supported Elements Reference](docs/elements-reference.adoc) for complete details.

## Development

### Building from Source

```bash
./gradlew clean build
```

### Running Tests

```bash
./gradlew test
```

### Code Coverage

```bash
./gradlew test jacocoTestReport
```

Reports are generated in `build/reports/jacoco/test/html/`.

## Contributing

Contributions are welcome! Please see the [Developer Guide](docs/developer-guide.adoc) for:

- Architecture overview
- Development workflow
- Code style guidelines
- Testing requirements
- Pull request process

## License

This project is licensed under the Apache License 2.0. See [LICENSE.md](LICENSE.md) for details.

## Version

Current version: **0.5.5-SNAPSHOT**

## Links

- **Documentation**: [docs/](docs/)
- **Examples**: [docs/examples/](docs/examples/)
- **Issues**: GitHub Issues
- **AsciiDoc**: https://asciidoc.org/
- **Asciidoctor**: https://asciidoctor.org/
- **DocBook**: https://docbook.org/

## Support

For questions, issues, or contributions, please visit the project repository.

---

Made with ❤️ by the AlphaSystem team
