#!/usr/bin/env bash
set -euo pipefail

REPO_URL="https://github.com/softwaresdevelopers148-ui/Sketchware_Structures-By-Myat-Gyi.git"
TARGET_DIR="${1:-Sketchware_Structures-By-Myat-Gyi}"

if [ -d "$TARGET_DIR/.git" ]; then
  git -C "$TARGET_DIR" pull --ff-only
else
  git clone --depth=1 "$REPO_URL" "$TARGET_DIR"
fi

echo "Reference commit: $(git -C "$TARGET_DIR" rev-parse HEAD)"
