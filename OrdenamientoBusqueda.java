import java.text.Collator;
import java.util.Locale;
import java.util.Scanner;

/**
 * SOFT-10 Estructura de Datos - Universidad Cenfotec
 * Estrategia de aprendizaje: Algoritmos de ordenamiento y búsqueda
 *
 * Este programa permite:
 *   1. Llenar un arreglo estático de palabras con el tamaño que indique el usuario.
 *   2. Ordenar el arreglo con el algoritmo que se elija: selección, inserción,
 *      burbuja, mezcla o rápido.
 *   3. Buscar palabras dentro del arreglo ordenado usando búsqueda binaria.
 *
 * Para comparar las palabras se usa el método comparar(), que ordena según el
 * alfabeto en español. Esto hace que no importe si una palabra está en mayúscula
 * o minúscula, y que las palabras con tilde o ñ queden en el lugar correcto.
 * Se usa la misma forma de comparar al ordenar y al buscar, ya que la búsqueda
 * binaria solo funciona si el arreglo se ordenó con ese mismo criterio.
 */
public class OrdenamientoBusqueda {

    private static final Scanner entrada = new Scanner(System.in);

    // Comparador para ordenar palabras en español. Con SECONDARY no se toman en
    // cuenta las mayúsculas, pero sí las tildes ("papa" y "papá" son distintas).
    private static final Collator comparador = Collator.getInstance(Locale.forLanguageTag("es-CR"));
    static {
        comparador.setStrength(Collator.SECONDARY);
    }

    // =====================================================================
    //  PROGRAMA PRINCIPAL
    // =====================================================================
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  ALGORITMOS DE ORDENAMIENTO Y BÚSQUEDA");
        System.out.println("==============================================");

        // Se le pide al usuario el tamaño del arreglo y luego se crea.
        int tamano = leerEntero("¿Cuántas palabras desea ingresar?: ", 1, Integer.MAX_VALUE);
        String[] palabras = new String[tamano];

        // Se llena el arreglo con las palabras del usuario.
        llenarArreglo(palabras);

        // Menú para escoger el algoritmo de ordenamiento.
        boolean salir = false;
        while (!salir) {
            System.out.println("\nPalabras ingresadas: " + arregloATexto(palabras));
            System.out.println("\n----------- MENÚ DE ORDENAMIENTO -----------");
            System.out.println("1. Selección   (O(n^2))");
            System.out.println("2. Inserción   (O(n^2))");
            System.out.println("3. Burbuja     (O(n^2))");
            System.out.println("4. Mezcla      (O(n log n))");
            System.out.println("5. Rápido      (O(n log n) en promedio, O(n^2) en el peor caso)");
            System.out.println("6. Ingresar palabras nuevas");
            System.out.println("0. Salir");
            int opcion = leerEntero("Escoja una opción: ", 0, 6);

            // Se ordena una copia y no el arreglo original. Así se pueden probar
            // varios algoritmos con las mismas palabras en el orden en que se ingresaron.
            String[] copia = copiarArreglo(palabras);

            switch (opcion) {
                case 1: ordenamientoSeleccion(copia); break;
                case 2: ordenamientoInsercion(copia); break;
                case 3: ordenamientoBurbuja(copia); break;
                case 4: ordenamientoMezcla(copia, 0, copia.length - 1); break;
                case 5: ordenamientoRapido(copia, 0, copia.length - 1); break;
                case 6:
                    llenarArreglo(palabras);
                    continue;
                case 0:
                    salir = true;
                    continue;
            }

            System.out.println("\nPalabras ordenadas: " + arregloATexto(copia));

            // Una vez ordenado el arreglo, se puede buscar una palabra.
            menuBusqueda(copia);
        }

        System.out.println("\nPrograma finalizado.");
        entrada.close();
    }

    // =====================================================================
    //  1. ORDENAMIENTO POR SELECCIÓN
    //  Complejidad: O(n^2) en todos los casos.
    //  En cada vuelta se busca la palabra menor de la parte que falta por
    //  ordenar y se pasa al inicio. Siempre revisa todas las palabras que
    //  faltan, aunque el arreglo ya venga ordenado, por eso nunca baja de O(n^2).
    // =====================================================================
    public static void ordenamientoSeleccion(String[] arreglo) {
        int n = arreglo.length;

        // Paso 1: se recorre cada posición del arreglo. Al terminar cada vuelta,
        // la posición i ya tiene la palabra que le corresponde.
        for (int i = 0; i < n - 1; i++) {

            // Paso 2: se supone que la palabra menor es la que está en la posición i.
            int indiceMinimo = i;

            // Paso 3: se revisa el resto del arreglo para ver si hay una palabra menor.
            for (int j = i + 1; j < n; j++) {
                if (comparar(arreglo[j], arreglo[indiceMinimo]) < 0) {
                    indiceMinimo = j;
                }
            }

            // Paso 4: si se encontró una palabra menor, se cambia de lugar con la de la posición i.
            if (indiceMinimo != i) {
                intercambiar(arreglo, i, indiceMinimo);
            }
        }
    }

    // =====================================================================
    //  2. ORDENAMIENTO POR INSERCIÓN
    //  Complejidad: O(n^2) en el peor caso y en promedio, O(n) en el mejor caso.
    //  Funciona parecido a cuando se ordenan cartas en la mano: se toma una
    //  palabra y se mete en el lugar que le toca entre las que ya están ordenadas.
    //  Si el arreglo ya viene ordenado, cada palabra se compara una sola vez,
    //  por eso el mejor caso es O(n).
    // =====================================================================
    public static void ordenamientoInsercion(String[] arreglo) {
        int n = arreglo.length;

        // Paso 1: la primera palabra se toma como ya ordenada,
        // así que se empieza desde la segunda.
        for (int i = 1; i < n; i++) {

            // Paso 2: se guarda la palabra que se va a acomodar.
            String clave = arreglo[i];
            int j = i - 1;

            // Paso 3: las palabras mayores que la clave se corren un espacio
            // a la derecha para dejar un campo libre.
            while (j >= 0 && comparar(arreglo[j], clave) > 0) {
                arreglo[j + 1] = arreglo[j];
                j--;
            }

            // Paso 4: se coloca la palabra en el campo que quedó libre.
            arreglo[j + 1] = clave;
        }
    }

    // =====================================================================
    //  3. ORDENAMIENTO BURBUJA
    //  Complejidad: O(n^2) en el peor caso y en promedio, O(n) en el mejor caso.
    //  Se comparan las palabras de dos en dos y se cambian si están en el orden
    //  equivocado. En cada pasada la palabra mayor va subiendo hasta el final,
    //  como una burbuja. Si en una pasada no hubo cambios, el arreglo ya está
    //  ordenado y se detiene, por eso el mejor caso es O(n).
    // =====================================================================
    public static void ordenamientoBurbuja(String[] arreglo) {
        int n = arreglo.length;

        // Paso 1: se hacen varias pasadas por el arreglo.
        for (int pasada = 0; pasada < n - 1; pasada++) {
            boolean huboIntercambio = false;

            // Paso 2: se comparan las palabras que están una al lado de la otra.
            // Las últimas posiciones ya quedaron ordenadas en pasadas anteriores,
            // por eso ya no se revisan.
            for (int j = 0; j < n - 1 - pasada; j++) {

                // Paso 3: si están en el orden equivocado, se cambian de lugar.
                if (comparar(arreglo[j], arreglo[j + 1]) > 0) {
                    intercambiar(arreglo, j, j + 1);
                    huboIntercambio = true;
                }
            }

            // Paso 4: si no se hizo ningún cambio, ya está ordenado y se termina.
            if (!huboIntercambio) {
                break;
            }
        }
    }

    // =====================================================================
    //  4. ORDENAMIENTO POR MEZCLA (MERGE SORT)
    //  Complejidad: O(n log n) en todos los casos.
    //  La idea es dividir el arreglo a la mitad una y otra vez hasta que queden
    //  partes de una sola palabra, y después unirlas de nuevo en orden.
    //  El arreglo se puede partir a la mitad unas log n veces, y en cada nivel
    //  se revisan las n palabras, por eso queda O(n log n).
    //  Su desventaja es que necesita arreglos extra para hacer las uniones.
    // =====================================================================
    public static void ordenamientoMezcla(String[] arreglo, int inicio, int fin) {
        // Si la parte tiene una palabra o ninguna, ya está ordenada.
        if (inicio >= fin) {
            return;
        }

        // Paso 1 (dividir): se busca la mitad de la parte actual.
        int medio = inicio + (fin - inicio) / 2;

        // Paso 2: se ordena cada mitad por separado, llamando al mismo método.
        ordenamientoMezcla(arreglo, inicio, medio);
        ordenamientoMezcla(arreglo, medio + 1, fin);

        // Paso 3 (unir): se juntan las dos mitades ya ordenadas.
        mezclar(arreglo, inicio, medio, fin);
    }

    /**
     * Método de apoyo para la mezcla: junta dos partes que ya están ordenadas
     * (de inicio a medio y de medio+1 a fin) en una sola parte ordenada.
     * Complejidad: O(n), porque pasa una vez por cada palabra.
     */
    private static void mezclar(String[] arreglo, int inicio, int medio, int fin) {
        // Paso 1: se copia cada mitad en un arreglo temporal.
        int tamIzq = medio - inicio + 1;
        int tamDer = fin - medio;
        String[] izquierda = new String[tamIzq];
        String[] derecha = new String[tamDer];

        for (int i = 0; i < tamIzq; i++) izquierda[i] = arreglo[inicio + i];
        for (int j = 0; j < tamDer; j++) derecha[j] = arreglo[medio + 1 + j];

        // Paso 2: se comparan las palabras de ambas mitades y se va pasando
        // al arreglo original la que sea menor.
        int i = 0, j = 0, k = inicio;
        while (i < tamIzq && j < tamDer) {
            if (comparar(izquierda[i], derecha[j]) <= 0) {
                arreglo[k++] = izquierda[i++];
            } else {
                arreglo[k++] = derecha[j++];
            }
        }

        // Paso 3: si en alguna mitad sobraron palabras, se copian al final
        // porque ya están en orden.
        while (i < tamIzq) arreglo[k++] = izquierda[i++];
        while (j < tamDer) arreglo[k++] = derecha[j++];
    }

    // =====================================================================
    //  5. ORDENAMIENTO RÁPIDO (QUICKSORT)
    //  Complejidad: O(n log n) en promedio y O(n^2) en el peor caso.
    //  Se escoge una palabra como pivote y se acomodan las demás: las menores
    //  a la izquierda y las mayores a la derecha. Luego se hace lo mismo con
    //  cada lado. Cuando el pivote divide el arreglo en partes parecidas, el
    //  tiempo es O(n log n). Si el pivote siempre queda en un extremo, las
    //  partes quedan muy desiguales y el tiempo sube a O(n^2).
    //  A diferencia de la mezcla, no necesita arreglos extra.
    // =====================================================================
    public static void ordenamientoRapido(String[] arreglo, int inicio, int fin) {
        // Solo se ordena si la parte tiene más de una palabra.
        if (inicio < fin) {

            // Paso 1: se acomodan las palabras alrededor del pivote.
            // Al terminar, el pivote ya queda en su posición final.
            int indicePivote = particion(arreglo, inicio, fin);

            // Paso 2: se ordena el lado izquierdo del pivote.
            ordenamientoRapido(arreglo, inicio, indicePivote - 1);

            // Paso 3: se ordena el lado derecho del pivote.
            ordenamientoRapido(arreglo, indicePivote + 1, fin);
        }
    }

    /**
     * Método de apoyo para el ordenamiento rápido: acomoda las palabras
     * menores al pivote a la izquierda y las mayores a la derecha.
     * Complejidad: O(n), porque recorre la parte una sola vez.
     */
    private static int particion(String[] arreglo, int inicio, int fin) {
        // Paso 1: se toma como pivote la palabra del medio y se pasa al final.
        // Se escoge la del medio para evitar el peor caso cuando el arreglo
        // ya viene ordenado.
        int medio = inicio + (fin - inicio) / 2;
        intercambiar(arreglo, medio, fin);
        String pivote = arreglo[fin];

        // Paso 2: "i" indica hasta dónde llegan las palabras menores al pivote.
        int i = inicio - 1;

        // Paso 3: se recorre la parte y cada palabra menor o igual al pivote
        // se pasa al lado izquierdo.
        for (int j = inicio; j < fin; j++) {
            if (comparar(arreglo[j], pivote) <= 0) {
                i++;
                intercambiar(arreglo, i, j);
            }
        }

        // Paso 4: se pone el pivote justo después de las palabras menores,
        // que es el lugar que le corresponde, y se devuelve esa posición.
        intercambiar(arreglo, i + 1, fin);
        return i + 1;
    }

    // =====================================================================
    //  BÚSQUEDA BINARIA
    //  Complejidad: O(log n).
    //  En vez de revisar las palabras una por una, se compara la palabra
    //  buscada con la que está en el centro. Si no es esa, se descarta la
    //  mitad donde no puede estar y se sigue buscando en la otra. Como cada
    //  vez se descarta la mitad, se necesitan muy pocas comparaciones: por
    //  ejemplo, con 1 000 000 de palabras bastan unas 20.
    //  Para que funcione, el arreglo tiene que estar ordenado.
    //  Devuelve la posición de la palabra o -1 si no la encuentra.
    // =====================================================================
    public static int busquedaBinaria(String[] arreglo, String buscada) {
        // Paso 1: al inicio se busca en todo el arreglo.
        int izquierda = 0;
        int derecha = arreglo.length - 1;

        // Paso 2: se repite mientras quede al menos una palabra por revisar.
        while (izquierda <= derecha) {

            // Paso 3: se busca la posición del centro.
            int medio = izquierda + (derecha - izquierda) / 2;
            int comparacion = comparar(buscada, arreglo[medio]);

            if (comparacion == 0) {
                // Paso 4a: la palabra del centro es la que se busca.
                return medio;
            } else if (comparacion < 0) {
                // Paso 4b: la palabra va antes en el alfabeto,
                // entonces se sigue buscando en la mitad izquierda.
                derecha = medio - 1;
            } else {
                // Paso 4c: la palabra va después en el alfabeto,
                // entonces se sigue buscando en la mitad derecha.
                izquierda = medio + 1;
            }
        }

        // Paso 5: ya no quedan palabras por revisar, así que no está en el arreglo.
        return -1;
    }

    // =====================================================================
    //  MÉTODOS DE APOYO
    // =====================================================================

    /** Le pide al usuario cada palabra y la guarda en el arreglo. */
    private static void llenarArreglo(String[] arreglo) {
        System.out.println("\nIngrese " + arreglo.length + " palabra(s):");
        for (int i = 0; i < arreglo.length; i++) {
            String palabra;
            do {
                System.out.print("  Palabra " + (i + 1) + ": ");
                palabra = entrada.nextLine().trim();
                if (palabra.isEmpty()) {
                    System.out.println("  Debe escribir una palabra.");
                }
            } while (palabra.isEmpty());
            arreglo[i] = palabra;
        }
    }

    /** Permite buscar una o varias palabras en el arreglo ordenado. */
    private static void menuBusqueda(String[] arregloOrdenado) {
        while (true) {
            System.out.print("\nEscriba la palabra que desea buscar (o presione Enter para volver al menú): ");
            String buscada = entrada.nextLine().trim();
            if (buscada.isEmpty()) {
                return;
            }

            int posicion = busquedaBinaria(arregloOrdenado, buscada);
            if (posicion >= 0) {
                System.out.println("  La palabra \"" + buscada + "\" se encuentra en la posición "
                        + posicion + " del arreglo ordenado.");
            } else {
                System.out.println("  La palabra \"" + buscada + "\" no se encuentra en el arreglo.");
            }
        }
    }

    /** Lee un número entero y revisa que esté dentro del rango permitido. */
    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Si no es un número, se muestra el mensaje de abajo.
            }
            System.out.println("  El valor ingresado no es válido, intente de nuevo.");
        }
    }

    /**
     * Compara dos palabras según el alfabeto en español.
     * Devuelve un número negativo si a va antes que b, 0 si son iguales
     * y un número positivo si a va después de b.
     */
    private static int comparar(String a, String b) {
        return comparador.compare(a, b);
    }

    /** Cambia de lugar dos palabras del arreglo. */
    private static void intercambiar(String[] arreglo, int a, int b) {
        String temporal = arreglo[a];
        arreglo[a] = arreglo[b];
        arreglo[b] = temporal;
    }

    /** Crea una copia del arreglo. */
    private static String[] copiarArreglo(String[] original) {
        String[] copia = new String[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i];
        }
        return copia;
    }

    /** Devuelve el arreglo como texto, por ejemplo: [casa, perro, sol]. */
    private static String arregloATexto(String[] arreglo) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arreglo.length; i++) {
            sb.append(arreglo[i]);
            if (i < arreglo.length - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
