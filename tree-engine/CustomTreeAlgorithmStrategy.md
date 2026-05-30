# CustomTreeAlgorithmStrategy

## Dominio involucrado

La estrategia trabaja principalmente con las siguientes clases del
dominio:

-   `Node`: entidad persistida que representa un nodo del árbol.
-   `TreeView`: representación jerárquica utilizada en las respuestas
    GET.
-   `TreeNode`: estructura interna usada únicamente por el motor de
    árboles para reconstruir relaciones padre-hijo en memoria.
-   `ITreeAlgorithmStrategy`: contrato que define todas las operaciones
    del árbol.

## Propósito

`CustomTreeAlgorithmStrategy` es una implementación propia de
`ITreeAlgorithmStrategy` que ejecuta operaciones sobre árboles
utilizando algoritmos desarrollados manualmente, sin apoyarse en
estructuras avanzadas de bibliotecas externas.

## Patrón utilizado

Implementa el patrón Strategy.

Permite intercambiar dinámicamente implementaciones de algoritmos:

-   `CustomTreeAlgorithmStrategy`
-   `CollectionsTreeAlgorithmStrategy`

La selección se realiza mediante configuración de Spring.

------------------------------------------------------------------------

## Naturaleza Stateless

La estrategia es completamente stateless.

Características:

-   No posee atributos de instancia.
-   No mantiene estado entre peticiones.
-   No almacena árboles en memoria.
-   No utiliza cachés.
-   No conserva resultados anteriores.

Cada operación recibe:

``` java
List<Node> nodes
```

y reconstruye el árbol cuando es necesario.

Ejemplo:

``` java
TreeNode root = buildInternalTree(nodes);
```

Esto vuelve a ejecutarse en cada operación.

### Ventajas

-   Thread-safe.
-   Compatible con Singleton de Spring.
-   Escalable.
-   Sin problemas de sincronización.

------------------------------------------------------------------------

## Modelo de dominio

Persistencia:

``` text
Node
 ├── id
 ├── parentId
 ├── treeId
 └── value
```

Motor interno:

``` text
TreeNode
 ├── id
 ├── value
 └── children
```

Vista de salida:

``` text
TreeView
 ├── id
 ├── treeId
 ├── value
 └── children
```

La estrategia transforma continuamente:

``` text
Node -> TreeNode -> TreeView
```

------------------------------------------------------------------------

## Construcción del árbol

### buildNodeMap()

Convierte todos los `Node` a `TreeNode` y genera:

``` java
Map<UUID, TreeNode>
```

Permitendo búsquedas O(1).

### buildInternalTree()

Reconstruye completamente la jerarquía.

Proceso:

1.  Crear todos los TreeNode.
2.  Guardarlos en un mapa.
3.  Recorrer nuevamente los Node.
4.  Conectar padre-hijo.
5.  Identificar la raíz.

Resultado:

``` text
Root
├── Child A
│   ├── Child A1
│   └── Child A2
└── Child B
```

------------------------------------------------------------------------

## Operaciones de creación

### createRoot()

Genera:

-   id del nodo
-   treeId del árbol
-   parentId = null

Representa el inicio de un árbol.

### addChild()

Genera:

-   nuevo id
-   parentId
-   treeId heredado

Representa la inserción de un hijo.

------------------------------------------------------------------------

## Construcción de vistas

### buildTree()

Flujo:

``` text
List<Node>
      ↓
buildInternalTree()
      ↓
TreeNode raíz
      ↓
toTreeView()
      ↓
TreeView
```

### buildSubTree()

Construye únicamente el subárbol de un nodo específico.

No devuelve ancestros.

------------------------------------------------------------------------

## DFS (Depth First Search)

Implementado mediante recursión.

Método auxiliar:

``` java
dfsRecursive()
```

Recorrido:

``` text
A
├── B
│   ├── D
│   └── E
└── C
```

Resultado:

``` text
A → B → D → E → C
```

### ¿Es recursivo?

Sí.

La recursión ocurre en:

``` java
dfsRecursive(child, result, originalMap);
```

------------------------------------------------------------------------

## BFS (Breadth First Search)

Implementación iterativa.

Utiliza:

``` java
Queue<TreeNode>
```

Resultado:

``` text
A → B → C → D
```

### ¿Es recursivo?

No.

Utiliza cola y ciclo while.

------------------------------------------------------------------------

## Path To Root

### getPathToRoot()

Obtiene:

``` text
Raíz → Padre → Nodo
```

Recorre la cadena de padres utilizando `parentId`.

------------------------------------------------------------------------

## Ancestors

### getAncestors()

Reutiliza:

``` java
getPathToRoot()
```

Elimina el último elemento.

Resultado:

``` text
Raíz → Padre
```

------------------------------------------------------------------------

## Height

### getHeight()

Utiliza el método auxiliar recursivo:

``` java
height(TreeNode node)
```

Algoritmo:

``` text
altura =
1 + altura máxima de los hijos
```

### ¿Es recursivo?

Sí.

------------------------------------------------------------------------

## Depth

### getDepth()

Calcula:

``` text
cantidad de ancestros
```

Internamente reutiliza:

``` java
getAncestors()
```

------------------------------------------------------------------------

## Validación de ciclos

### validateNoCycles()

Para cada nodo:

1.  Sigue la cadena de padres.
2.  Guarda visitados en un Set.
3.  Detecta repeticiones.

Ejemplo inválido:

``` text
A → B → C
↑       ↓
└───────┘
```

Resultado:

``` java
false
```

------------------------------------------------------------------------

## Métodos auxiliares

  Método                Función
  --------------------- -----------------------
  buildInternalTree()   Reconstruye el árbol
  buildNodeMap()        UUID -\> TreeNode
  nodeMap()             UUID -\> Node
  dfsRecursive()        DFS recursivo
  height()              Altura recursiva
  toTreeView()          Conversión a TreeView

------------------------------------------------------------------------

## Resumen

CustomTreeAlgorithmStrategy es una estrategia stateless que reconstruye
el árbol desde una lista plana de nodos del dominio (`Node`) cada vez
que ejecuta una operación. Utiliza recursión en DFS y cálculo de altura,
emplea métodos auxiliares para encapsular la construcción del árbol y
transforma estructuras de dominio hacia representaciones jerárquicas
(`TreeView`) para responder consultas y recorridos.
