GRADLE = ./gradlew

build:
	$(GRADLE) build

clean:
	$(GRADLE) clean

test:
	$(GRADLE) test

all: clean build test

cli:
	$(GRADLE) :asciidoc-docx-cli:shadowJar
