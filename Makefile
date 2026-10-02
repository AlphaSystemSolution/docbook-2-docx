GRADLE = ./gradlew
GET_VERSION = $(GRADLE) -q printVersion | tr -d '\033' | sed -n 's/.*Projected version is: //p' | sed 's/\[[0-9;]*m//g'

build:
	$(GRADLE) build

clean:
	$(GRADLE) clean

test:
	$(GRADLE) test

spotless:
	$(GRADLE) spotlessApply

all: clean build test

projectedVersion:
	@VERSION=$$($(GET_VERSION)); \
	if [ -z "$$VERSION" ]; then echo "Failed to determine release version"; exit 1; fi; \
	echo $$VERSION

publishLocal:
	@VERSION=$$($(GET_VERSION)); \
	if [ -z "$$VERSION" ]; then echo "Failed to determine release version"; exit 1; fi; \
	$(GRADLE) -Pversion=$$VERSION publishToMavenLocal
	
cli:
	$(GRADLE) :asciidoc-docx-cli:shadowJar

printVersion:
	$(GRADLE) printVersion

release:
	@VERSION=$$($(GET_VERSION)); \
	if [ -z "$$VERSION" ]; then echo "Failed to determine release version"; exit 1; fi; \
	echo "Releasing $$VERSION"; \
	$(GRADLE) -Pversion=$$VERSION setReleaseVersion publishToMavenCentral createTag pushTag
	