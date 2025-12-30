#!/bin/bash

OPTIONS_FILE="options.json"
PACKAGE_DIR="techwatch-api"

# 1. Extraire l'URL du repo GitHub
REPO_URL=$(jq -r '.github' "$OPTIONS_FILE")

# 2. Aller dans le dossier
cd "$PACKAGE_DIR" || exit

# 3. Préparer le package.json original
echo "📝 Mise à jour du package.json..."
TMP_JSON=$(mktemp)
jq --arg repo_url "$REPO_URL" \
  '.publishConfig = {"registry": "https://npm.pkg.github.com"} |
   .repository = {"type": "git", "url": $repo_url}' \
   package.json > "$TMP_JSON" && mv "$TMP_JSON" package.json

# 4. Installation forcée des dépendances nécessaires
echo "📦 Installation d'Axios et TypeScript..."
npm install axios
npm install typescript --save-dev

# 5. Compilation forcée vers 'dist'
echo "⚙️ Compilation du TypeScript vers ./dist..."
# On ajoute l'inclusion des fichiers TS à la racine et dans apis/models
npx tsc api.ts --outDir dist --declaration true --module commonjs --target es6 --moduleResolution node --skipLibCheck true --lib es6,dom

# 6. Vérification et préparation du dossier dist
if [ ! -d "dist" ]; then
    echo "❌ Erreur: Le dossier 'dist' n'a pas été généré par la compilation."
    exit 1
fi

echo "📂 Préparation du dossier de sortie..."
cp package.json dist/
[ -f "README.md" ] && cp README.md dist/

# 7. Publication depuis le dossier dist
echo "⬆️ Publication sur GitHub Packages..."
cd dist || exit
npm publish

echo "🎉 Package publié avec succès depuis le dossier dist !"