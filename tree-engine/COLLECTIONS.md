# Proyecto Final — Programación 3 — Grupo 2
## Documentación — Persona B: CollectionsTreeAlgorithmStrategy + PostgreSQL
 
---
 
## Responsabilidades
 
- `CollectionsTreeAlgorithmStrategy` — algoritmos de árbol con Java Collections
- Configuración y conexión con **PostgreSQL**
---
 
## Archivos
 
```
tree-engine/
└── src/main/java/com/grupo2/treeengine/
    └── strategy/impl/
        └── CollectionsTreeAlgorithmStrategy.java
 
app/
└── src/main/resources/
    └── application-postgres.properties
```
 
---
 
## CollectionsTreeAlgorithmStrategy
 
Implementación de `ITreeAlgorithmStrategy` que opera directamente sobre `List<Node>` usando Java Collections. **Sin estado interno** — todos los métodos reciben la lista como parámetro.
 
### Algoritmos implementados
 
| Operación          | Estructura usada        | Descripción                             |
|--------------------|-------------------------|-----------------------------------------|
| `buildTree`        | Stream + recursión      | Construye TreeView desde la raíz        |
| `buildSubTree`     | Stream + recursión      | Construye TreeView desde cualquier nodo |
| `dfs`              | `ArrayDeque` como pila  | Recorrido en profundidad                |
| `bfs`              | `ArrayDeque` como cola  | Recorrido en amplitud                   |
| `getPathToRoot`    | While loop ascendente   | Ruta desde nodo hasta la raíz           |
| `getAncestors`     | While loop ascendente   | Todos los ancestros sin el nodo mismo   |
| `getHeight`        | Recursión               | Altura máxima del árbol                 |
| `getDepth`         | While loop ascendente   | Profundidad contando ancestros          |
| `validateNoCycles` | DFS con HashSet         | Detecta ciclos en el árbol              |
 
### DFS — usa ArrayDeque como pila (LIFO)
 
```java
ArrayDeque<UUID> stack = new ArrayDeque<>();
stack.push(root.getId());
 
while (!stack.isEmpty()) {
    UUID currentId = stack.pop();
    // agregar nodo al resultado
    // agregar hijos en orden inverso para mantener orden
}
```
 
### BFS — usa ArrayDeque como cola (FIFO)
 
```java
ArrayDeque<UUID> queue = new ArrayDeque<>();
queue.add(root.getId());
 
while (!queue.isEmpty()) {
    UUID currentId = queue.poll();
    // agregar nodo al resultado
    // agregar hijos al final de la cola
}
```
 
### Diferencia DFS vs BFS
 
Con este árbol:
```
      raíz
     /    \
  hijo1   hijo2
    |
 nieto1
```
 
- **DFS**: raíz → hijo1 → nieto1 → hijo2
- **BFS**: raíz → hijo1 → hijo2 → nieto1
---
 
## Configuración PostgreSQL
 
### `application-postgres.properties`
 
```properties
app.persistence.type=jpa
 
spring.datasource.url=jdbc:postgresql://localhost:5432/auto_bom
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
 
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
 
---
 
## Ejecución con PostgreSQL
 
### 1. Compilar el proyecto
 
```cmd
mvn clean install -DskipTests
```
 
### 2. Levantar PostgreSQL con Docker
 
```cmd
docker run -d --name postgres-auto-bom -p 5432:5432 ^
  -e POSTGRES_DB=auto_bom ^
  -e POSTGRES_USER=postgres ^
  -e POSTGRES_PASSWORD=postgres ^
  postgres:latest
```
 
### 3. Ejecutar la aplicación
 
```cmd
mvn -pl app spring-boot:run "-Dspring-boot.run.arguments=--spring.config.location=classpath:/application-postgres.properties --app.tree.strategy=collections"
```
 
### 4. Verificar datos en la BD
 
```cmd
docker exec -it postgres-auto-bom psql -U postgres -d auto_bom -c "SELECT * FROM trees;"
docker exec -it postgres-auto-bom psql -U postgres -d auto_bom -c "SELECT * FROM nodes;"
```
 
---
 
## Endpoints validados con PostgreSQL + Collections
 
| Método | Endpoint                             | Resultado          |
|--------|--------------------------------------|--------------------|
| POST   | `/nodes/root`                        | 201 Created ✅     |
| POST   | `/nodes/{parentId}/children`         | 201 Created ✅     |
| GET    | `/tree`                              | 200 OK ✅          |
| GET    | `/tree/{nodeId}`                     | 200 OK ✅          |
| GET    | `/nodes/{nodeId}/path`               | 200 OK ✅          |
| GET    | `/tree/traversal?type=DFS`           | 200 OK ✅          |
| GET    | `/tree/traversal?type=BFS`           | 200 OK ✅          |
| GET    | `/tree/height?treeId={id}`           | 200 OK ✅          |
| GET    | `/nodes/{nodeId}/depth`              | 200 OK ✅          |
| GET    | `/nodes/{nodeId}/ancestors`          | 200 OK ✅          |
| GET    | `/tree/validate?treeId={id}`         | 200 OK ✅          |
| PUT    | `/tree/{treeId}`                     | 200 OK ✅          |
| GET    | `/nodes/{uuid-inexistente}/depth`    | 404 Not Found ✅   |
| POST   | `/nodes/{uuid-inexistente}/children` | 404 Not Found ✅   |