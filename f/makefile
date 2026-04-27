# Variables
GRADLE = ./gradlew

.PHONY: all build run clean test

# Default task
all: build

build:
	$(GRADLE) build --no-configuration-cache

run:
	$(GRADLE) run --no-configuration-cache

test:
	$(GRADLE) test --no-configuration-cache

clean:
	$(GRADLE) clean