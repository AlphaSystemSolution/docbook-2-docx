GRADLE = ./gradlew

build:
	$(GRADLE) build

clean:
	$(GRADLE) clean

test:
	$(GRADLE) test

spotless:
	$(GRADLE) spotlessApply

all: clean build test

publishLocal:
	$(GRADLE) publishToMavenLocal

cli:
	$(GRADLE) :asciidoc-docx-cli:shadowJar

printVersion:
	$(GRADLE) printVersion

release:
	$(GRADLE) publishToMavenCentral createTag pushTag
