#!/bin/bash

# Chemins relatifs
OPTIONS_FILE="options.json"
PACKAGE_DIR="techwatch-api"

echo "🔍 Lecture de la configuration depuis $OPTIONS_FILE..."

# 1. Extraire l'URL du repo GitHub depuis options.json
REPO_URL=$(jq -r '.github' "$OPTIONS_FILE")

if [ "$REPO_URL" == "null" ] || [ -z "$REPO_URL" ]; then
    echo "❌ Erreur: Impossible de trouver l'URL github dans $OPTIONS_FILE"
    exit 1
fi

# 2. Aller dans le dossier du package généré
cd "$PACKAGE_DIR" || exit

# 3. Ajouter uniquement publishConfig et repository au package.json
echo "📝 Mise à jour de la configuration de publication..."
TMP_JSON=$(mktemp)
jq --arg repo_url "$REPO_URL" \
  '.publishConfig = {"registry": "https://npm.pkg.github.com"} |
   .repository = {"type": "git", "url": $repo_url}' \
   package.json > "$TMP_JSON" && mv "$TMP_JSON" package.json

# 4. Publication
echo "⬆️ Publication sur GitHub Packages..."
npm publish

echo "🎉 Terminé ! Les infos de repository et de publication ont été ajoutées."