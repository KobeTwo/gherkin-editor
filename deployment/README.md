# Gherkin-Editor Deployment
Gherkin-editor is build on top of spring boot. You can run the app via Java directly or containerized via Docker, docker-compose or Kubernetes.

## Prerequisites
To run gherkin-editor you need a running instance of elasticsearch to store all your data.
- elasticsearch 2.4 or higher
- java 8 or higher
 Running the app
### run jar via java

```
java -jar gherkin-editor.v.0.0.23.jar
```

### run via docker
```
docker run kobetwo/gherkin-editor
```
### run via docker-compose
See the [docker compose example deployment](./docker-compose).

### run via kubernetes
See the [kubernetes example deployment](./kubernetes).

## Configuration and profiles

## Backup and restore
All information is stored in elasticsearch. For backup and restore visit the official documentation: 
https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-snapshots.html
