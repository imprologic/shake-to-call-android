#!/bin/bash
set -e

# 1. Clean previous builds (optional but recommended)
echo "🧹 Cleaning previous build..."
./gradlew clean

# 2. Build the release app bundle using the key.properties file
echo "📦 Building release app bundle (.aab)..."
./gradlew bundleGoogleplayRelease

# 3. Display output path
echo "✅ Build complete!"
echo "👉 You can find your AAB at:"
echo "   $(pwd)/app/build/outputs/bundle/googleplayRelease/app-release.aab"
