# Proyecto Final — Programación 3 — Grupo 2

Backend REST API para gestión de árboles genéricos con estrategias intercambiables de algoritmos y persistencia. Para documentacion especifica y util, mira carpeta /docs, ademas tambien hay diagramas de flujo de como es proyecto, ejecucion del proyecto, interfaces, flujo de GET y POST. Estos diagramas de flujo se encuentra en la carpeta /Diagramas.

LINK TRELLO: [Grupo 2](https://trello.com/b/PIkiqf3t/proyecto-final-progra-3)
---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.5 | Framework backend |
| Maven | Multimodulo | Gestión de dependencias y compilación |
| OpenAPI Generator | 7.6 | Generación de código contract-first |
| Hibernate / JPA | 6.6 | ORM para H2 y PostgreSQL |
| Spring Data MongoDB | 5.5 | Persistencia en MongoDB |
| Docker | - | Contenedores para PostgreSQL y MongoDB |

---

## Arquitectura

El proyecto está dividido en dos módulos Maven:

```
proyecto-final/
├── tree-engine/    ← Lógica de algoritmos (JAR puro, sin Spring)
└── app/            ← API REST + persistencia (Spring Boot)
```

### tree-engine

Módulo independiente sin dependencias de Spring ni bases de datos. Contiene:

- **`Node`** — objeto de dominio (`id`, `value`, `parentId`, `treeId`)
- **`TreeView`** — representación jerárquica del árbol con hijos anidados
- **`ITreeAlgorithmStrategy`** — interfaz con 11 operaciones de árbol
- **`CollectionsTreeAlgorithmStrategy`** — implementación con Java Collections (Persona B)
- **`CustomTreeAlgorithmStrategy`** — implementación con estructura interna `TreeNode` (Persona A)
- **`TreeService`** — facade que delega a la estrategia activa

### app

Módulo Spring Boot con tres capas:

- **Controller** — generado por OpenAPI Generator desde `openapi.yaml`
- **Service** — `NodeService` orquesta `TreeService` + `PersistenceStrategy`
- **Persistence** — `PersistenceStrategy` intercambiable: JPA o MongoDB (Persona C)

---

## Patrones de Diseño

### Strategy Pattern — doble aplicación

```
ITreeAlgorithmStrategy          PersistenceStrategy
├── CollectionsStrategy         ├── JpaPersistenceStrategy
└── CustomStrategy              └── MongoPersistenceStrategy
```

Permite cambiar el algoritmo y la base de datos en tiempo de ejecución sin modificar el código.

### Contract-First

El archivo `openapi.yaml` define el contrato de la API. El plugin `openapi-generator` genera automáticamente los modelos y las interfaces de los controllers en cada compilación.

### Configuración condicional sin perfiles Spring

`@ConditionalOnProperty` activa los beans según las propiedades pasadas por línea de comandos:

| Propiedad | Valor | Bean activado |
|---|---|---|
| `app.persistence.type` | `jpa` | `JpaPersistenceStrategy` |
| `app.persistence.type` | `mongo` | `MongoPersistenceStrategy` |
| `app.tree.strategy` | `collections` | `CollectionsTreeAlgorithmStrategy` |
| `app.tree.strategy` | `custom` | `CustomTreeAlgorithmStrategy` |

---

## Endpoints

| # | Método | Endpoint | Descripción |
|---|---|---|---|
| 1 | POST | `/nodes/root` | Crear nodo raíz |
| 2 | POST | `/nodes/{parentId}/children` | Agregar hijo |
| 3 | GET | `/tree` | Obtener todos los árboles |
| 4 | GET | `/tree/{nodeId}` | Obtener subárbol |
| 5 | GET | `/nodes/{nodeId}/path` | Ruta desde raíz a nodo |
| 6 | GET | `/tree/traversal?type=DFS\|BFS` | Recorrido DFS o BFS |
| 7 | GET | `/tree/height?treeId={id}` | Altura del árbol |
| 8 | GET | `/nodes/{nodeId}/depth` | Profundidad de un nodo |
| 9 | GET | `/nodes/{nodeId}/ancestors` | Ancestros de un nodo |
| 10 | GET | `/tree/validate?treeId={id}` | Validar que no haya ciclos |
| 11 | PUT | `/tree/{treeId}` | Actualizar datos del árbol |

Documentación interactiva: `http://localhost:8080/swagger-ui.html`

---

## Ejecución

> El proyecto **no usa perfiles de Spring**. La configuración se selecciona por línea de comandos.

### Compilar

```cmd
mvn clean install -DskipTests
```

### H2 (sin Docker)

```cmd
mvn -pl app spring-boot:run "-Dspring-boot.run.arguments=--spring.config.location=classpath:/application-h2.properties --app.tree.strategy=collections"
```

### PostgreSQL

```cmd
docker run -d --name postgres-auto-bom -p 5432:5432 -e POSTGRES_DB=auto_bom -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres postgres:latest

mvn -pl app spring-boot:run "-Dspring-boot.run.arguments=--spring.config.location=classpath:/application-postgres.properties --app.tree.strategy=collections"
```

### MongoDB

```cmd
docker run -d --name mongo-arbol -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin mongo:latest

mvn -pl app spring-boot:run "-Dspring-boot.run.arguments=--spring.config.location=classpath:/application-mongodb.properties --app.tree.strategy=collections"
```

> Reemplaza `--app.tree.strategy=collections` por `--app.tree.strategy=custom` para usar la estrategia alternativa.

---

## Publicación del Backend mediante Ngrok (Opcional)

Durante el desarrollo se utilizó Ngrok para exponer temporalmente el backend a Internet y permitir la integración con herramientas externas de generación de frontend.

### Requisitos

- Backend ejecutándose localmente.
- Cuenta de Ngrok.
- Authtoken configurado.

### Iniciar el backend

```cmd
mvn -pl app spring-boot:run "-Dspring-boot.run.arguments=--spring.config.location=classpath:/application-postgres.properties --app.tree.strategy=collections"
```

Verificar que la aplicación responda correctamente:

```text
http://localhost:8080/health
```

### Publicar mediante Ngrok
Donde descargaste ngrok, abre un cmd y pon lo siguiente
```cmd
ngrok http 8080
```

Ngrok generará una URL similar a:

```text
https://abcd1234.ngrok-free.app
```

### Verificar acceso remoto

```text
https://abcd1234.ngrok-free.app/swagger-ui.html
```

o

```text
https://abcd1234.ngrok-free.app/actuator/health
```

### Consideraciones

- El proyecto incluye configuración específica para facilitar la integración mediante Ngrok.
- Si se utiliza un frontend hospedado en otro dominio, puede ser necesario habilitar la configuración CORS correspondiente, se encuetra en el paquete config.
- Las URLs generadas por Ngrok son temporales y cambian cada vez que se reinicia el túnel.
- Ngrok fue utilizado únicamente para pruebas e integración durante el desarrollo.

Para más detalles consultar:

```text
docs/DESAFIOS_DE_INTEGRACION_Y_DESPLIEGUE.md
```

---
## Manejo de Errores

| Excepción | HTTP | Cuándo |
|---|---|---|
| `NodeNotFoundException` | 404 | Nodo o árbol no encontrado |
| `RootAlreadyExistsException` | 409 | Ya existe una raíz |
| `InvalidNodeValueException` | 400 | Valor del nodo inválido |
| `CycleDetectedException` | 400 | Ciclo detectado en el árbol |
| `Exception` | 500 | Error interno del servidor |

---

## Equipo

| Persona | Responsabilidad |
|---|---|
| A | `CustomTreeAlgorithmStrategy` |
| B | `CollectionsTreeAlgorithmStrategy` + PostgreSQL |
| C | MongoDB + beans condicionales |