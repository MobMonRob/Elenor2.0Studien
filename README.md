# BalanceBook

BalanceBook ist eine Anwendung zur Verwaltung einer gemeinsamen Kasse innerhalb einer Gruppe.  
Die Anwendung besteht aus einem Backend und einem Frontend. Für die Konfiguration und Einrichtung über Docker werden zusätzlich eine MariaDB-Instanz sowie eine Keycloak-Instanz verwendet.

## Inbetriebnahme

### 1. Repository klonen und notwendige Software überprüfen

Damit die Anwendung ausgeführt werden kann, muss der Quellcode lokal auf dem Rechner verfügbar sein. Dafür muss dieses Repository in ein beliebiges lokales Zielverzeichnis geklont werden.

Bevor die Anwendung gestartet werden kann, muss sichergestellt sein, dass Docker installiert ist.

### 2. Keycloak-Container starten

Um die Keycloak-Instanz zu starten, muss der Docker-Container mit folgendem Befehl gestartet werden:

```
docker run --name keycloak -p 8081:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=adminUser -e KC_BOOTSTRAP_ADMIN_PASSWORD=adminUser --hostname auth.local quay.io/keycloak/keycloak:latest start-dev
```

Die Umgebungsvariablen `KC_BOOTSTRAP_ADMIN_USERNAME` und `KC_BOOTSTRAP_ADMIN_PASSWORD` sollten angepasst werden. Diese Variablen dienen der Authentifizierung des Administrators in der Keycloak-Instanz.

### 3. Keycloak-Instanz konfigurieren

Zur Konfiguration der Keycloak-Instanz muss die URL `http://localhost:8081` im Webbrowser geöffnet werden. Dort kann man sich mit den oben angegebenen Anmeldedaten in den Administrator-Account einloggen.

Für die Erstellung eines dauerhaften Admin-Accounts muss dieser in Keycloak konfiguriert werden. Dazu auf der linken Seite auf **"Users"** klicken und dann auf **"Add User"**. Es öffnet sich ein Formular, in dem die Daten des neuen Benutzers eingegeben werden können. Nach dem Erstellen können unter **"Credentials"** das Passwort gesetzt und unter **"Role Mappings"** die Rolle `admin` hinzugefügt werden. Danach kann man sich mit diesem neuen Benutzer im Keycloak-Admin-Panel anmelden.

Anschließend muss ein neues Realm erstellt werden. Dafür auf der linken Seite auf **"Realms"** klicken und **"Create Realm"** auswählen. Dort kann ein vorkonfiguriertes Realm aus dem Ordner `keycloak` (Datei `realm-export.json`) importiert werden. Die Aktion kann anschließend bestätigt und der Browser geschlossen werden.

Falls nicht das gesamte Realm, sondern nur der Client (Datei `balancebook.json`) importiert werden soll, sind weitere Anpassungen erforderlich, die sich am importierten Realm orientieren. Dieser Vorgang wird nur Personen empfohlen, die mit Keycloak vertraut sind. In diesem Fall müssen auch die Realm-Namen im Backend (`application.properties`) und im Frontend (`HttpClient.js`) angepasst werden.

### 4. DNS-Weiterleitung einrichten

Damit die Keycloak- und Backend-Instanz korrekt erreichbar sind, muss eine DNS-Weiterleitung eingerichtet werden. Dazu ist die Datei `/etc/hosts` (unter Windows `C:\Windows\System32\drivers\etc\hosts`) zu bearbeiten. Es muss folgende Zeile hinzugefügt werden:

```
127.0.0.1 auth.local
```

Gegebenenfalls muss der Editor mit Administratorrechten gestartet werden, um die Datei bearbeiten zu können.

Durch diese Weiterleitung werden lokale Aufrufe an `auth.local` an die Keycloak-Instanz geleitet. So können auch die Backend- und Frontend-Instanz unter dieser Adresse mit Keycloak kommunizieren.

### 5. Docker-Compose-Befehl ausführen

Vor dem Start der Anwendung sollten die Datenbankpasswörter in der Docker-Compose-Datei sowohl beim MariaDB-Container als auch beim Backend-Container angepasst werden.

Damit die DNS-Weiterleitung in der Docker-Compose-Date korrekt funktioniert, muss die Docker Version 20.10 oder höher installiert sein. Bei älteren Versionen kann es zu Problemen kommen, da die Container nicht auf die lokale DNS-Weiterleitung über host-gateway zugreifen können.

angepasst werden. Die eigene IP-Adresse kann unter Windows mit dem Befehl `ipconfig` in der Eingabeaufforderung (IPv4-Adresse des Ethernet-Adapters) ermittelt werden.

Zum Start der Anwendung im Terminal den folgenden Befehl ausführen:

```
docker compose up --build
```

Dabei muss sich das Terminal im Verzeichnis des geklonten Repositories befinden.

## Benutzung

### Frontend

Das Frontend ist unter `http://localhost:3000` erreichbar. Dort muss sich der Benutzer zunächst registrieren, da die in Keycloak angelegten Benutzerdaten getrennt von den BalanceBook-Nutzerdaten gespeichert werden. Nach der Registrierung erfolgt eine Weiterleitung zur Hauptseite. Dort sind folgende Funktionen verfügbar:

- **Mein Profil**: Bearbeiten, Zahlungsinformationen hinzufügen/löschen/bearbeiten
- **Mitglieder**: Anzeigen, inklusive Zahlungsinformationen
- **Virtuelle Konten**: Anzeigen, Erstellen, Bearbeiten, Löschen
- **Externe Konten**: Anzeigen, Erstellen, Bearbeiten, Löschen, inklusive Zahlungsinformationen
- **Transaktionen**: Anzeigen & Filtern (alle), Erstellen, Bearbeiten, Löschen (nur eigene Transaktionen)

### Backend

Das Backend ist unter `http://localhost:8080` erreichbar.

Die verfügbaren Endpunkte sind über Swagger unter  
`http://localhost:8080/swagger-ui/index.html` dokumentiert.

Ein Token kann über folgenden Keycloak-Endpunkt angefordert werden:

```
POST http://localhost:8081/realms/balancebook-realm/protocol/openid-connect/token

Body (x-www-form-urlencoded):
grant_type: password
client_id: balancebook
username: <angelegter Benutzername>
password: <angelegtes Passwort>
```

Der Benutzer muss zuvor manuell in Keycloak im Client „balancebook“ angelegt worden sein – entweder über die Admin-Konsole oder über das Testfrontend bei der Registrierung.

### Keycloak

Der Keycloak-Account für den Admin ist erreichbar unter `http://localhost:8081`. Dort kann der Admin den Keycloak-Account bearbeitet.

Unter der URL `http://localhost:8081/realms/balancebook-realm/account/` kann sich der Benutzer von BalanceBook anmelden und sein Keycloak-Profil einsehen bzw. bearbeiten.

### Allgemeine Informationen

Die Anwendung ist ein Konzept zur Dokumentation gemeinsamer Gruppenüberweisungen. Sie ist derzeit nicht für den produktiven Einsatz geeignet. Vor einem Einsatz müssen Sicherheitsaspekte aller Komponenten überprüft und angepasst werden. Detaillierte Informationen hierzu sind im Ausblick der Studienarbeit enthalten.

Die Anwendung wurde durch Integrationstests und statische Codeanalyse überprüft. Diese Tests werden bei jedem Push oder Pull-Request auf den Branch `main` automatisch ausgeführt.

#### Integrationstests

Die Integrationstests sind im Backend mittels JUnit implementiert. Zur Ausführung muss eine MariaDB-Instanz mit den Konfigurationen aus der `application.properties` im Testverzeichnis laufen. Über die Tests sind alle REST-Endpunkte abgedeckt, womit über 80% des gesamten Codes getestet werden.

#### Statische Codeanalyse

Die Ergebnisse sind unter folgender Adresse einsehbar:  
`https://sonarcloud.io/organizations/mobmonrob-elinor2/projects`  
Nach der Anmeldung über einen GitHub-Account der Projektbearbeiter ist, stehen dort detaillierte Informationen zur Verfügung.

Aktuelle Ergebnisse:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mobmonrob-elinor2_mobmonrob-elinor2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mobmonrob-elinor2_mobmonrob-elinor2)  
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mobmonrob-elinor2_mobmonrob-elinor2&metric=bugs)](https://sonarcloud.io/summary/new_code?id=mobmonrob-elinor2_mobmonrob-elinor2)  
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mobmonrob-elinor2_mobmonrob-elinor2&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=mobmonrob-elinor2_mobmonrob-elinor2)  
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mobmonrob-elinor2_mobmonrob-elinor2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mobmonrob-elinor2_mobmonrob-elinor2)
