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
	$(GRADLE) setReleaseVersion publishToMavenLocal
	
cli:
	$(GRADLE) :asciidoc-docx-cli:shadowJar
