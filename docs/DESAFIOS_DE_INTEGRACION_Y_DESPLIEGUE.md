# Desafíos de Integración y Despliegue

## Introducción

Durante las etapas finales del proyecto surgieron desafíos relacionados con la integración entre el backend y el frontend, así como con la necesidad de exponer servicios locales hacia entornos externos para realizar pruebas de integración.

---

# 1. Integración del frontend generado mediante IA

## Situación inicial

Con el objetivo de acelerar el desarrollo de la interfaz gráfica se evaluaron herramientas de generación automática de frontend basadas en inteligencia artificial.

Entre las plataformas exploradas se encontraban Bolt y Lovable.

## Problema detectado

Las primeras versiones generadas presentaban varias limitaciones:

* La estructura visual no coincidía con los requerimientos del proyecto.
* Las once operaciones de la API no estaban representadas adecuadamente.
* La navegación generada era insuficiente para exponer todas las funcionalidades disponibles.
* Los prompts iniciales no proporcionaban suficiente contexto a la IA.

Adicionalmente, el tiempo disponible para iterar era reducido debido a la carga académica y a otros proyectos paralelos.

## Solución implementada

Se realizaron múltiples refinamientos sobre los prompts utilizados para generar la interfaz.

Cada iteración incorporó mayor detalle acerca de:

* Operaciones disponibles.
* Endpoints de la API.
* Estructura esperada de navegación.
* Flujo de interacción entre usuario y sistema.

## Resultado

Se obtuvo una interfaz funcional para realizar pruebas de integración, aunque con algunas limitaciones respecto a la versión inicialmente planeada.

---

# 2. Exposición pública del backend mediante Ngrok

## Situación inicial

Las plataformas utilizadas para generar el frontend se ejecutaban en entornos remotos y no podían acceder directamente a un backend ejecutándose localmente en:

http://localhost:8080

## Problema detectado

El frontend no podía comunicarse con la API debido a que el backend únicamente existía dentro de la red local del desarrollador.

## Solución implementada

Se incorporó Ngrok para publicar temporalmente el backend mediante una URL accesible desde Internet.

El flujo utilizado fue:

Backend Spring Boot → localhost:8080 → Ngrok → URL pública → Frontend remoto

## Resultado

El frontend pudo consumir los servicios REST sin necesidad de desplegar la aplicación en infraestructura adicional.

---

# 3. Pantalla de advertencia de Ngrok

## Problema detectado

Tras publicar el backend mediante Ngrok, las solicitudes continuaban fallando.

Durante la investigación se observó que Ngrok mostraba una pantalla de advertencia previa antes de permitir el acceso al backend.

El frontend interpretaba dicha página como si fuera la respuesta del servidor.

Tambien swagger enciende y todo pero no se puede probar por la pantalla de advertecia de ngrok. ya que no puedes modificar los headers de swagger, bueno si se investiga supongo que se podria, pero no nos hace falta swagger para nuestro forntend.

## Solución implementada

Se investigó el comportamiento de Ngrok y se identificó la necesidad de enviar encabezados HTTP específicos para evitar la aparición de dicha pantalla.

Por esta razón se incorporó un filtro HTTP encargado de agregar automáticamente los encabezados requeridos.

## Resultado

Las solicitudes comenzaron a llegar correctamente a los endpoints de la API.

---

# 4. Restricciones CORS durante la integración

## Problema detectado

Una vez resuelto el problema de la advertencia de Ngrok, continuaban apareciendo errores de comunicación entre frontend y backend.

La revisión de los registros de Spring Boot permitió identificar restricciones asociadas a CORS.

## Solución implementada

Se incorporó una configuración específica para permitir solicitudes provenientes de los dominios utilizados durante las pruebas de integración.

## Resultado

La comunicación entre frontend y backend pudo establecerse correctamente.

---

# 5. Limitaciones encontradas durante la generación automática del frontend

## Problema detectado

Las herramientas de generación automática presentaban varias limitaciones prácticas:

* Créditos gratuitos limitados.
* Cantidad reducida de iteraciones posibles.
* Resultados inconsistentes entre ejecuciones.
* Dificultad para obtener exactamente la estructura visual deseada.

Cada modificación importante requería nuevas iteraciones y consumía recursos adicionales.

## Resultado

La experiencia demostró que las herramientas de IA pueden acelerar significativamente el desarrollo inicial, pero siguen requiriendo supervisión humana, refinamiento de prompts y validaciones continuas para obtener resultados alineados con los requisitos del proyecto.
