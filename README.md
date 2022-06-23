# Sistema Bancario

Michele Banfi 869294  
Stefano Quaggio 866504

## Endpoint

Gli endpoint che il nostro server espone sono i seguenti:

- `/api/account`

  - GET
  - POST
  - DELETE

- `/api/account/{accountId}`

  - GET
  - POST
  - PUT
  - PATCH
  - HEAD

- `/api/transfer`

  - POST

- `/api/divert`
  - POST

Mentre il frontend web è esposto ai seguenti endpoint:

- `/`

  Permette di listare le transazioni dato un accountId appoggiandosi all'endpoint `/api/account/{accountId}` con richiesta `GET`, e permette inoltre di poterle annullare, appoggiandosi all'endpoint `/api/divert` con richiesta `POST`

- `/transfer`

Espansioni frontend:

- `/list`

  Permette di listare tutti gli account registrati all'interno del sistema, appoggiandosi all'endpoint `/api/account` con richiesta `GET`. È anche possibile all'interno della pagina modificare nome e cognome del proprietario dell'account appoggiandosi all'endpoint `/api/account{accountId}` con richiesta `PUT`.

- `/register`

  Dà la possibilità di registrare un nuovo cliente tramite form, appoggiandosi all'endpoint `/api/account` con richiesta `POST`

## Database

Per il salvataggio dei dati, abbiamo optato per 2 file:

- `accounts.json`

In cui vengono salvate le info degli utenti registrati e le loro transazioni effettuate (come da richiesta)

- `transazioni.json`

In cui vengono salvate tutte le transazioni effettuate all'interno del sistema (per praticità di utilizzo all'interno di Java)
