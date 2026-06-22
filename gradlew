#!/usr/bin/env sh
set -e
GRADLE_VERSION=9.2.0
DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
GRADLE_HOME="$DIR/.gradle-local/gradle-$GRADLE_VERSION"
GRADLE_BIN="$GRADLE_HOME/bin/gradle"
if [ ! -x "$GRADLE_BIN" ]; then
  echo "Gradle $GRADLE_VERSION not found. Downloading..."
  mkdir -p "$DIR/.gradle-local"
  if command -v curl >/dev/null 2>&1; then
    curl -L "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$DIR/.gradle-local/gradle-$GRADLE_VERSION-bin.zip"
  elif command -v wget >/dev/null 2>&1; then
    wget "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -O "$DIR/.gradle-local/gradle-$GRADLE_VERSION-bin.zip"
  else
    echo "curl or wget is required to download Gradle."
    exit 1
  fi
  unzip -q "$DIR/.gradle-local/gradle-$GRADLE_VERSION-bin.zip" -d "$DIR/.gradle-local"
fi
exec "$GRADLE_BIN" "$@"
