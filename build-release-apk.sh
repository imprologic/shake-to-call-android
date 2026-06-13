#!/bin/bash
set -e

# 1. Clean previous builds (optional but recommended)
echo "🧹 Cleaning previous build..."
./gradlew clean

# 2. Build the release APK using the key.properties file
echo "📦 Building release APK..."
./gradlew assembleGithubRelease

# 3. Display output path
echo "✅ Build complete!"
echo "👉 You can find your APK at:"
echo "   $(pwd)/app/build/outputs/apk/github/release/app-release.apk"

