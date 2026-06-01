# Dificultades y Soluciones

## Introducción

Durante el desarrollo del proyecto surgieron diversos desafíos técnicos y arquitectónicos que obligaron al equipo a replantear varias decisiones de diseño. Esta sección documenta los principales problemas encontrados, las alternativas evaluadas y las soluciones implementadas.

---

# 1. Acoplamiento entre algoritmos y persistencia

## Situación inicial

Durante las primeras semanas del proyecto, las operaciones del árbol se desarrollaron directamente sobre las entidades utilizadas para persistencia.

Esta aproximación funcionaba para operaciones simples como la creación de nodos raíz y la inserción de hijos. Sin embargo, conforme aumentó el número de operaciones requeridas, comenzaron a aparecer limitaciones arquitectónicas importantes.

## Problema detectado

La lógica de los algoritmos dependía directamente de las entidades de base de datos.

Esto provocaba varios inconvenientes:

* El Tree Engine dejaba de ser independiente.
* Las modificaciones en las entidades afectaban directamente los algoritmos.
* Las estrategias `CollectionsTreeAlgorithmStrategy` y `CustomTreeAlgorithmStrategy` no disponían de una representación común de los nodos.
* El motor de algoritmos comenzaba a depender de detalles específicos de persistencia.

## Solución implementada

Se diseñó un modelo de dominio independiente compuesto por:

* `Node`
* `TreeView`

Estos objetos se convirtieron en el contrato común entre el Tree Engine, los servicios de aplicación y las distintas estrategias de persistencia.

De esta forma, las entidades quedaron limitadas exclusivamente a la capa de persistencia mientras que los algoritmos operan únicamente sobre objetos de dominio.

## Resultado

La solución permitió desacoplar completamente la lógica de negocio de la infraestructura de almacenamiento, facilitando la coexistencia de múltiples algoritmos y múltiples tecnologías de persistencia.

---

# 2. Ejecución de operaciones de consulta sin mantener estado en memoria

## Situación inicial

Durante el diseño inicial surgió la posibilidad de mantener la estructura del árbol cargada en memoria y reutilizarla para responder las operaciones de consulta.

Incluso se consideró reconstruir automáticamente el árbol al iniciar la aplicación.

## Problema detectado

Tras analizar esta aproximación se identificaron varios riesgos:

* La estructura en memoria podía quedar desactualizada respecto a la base de datos.
* El sistema comenzaría a depender de información almacenada en memoria.
* Las operaciones GET podrían devolver información inconsistente.
* El Tree Engine dejaría de comportarse como un motor de algoritmos independiente.

## Solución implementada

Se redefinió la responsabilidad de las estrategias de persistencia.

Para cada operación de consulta:

1. La estrategia de persistencia obtiene los nodos almacenados.
2. La lista de nodos es enviada al Tree Engine.
3. Cada estrategia reconstruye internamente la jerarquía utilizando sus propios builders.
4. El algoritmo ejecuta la operación solicitada.

Esta decisión permitió mantener la independencia entre persistencia y algoritmos.

## Resultado

El Tree Engine opera siempre sobre información obtenida desde la base de datos, evitando dependencias de estado interno y garantizando consistencia en las consultas.

---

# 3. Configuración simultánea de MongoDB y JPA

## Situación inicial

El sistema fue diseñado para soportar múltiples tecnologías de persistencia:

* H2
* PostgreSQL
* MongoDB

La selección de la tecnología debía realizarse mediante configuración sin modificar el código fuente.

## Problema detectado

Al incorporar MongoDB aparecieron conflictos durante el arranque de Spring Boot.

Aunque se seleccionaba MongoDB mediante propiedades de configuración, Spring continuaba intentando inicializar componentes asociados a JPA e Hibernate.

Como consecuencia:

* Se activaban componentes innecesarios.
* Aparecían conflictos entre tecnologías de persistencia.
* La aplicación no iniciaba correctamente.

## Solución implementada

Se reorganizó la configuración utilizando `@ConditionalOnProperty` para activar únicamente los componentes necesarios.

Además, en la configuración específica de MongoDB se incorporaron exclusiones para evitar que Spring cargara componentes asociados a JPA e Hibernate cuando la aplicación se ejecuta utilizando MongoDB.

## Resultado

Cada tecnología de persistencia quedó correctamente aislada y únicamente se inicializan los componentes requeridos para la configuración seleccionada.

---

# 4. Inconsistencias entre OpenAPI Generator y Eclipse

## Situación inicial

El proyecto adoptó un enfoque Contract-First basado en OpenAPI Generator.

El contrato definido en OpenAPI era utilizado para generar automáticamente modelos, interfaces y controladores.

## Problema detectado

Durante el desarrollo surgieron varios inconvenientes:

* Sensibilidad del archivo YAML a errores de indentación.
* Errores de generación causados por modificaciones mínimas.
* Imports marcados como inválidos dentro de Eclipse.
* Diferencias entre el estado reportado por Eclipse y el resultado real de la compilación Maven.

En múltiples ocasiones la aplicación compilaba correctamente mientras Eclipse seguía reportando errores inexistentes.

## Solución implementada

Se adoptaron varias medidas:

* Validación constante del archivo OpenAPI.
* Verificación del estado real del proyecto mediante Maven.
* Ejecución de Maven Update cuando aparecían inconsistencias.
* Reinicio del IDE cuando el índice interno quedaba desincronizado.

## Resultado

El equipo logró continuar el desarrollo sin interrupciones importantes provocadas por problemas específicos del entorno de desarrollo.
