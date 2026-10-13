# Pokémon Card Collection API

## Inhoudsopgave

1. [Inleiding](#inleiding)
2. [Benodigdheden](#benodigdheden)
3. [Projectstructuur en technieken](#projectstructuur-en-technieken)
4. [Installatie en configuratie](#installatie-en-configuratie)
5. [Applicatie starten](#applicatie-starten)
6. [Testgebruikers en rollen](#testgebruikers-en-rollen)
7. [API gebruiken met Postman](#api-gebruiken-met-postman)
8. [Tests uitvoeren](#tests-uitvoeren)

## Inleiding

De Pokémon Card Collection API helpt gebruikers hun Pokémonkaartencollectie te beheren.

De huidige applicatie ondersteunt:

* Pokémonkaarten bekijken;
* kaarten toevoegen aan, wijzigen in en verwijderen uit een persoonlijke collectie;
* persoonlijke favorieten beheren;
* een gebruikersprofiel beheren;
* als beheerder een overzicht van lokale gebruikers ophalen.

De API gebruikt Keycloak voor authenticatie en autorisatie. Gebruikers kunnen uitsluitend hun eigen collectie, favorieten en profiel beheren.

## Benodigdheden

* Java JDK 25;
* Maven, of de Maven Wrapper wanneer deze in de projectmap aanwezig is;
* PostgreSQL;
* Keycloak;
* Postman voor het handmatig uitvoeren van API-requests;
* Git om de repository te klonen.

De onderstaande terminalcommando’s zijn geschreven voor Windows PowerShell.

## Projectstructuur en technieken

| Locatie                                               | Inhoud                                 |
| ----------------------------------------------------- | -------------------------------------- |
| `src/main/java/nl/remcokuipers/pokemoncardcollection` | Java-broncode                          |
| `config`                                              | Securityconfiguratie                   |
| `controller`                                          | REST-endpoints                         |
| `service`                                             | Applicatielogica                       |
| `repository`                                          | Databasecommunicatie                   |
| `entity`                                              | Databasemodellen                       |
| `dto`                                                 | Invoer- en uitvoerobjecten             |
| `mapper`                                              | Omzetting tussen entiteiten en DTO’s   |
| `exception`                                           | Exceptions en centrale foutafhandeling |
| `src/main/resources`                                  | Configuratie en initiële testdata      |
| `src/test/java`                                       | Geautomatiseerde tests                 |
| `docs/keycloak`                                       | Keycloak-realmexport                   |
| `pom.xml`                                             | Maven-configuratie en dependencies     |

De packages van `config` tot en met `exception` staan onder het genoemde Java-basispakket.

De API gebruikt Spring Boot 4.1.1 en Spring MVC voor de REST-endpoints, Spring Data JPA voor PostgreSQL en Spring Security voor het verwerken van OAuth2/JWT-tokens. Invoer wordt gevalideerd met Jakarta Validation. Tests gebruiken JUnit, Mockito en MockMvc. Maven beheert de dependencies en bouwt het project.

## Installatie en configuratie

### 1. Project ophalen

```powershell
git clone https://github.com/RemcoKuipers/Pokemon-Card-Collection.git
cd Pokemon-Card-Collection
```

### 2. PostgreSQL voorbereiden

Start PostgreSQL op poort `5432` en maak een database aan:

```sql
CREATE DATABASE pokemon_card_collection_db;
```

De applicatie gebruikt standaard:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pokemon_card_collection_db
spring.datasource.username=postgres
spring.datasource.password=${POSTGRES_PASSWORD}
```

Gebruik je een andere databasegebruiker, poort of databasenaam? Pas deze waarden dan aan in `src/main/resources/application.properties`.

Stel in de PowerShell-sessie waarin je de API start het databasewachtwoord in:

```powershell
$env:POSTGRES_PASSWORD = "VUL_HIER_JE_LOKALE_DATABASEWACHTWOORD_IN"
```

Deze variabele geldt voor de huidige PowerShell-sessie. Start je de applicatie vanuit IntelliJ, voeg `POSTGRES_PASSWORD` dan toe aan de omgevingsvariabelen van de runconfiguratie.

**Let op:** de huidige configuratie gebruikt `spring.jpa.hibernate.ddl-auto=create`. Bij iedere start worden de applicatietabellen opnieuw aangemaakt en wordt `data.sql` uitgevoerd. Gebruik hiervoor een aparte ontwikkel- of testdatabase; bestaande applicatiegegevens gaan verloren.

### 3. Keycloak voorbereiden

Start je lokale Keycloak-installatie op poort `9090`. Bij een uitgepakte Keycloak-distributie kan dat vanuit de Keycloak-installatiemap met:

```powershell
.\bin\kc.bat start-dev --http-port=9090
```

Open de Admin Console op `http://localhost:9090` en log in met je lokale Keycloak-beheeraccount.

Importeer bij het aanmaken van een realm:

```text
docs/keycloak/PokemonCardCollection-realm.json
```

Selecteer daarna de realm `PokemonCardCollection`. Controleer dat de client `PokemonCardCollection` aanwezig is en de clientrollen `USER` en `ADMIN` bevat.

Open bij deze client **Credentials** en genereer een nieuwe client secret voor gebruik in Postman.

Controleer of de realm bereikbaar is via:

```text
http://localhost:9090/realms/PokemonCardCollection/.well-known/openid-configuration
```

Deze URL moet een JSON-document teruggeven.

### 4. Securityconfiguratie controleren

In `application.properties` staan:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/PokemonCardCollection
spring.security.oauth2.resourceserver.jwt.audience=PokemonCardCollection
client-id=PokemonCardCollection
```

De realmnaam, client-ID en audience zijn hoofdlettergevoelig.

## Applicatie starten

Zorg dat PostgreSQL en Keycloak draaien. Voer vanuit de projectmap uit:

```powershell
mvn spring-boot:run
```

Wanneer je de Maven Wrapper gebruikt:

```powershell
.\mvnw.cmd spring-boot:run
```

De API is bereikbaar op:

```text
http://localhost:8080
```

Stop de applicatie in de terminal met `Ctrl+C`.

## Testgebruikers en rollen

Controleer na de realmimport onderstaande accounts. Maak ontbrekende accounts aan en stel via **Credentials** lokale testwachtwoorden in.

| Account        | E-mail               | Clientrol |
| -------------- | -------------------- | --------- |
| `test`         | `test@test.nl`       | USER      |
| `test2`        | `test2@test.nl`      | USER      |
| `test_admin`   | `testadmin@test.nl`  | ADMIN     |
| `test_no_role` | `testnorole@test.nl` | Geen      |

Wijs rollen toe via **Role mapping** bij de gebruiker. Kies de clientrollen van `PokemonCardCollection`, niet alleen gelijknamige realmrollen.

Bij `test2` ontbreekt de USER-clientrol in de bestaande export; voeg deze na import toe. Controleer bij `test_no_role` dat het account ook geen USER- of ADMIN-clientrol erft.

USER heeft toegang tot kaarten en de eigen collectie, favorieten en profiel. ADMIN heeft toegang tot `GET /admin/users`. Een ADMIN-account heeft niet automatisch de USER-rol.

Een account heeft een gevulde username en e-mail nodig voor automatische lokale gebruikersaanmaak.

## API gebruiken met Postman

Importeer het bestand `Pokémon Card Collection API.postman_collection.json` uit de repository. Stel de collectievariabele `baseUrl` in op:

```text
http://localhost:8080
```

De collectie gebruikt deze lokale Vault-verwijzingen:

| Vault-naam         | In te vullen waarde                  |
| ------------------ | ------------------------------------ |
| `auth_secret_07gb` | `PokemonCardCollection`              |
| `auth_secret_0oww` | De lokaal gegenereerde client secret |

Maak deze twee waarden aan in Postman Vault.

Gebruik voor het ophalen van een token OAuth 2.0 met **Authorization Code**:

| Instelling       | Waarde                                                                             |
| ---------------- | ---------------------------------------------------------------------------------- |
| Callback URL     | `https://oauth.pstmn.io/v1/callback`                                               |
| Auth URL         | `http://localhost:9090/realms/PokemonCardCollection/protocol/openid-connect/auth`  |
| Access Token URL | `http://localhost:9090/realms/PokemonCardCollection/protocol/openid-connect/token` |
| Client ID        | `PokemonCardCollection`                                                            |
| Client secret    | Je lokale client secret                                                            |
| Scope            | `openid profile email`                                                             |

Haal een token op, log in met het gewenste testaccount en kies **Use Token**. Controleer per request of het juiste account wordt gebruikt. Voor requests zonder authenticatie kies je **No Auth**.

De collectie bevat vaste voorbeeld-IDs. Pas deze aan aan de aanwezige testdata. Voor eigenaarschapschecks maak je gegevens aan als `test` en probeer je die met `test2` te benaderen. Controleer daarna als eigenaar of de gegevens intact zijn.

## Tests uitvoeren

Voer vanuit de projectmap uit:

```powershell
mvn test
```

Of met de Maven Wrapper:

```powershell
.\mvnw.cmd test
```

Stel vooraf `POSTGRES_PASSWORD` in. De huidige applicatiecontexttest gebruikt de database- en Keycloak-configuratie van de applicatie. PostgreSQL en Keycloak moeten daarom draaien. Ook hierbij kan de testdatabase opnieuw worden aangemaakt.

De afzonderlijke service- en controllertests gebruiken mocks en hebben deze externe diensten niet nodig.

De huidige tests controleren onder andere:

* lokale gebruikersaanmaak en hergebruik;
* ontbrekende of lege verplichte claims;
* conflicten met bestaande usernames en e-mailadressen;
* ontbrekende authenticatie en een ongeschikte principal;
* de 403-JSON-response bij een onvolledig account.

De MockMvc-controllertest controleert de response van de exceptionhandler en laadt geen securityfilters.

De Postman-collectie bevat daarnaast handmatige controles voor authenticatie, rollen en eigenaarschap. Voorbeelden zijn 401 zonder token, 403 bij onvoldoende rechten en 404 bij het benaderen van andermans gegevens.
