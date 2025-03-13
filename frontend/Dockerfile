# Verwende ein Node.js-Image als Basis
FROM node:22 AS build

# Setze das Arbeitsverzeichnis im Container
WORKDIR /app

# Kopiere package.json und package-lock.json
COPY package*.json ./

# Installiere die Abhängigkeiten
RUN npm install

# Kopiere den gesamten Quellcode
COPY . .

# Baue die React-App
RUN npm run build

# Verwende ein Nginx-Image, um die React-App bereitzustellen
FROM nginx:alpine

# Kopiere die erstellte Build-Datei ins Nginx-Verzeichnis
COPY --from=build /app/build /usr/share/nginx/html

# Exponiere den Port, auf dem die App läuft
EXPOSE 80

# Starte Nginx
CMD ["nginx", "-g", "daemon off;"]