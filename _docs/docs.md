### PostgreSQL
### 1. Arrêter et nettoyer
docker compose down -v

### 2. Vérifier que tout est bien supprimé
docker volume ls
docker ps -a

### 3. Relancer
docker compose up -d

### 4. Vérifier les logs
docker logs -f postgres-bsn

### 5. Tester la connexion
docker exec -it postgres-bsn psql -U username -d bsndb

### Install openapi-generator (inside ui project)
- Copy swagger specification and add it in openapi.json

### Code input
- **$ npm i angular-code-input**

### La gestion des exceptions
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // exceptions to handle
}
```

### Swagger
- Swagger est une librairie pour documenter les endpoints
- Elle permet également de ne plus avoir à créer manuellement ses services du coté de Angular.

#### 1. Configuration
- Il faut le configurer d'abord du coté backend. 
- Ensuite, démarer le projet backend. 
- Ouvrir Swagger sur le navigateur et cliquer sur le lien /api/v1/v3/api-docs 
- Copier toute la config et les endpoints générés et la coller dans un fichier openapi.json dans le src du frontend.

#### Générer des services avec Swagger (Angular)
- Installer le CLI `ng-openapi-gen`: npm install ng-openapi-gen
- Ajouter cette commande dans package.json :
```json
{
  "scripts": {
    "api-gen": "ng-openapi-gen --input ./src/openapi/openapi.json --output ./src/app/services"
  }
}
```
- En faisant `$ npm run api-gen`, nous disons à open api de traduire le contenu de openapi.json en service et les 
 mettre dans le dossier services.

### Interceptors
- Interceptor is like Spring Filter

### Angular : Guard, decode the token with @auth0/angular-jwt
$ npm install @auth0/angular-jwt

## Keycloak
### 1. Create a realm
### 2. Create a client

### Configure Keycloak in Angular
- Install keycloak-js :
  $ npm i keycloak-js@24.0.2