# BalanceBook

BalanceBook ist eine Anwendung um eine gemeinsame Kasse in einer Gruppe zu führen. 
Die Anwendung umfasst ein Backend und ein Frontend. Bei der Konfiguration und Einrichtung über DOcker ist zusätzlich eine MariaDB-Instanz und eine keycloak-Instanz eingeschlossen.

## Inbetriebnahme
### 1. Repository klonen und Überprüfung auf die notwendige Software
Damit die Anwendung ausgeführt werden kann, muss der Quellcode lokal auf dem Rechner zur Verfügung stehen. Dafür muss dieses Repository in einen beliebigen lokalenn Zielordner geklont werden.

Bevor die Anwendung gestartet werden kann, muss sichergestellt werden, dass Docker installiert ist.

### 2. Keycloak-Container starten


Um die Keycloak-Instanz zu starten, muss der Docker-Container mit dem folgenden Befehl gestartet werden:`

```
docker run --name keycloak -p 8081:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=adminUser  -e KC_BOOTSTRAP_ADMIN_PASSWORD=adminUser --hostname auth.local quay.io/keycloak/keycloak:latest start-dev
```
Dabei sollten die Umgebungsvariablen `KC_BOOTSTRAP_ADMIN_USERNAME` und `KC_BOOTSTRAP_ADMIN_PASSWORD` angepasst werden. Diese Variablen sind für die Authentifizierung des Administrators in der Keycloak-Instanz verantwortlich.

### 3. Keycloak-Instanz konfigurieren
Um die Keycloak-Instanz zu konfigurieren, muss die URL http://localhost:8081 in einem Webbrowser aufgerufen werden. Dort kann der Administrator-Account mit den oben angegebenen Anmeldedaten eingeloggt werden.
Für die Erstellung eines permanenten Admin-Accounts muss der Admin-Account in der Keycloak-Instanz konfiguriert werden. Dazu muss auf der linken Seite auf "Users" geklickt werden und dann auf "Add User". Es öffnet sich ein Fenster, in dem die Daten des neuen Benutzers eingegeben werden können. Nach dem Erstellen kann unter Confidentials das Passwort bestätigt werden und unter Roles die admin Rolle hinzugefügt werden. Daraufhin kann sich in Zukunft mit dem neuen Benutzer in dem Keycloak-Admin-Fenster angemeldet werden.

Anschließend muss ein neues Realm erstellt werden. Dazu muss auf der linken Seite auf "Realms" geklickt werden und dann auf "Create Realm". Es öffnet sich eine Option ein bestehendes Realm zu importieren. Dabei kann das vorkonfigurierte Realm aus dem Ordner keycloak `realm-export.json` geladen werden. 
Anschließend kann die Aktion bestätigt und das Fenster im Browser wieder geschlossen werden.

Falls nicht das ganze Realm, sondern nur der Client importiert werden soll, müssen noch weitere Anpassungen welche sich am importierten Realm orientieren am bestehenden Realm angepasst werden. Daher wird dieser Import nur Personen empfohlen, die sich mit Keycloak auskennen. Dabei müssen dann auch die Realm-Namen im Backend (application.properties) und im Frontend (HttpClient.js) angepasst werden.

### 4. DNS-Weiterleitung einrichten

Damit eine korrekte Weiterleitung der Aufrufe an die Keycloak-Instanz und die Backend-Instanz erfolgen kann, muss eine DNS-Weiterleitung eingerichtet werden. Dazu muss die Datei `/etc/hosts` (unter Windows `C:\Windows\System32\drivers\etc\hosts`) bearbeitet werden. Dort muss folgende Zeile hinzugefügt werden:
`127.0.0.1 auth.local`
Dadurch werden lokal alle Aufrufe an `auth.local` an die Keycloak-Instanz weitergeleitet. Dadurch können über die gleiche Adresse auch die Backend-Instanz und die Frontend-Instanz mit der Keycloak-Instanz kommunizieren.

### 5. Docker-Compose-Befehl ausführen
Bevor die Anwendung ausgeführt wird, sollten die Datenbankpasswörter in der Docker-Compose-Datei beim MariaDB-Container und beim backend-Container neu gewählt werden.

Um die Anwendung im Anschluss zu starten, muss der Docker-Compose-Befehl im Terminal ausgeführt werden. 
Dabei muss sichergestellt werden, dass sich das Terminal im Verzeichnis des geklonten Repositories befindet. Der Befehl lautet:

`docker-compose up --build`

## Benutzung

### Frontend
Das Frontend kann unter der Adresse http://localhost:3000 aufgerufen werden. Dort muss sich der Benutzer zuerst neu registrieren, weil die vorher angelegten Keycloak-Benutzerdaten isoliert von den BalanceBook-Nutzerdaten liegen. Nach der Registrierung wird er zu Hauptseite weitergeleitet. Dort sind folgende Funkzionen verfügbar:
- **Mein Profil**: Bearbeiten, Zahlungsinformationen hinzufügen/löschen/bearbeiten
- **Mitglieder**: Einsehen inklusive der Zahlungsinformationen
- **Virtuelle Konten**: Einsehen, Erstellen, Bearbeiten, Löschen
- **Externe Konten**: Einsehen, Erstellen, Bearbeiten, Löschen inklusive der Zahlungsinformationen
- **Transaktionen**: Einsehen & Filtern (Alle), Erstellen & Bearbeiten & Löschen (nur die für mich bestimmten)

Falls manchmal der Inhalt der Seite nicht angezeigt wird (vor allem nach der Anmeldung), muss die Seite neu geladen werden (F5). Das Problem ist analysiert worden und hängt eventuell an einer fehlenden HTTPS-Verbindung in Kombination mit den Browser-Standards.

### Backend
Das Backend ist unter der Adresse http://localhost:8080 erreichbar. 

Die verfügbaren Endpunkte sind unter http://localhost:8080/swagger-ui/index.html über Swagger einsehbar.

Für den Erhalt des Tokens ist ein Endpunkt über Keycloak verfügbar:
```   
    POST http://localhost:8081/realms/balancebook-realm/protocol/openid-connect/token
	
    Body:
	grant-type: password
	client_id: balancebook
	username: angelegteruser_benutzername
	password angelegteruser_passwort
```
Dafür muss aber manuell ein User in Keycloak im Client des BalanceBooks angelegt werden. Das geht entweder über die Admin-Konsole oder über das Testfrontend beim Registrieren.

### Generelle Informationen
Die implementierte Anwendung ist auf den Testbetrieb ausgelegt. Das heißt, wenn ein Anwendungstest durchgeführt wurde, müssen die einzelnen Komponenten noch in Sicherheitsaspekten auf die Funktionstauglichkeit im Produktivbetrieb untersucht werden. Detaillierte Angaben sind im Ausblick der Studienarbeit zu finden.