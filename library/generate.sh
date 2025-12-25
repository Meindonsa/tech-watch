#!/bin/sh

app_name=techwatch-api
options_file="options.json"

[ -f "$options_file" ] || { echo "$options_file manquant !"; exit 1; }

mkdir -p release
rm -rf ${app_name} api.json

echo "1. Récupération OpenAPI"
curl -f http://localhost:23000/services-layer/v3/api-docs -o api.json || exit 1

echo "2. Génération avec swagger-codegen"
swagger-codegen generate \
  -i api.json \
  -l typescript-axios \
  -o ${app_name} \
  -c ${options_file} \
  --additional-properties supportsES6=true,withNodeImports=true

cd ${app_name}

# Récupération propre du nom et de la version (avec fallback si jq foire)
package_name=$(grep '"npmName"' ../options.json | cut -d'"' -f4)
version=$(grep '"npmVersion"' ../options.json | cut -d'"' -f4)
 echo $version
# Nettoyage du nom pour le .tgz : on remplace @scope/ par scope-
tgz_name=$(echo "$package_name" | tr '/' '-')   # @meindonsa/chat-api → @meindonsa-chat-api
tgz_name=${tgz_name#@}                          # @meindonsa-chat-api → meindonsa-chat-api
tgz_file="${tgz_name}-${version}.tgz"

echo "3. package.json minimal"
cat > package.json << EOF
{
  "name": "$package_name",
  "version": "$version",
  "main": "index.js",
  "types": "index.d.ts",
  "files": ["**/*.js", "**/*.d.ts"],
  "license": "MIT"
}
EOF

cat > model.ts << 'EOF'
export * from './models';
EOF

# index.js et index.d.ts simples
cat > index.js << 'EOF'
export * from './api';
export * from './model';
export * from './configuration';
EOF

cat > index.d.ts << 'EOF'
export * from './api';
export * from './model';
export * from './configuration';
EOF

echo "4. Création du .tgz avec le bon nom"
cd ..
tar -czf "release/${tgz_file}" ${app_name}

echo "=================================="
echo "C’EST FINI !"
echo "Fichier : release/${tgz_file}"
echo "Nom complet : $package_name"
echo "Version : $version"
echo "=================================="