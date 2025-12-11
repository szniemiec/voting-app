# System Głosowania - Aplikacja Spring Boot

## Opis
Aplikacja do zarządzania wyborami pozwalająca na rejestrację głosów w wielu instancjach wyborów.

## Konfiguracja i Uruchomienie

# 1. Utwórz bazę danych
createdb voting_db
oraz dostosuj dane usera postgresowego w application.yml

# 2. Uruchom aplikację

Endpointy: <br/>

Głosujący <br/>
```
POST /api/v1/voters - Rejestracja nowego wyborca
GET /api/v1/voters - Lista wszystkich wyborców
GET /api/v1/voters/{id} - Dane wyborca
PUT /api/v1/voters/{id}/block - Blokowanie wyborca
PUT /api/v1/voters/{id}/unblock - Odblokowanie wyborca
```
Wybory <br/>
```
POST /api/v1/elections - Utworzenie nowych wyborów
GET /api/v1/elections - Lista wszystkich wyborów
GET /api/v1/elections/{id} - Dane wyborów
GET /api/v1/elections/active - Aktywne wybory
PUT /api/v1/elections/{id}/status - Zmiana statusu
```
Głosy <br/>
```
POST /api/v1/votes - Oddanie głosu
GET /api/v1/votes/election/{electionId} - Wyniki wyborów
```

Przykłady zapytań:

Rejestracja wyborca: <br/>
```
curl --location 'http://localhost:8080/api/v1/voters' \
--header 'Content-Type: application/json' \
--data '{
    "peselNumber": "12345678901",
    "firstName": "Szymon",
    "lastName": "Niemiec"
}'
```

Utworzenie wyborów: <br/>
```
curl --location 'http://localhost:8080/api/v1/elections' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Wybory na Prezydenta Miasta Rzeszów 2025",
    "description": "Wybory na Prezydenta Miasta Rzeszów",
    "startDate": "2025-12-09T23:49:05.629+08:00",
    "endDate": "2025-12-15T23:49:05.629+08:00",
    "options": [
      {
        "label": "Kandydat A",
        "displayOrder": 1
      },
      {
        "label": "Kandydat B",
        "displayOrder": 2
      }
    ]
  }'
```

Oddanie głosu: <br/>
```
curl --location 'http://localhost:8080/api/v1/votes' \
--header 'Content-Type: application/json' \
--data '{
    "voterId": 1,
    "electionId": 1,
    "optionId": 1
  }'
```
