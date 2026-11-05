# Algoritmos de ordenamiento y búsqueda

**SOFT-10 Estructura de Datos — Universidad Cenfotec**
Estrategia de aprendizaje · Docente: Romario Salas Cerdas

Este programa en Java permite ingresar una lista de palabras, ordenarlas con distintos algoritmos y después buscar una palabra dentro de la lista ordenada.

El funcionamiento es el siguiente:

1. El usuario indica cuántas palabras quiere ingresar y las escribe una por una. Estas se guardan en un arreglo estático.
2. Se muestra un menú para escoger el algoritmo de ordenamiento: selección, inserción, burbuja, mezcla o rápido.
3. Con el arreglo ya ordenado, se puede buscar cualquier palabra por medio de búsqueda binaria.

Cada algoritmo está hecho en su propio método, con comentarios que explican cada paso y su complejidad en notación O grande.

## Complejidad de cada algoritmo

| Algoritmo | Mejor caso | Promedio | Peor caso |
|---|---|---|---|
| Selección | O(n²) | O(n²) | O(n²) |
| Inserción | O(n) | O(n²) | O(n²) |
| Burbuja | O(n) | O(n²) | O(n²) |
| Mezcla | O(n log n) | O(n log n) | O(n log n) |
| Rápido | O(n log n) | O(n log n) | O(n²) |
| **Búsqueda binaria** | O(1) | O(log n) | O(log n) |

## Algoritmos de mezcla y rápido

Estos dos algoritmos no se vieron en el material del curso, así que se investigaron aparte. Ambos se basan en la idea de dividir el problema en partes más pequeñas.

- **Mezcla (Merge Sort):** divide el arreglo a la mitad varias veces hasta que quedan partes de una sola palabra. Después las va uniendo de nuevo, pero en orden. Su tiempo siempre es O(n log n), sin importar cómo vengan las palabras. Su desventaja es que necesita arreglos extra para hacer las uniones.
- **Rápido (Quicksort):** escoge una palabra como pivote y acomoda las demás, dejando las menores a la izquierda y las mayores a la derecha. Luego repite lo mismo con cada lado. Normalmente es O(n log n), pero si el pivote queda siempre en un extremo puede llegar a O(n²). En este programa se usa la palabra del medio como pivote para evitar ese caso cuando la lista ya viene ordenada.

## Búsqueda binaria

La búsqueda binaria no revisa las palabras una por una. Compara la palabra buscada con la del centro y, si no es esa, descarta la mitad donde no puede estar. Esto se repite hasta encontrarla o hasta que no queden palabras por revisar.

Como cada vez se descarta la mitad, su complejidad es O(log n). Por ejemplo, en una lista de un millón de palabras se necesitan unas 20 comparaciones. La condición para usarla es que la lista esté ordenada.

## Orden de las palabras

Las palabras se ordenan según el alfabeto en español. No se toman en cuenta las mayúsculas, y las palabras con tilde o con ñ quedan en el lugar correcto. Por ejemplo: `árbol, arena, Casa, Éxito, nube, ñandú, perro, zorro`.

## Cómo ejecutarlo

Se necesita Java 8 o una versión más reciente.

```bash
javac -encoding UTF-8 OrdenamientoBusqueda.java
java OrdenamientoBusqueda
```

## Ejemplo de uso

```
¿Cuántas palabras desea ingresar?: 4
  Palabra 1: perro
  Palabra 2: Casa
  Palabra 3: árbol
  Palabra 4: zorro

----------- MENÚ DE ORDENAMIENTO -----------
1. Selección   (O(n^2))
...
Escoja una opción: 4

Palabras ordenadas: [árbol, Casa, perro, zorro]

Escriba la palabra que desea buscar (o presione Enter para volver al menú): perro
  La palabra "perro" se encuentra en la posición 2 del arreglo ordenado.
```

Cada vez que se escoge un algoritmo, se ordena una copia de la lista original. De esta forma se pueden probar todos los algoritmos con las mismas palabras. La opción 6 del menú permite ingresar palabras nuevas.
