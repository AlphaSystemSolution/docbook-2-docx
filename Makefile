GRADLE = ./gradlew

build:
	$(GRADLE) build

clean:
	$(GRADLE) clean

test:
	$(GRADLE) test

spotless:
	$(GRADLE) spotlessApply

publishLocal:
	$(GRADLE) publishToMavenLocal

all: clean build test

cli:
	$(GRADLE) :asciidoc-docx-cli:shadowJar
