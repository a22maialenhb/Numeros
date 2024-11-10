import kotlin.random.Random
import java.io.File

// Colores para la consola
val RESET = "\u001B[0m"
val RED = "\u001B[31m"
val GREEN = "\u001B[38;2;78;245;66m"
val YELLOW = "\u001B[38;2;245;236;66m"
val BLUE = "\u001B[38;2;0;217;255m"
val ORANGE = "\u001B[38;2;255;147;38m"
val LIGHT_PINK = "\u001B[38;2;255;133;220m"
val DARK_GREEN = "\u001B[38;2;4;181;54m"

var nintentos = 10
var contador = 0
var cifras = 4

// Función para generar el número aleatorio
fun numAlt(): String {
    val digitos = mutableSetOf<Int>()
    while (digitos.size < cifras) {
        val nuevoDigito = Random.nextInt(1, 7)
        digitos.add(nuevoDigito)
    }
    return digitos.joinToString("")
}

// Función para comparar el número ingresado con el número secreto
fun compararNumeros(numero1: String, numero2: String): Boolean {
    var aciertos = 0
    var coincidencias = 0
    contador++

    // Contar aciertos (dígitos correctos en la misma posición)
    for (i in 0..3) {
        if (numero1[i] == numero2[i]) aciertos++
    }

    // Contar coincidencias (dígitos correctos pero en diferente posición)
    for (i in 0..3) {
        if (numero1[i] in numero2) coincidencias++
    }

    // Mostrar el resultado con colores
    val resultado = "Aciertos : ${GREEN} $aciertos ${RESET}| Coincidencias : ${YELLOW} ${coincidencias - aciertos}${RESET}"
    println("$resultado \n")
    File("ultima_jugada.txt").appendText("Intento $contador:${BLUE} $numero1${RESET} | $resultado \n")

    if (aciertos == cifras) {
        println("${GREEN}¡Enhorabuena! Has adivinado el número secreto correctamente.${RESET} \n")
        return true
    }
    return false
}

// Validar el número ingresado por el usuario
fun comprobar(numero: String, solucion: String): Boolean {
    if (numero.length != cifras) {
        println("${RED}El número tiene que tener exactamente 4 cifras.\n ${RESET}")
        return false
    }

    // Verificar que los dígitos sean entre 1 y 6
    for (i in numero) {
        if (i - '0' > 6) {
            println("${RED}Las cifras del número deben ser entre 1 y 6.${RESET} \n")
            return false
        }
    }

    // Validar que no haya dígitos repetidos
    val digitosUnicos = numero.toSet()
    if (digitosUnicos.size < 4) {
        println("${RED}Los números no deben tener dígitos repetidos.${RESET}\n")
        return false
    }

    return compararNumeros(numero, solucion)
}

// Función principal del juego
fun jugar() {
    contador = 0  // Resetear contador
    println("${LIGHT_PINK}Escribe un número del 1 al 6 (ambos incluidos). Y que sea de 4 cifras únicamente.${RESET} \n")
    println("${LIGHT_PINK}Los aciertos son los números en la posición correcta con la cifra correcta.${RESET}")
    println("${LIGHT_PINK}Las coincidencias son los números que se encuentran dentro del número secreto.${RESET} \n")
    println("Teclea un número de 4 cifras no repetidas con dígitos del 1 al 6: \n ")

    val numeroAleatorio = numAlt()  // Generar el número secreto
    File("ultima_jugada.txt").writeText("${ORANGE}Jugada completa: \n${RESET}")

    // Ciclo de intentos
    while (contador < nintentos) {
        print("${BLUE}Intento ${contador + 1}:${RESET} ")
        val numero = readln()
        val comprobacion = comprobar(numero, numeroAleatorio)

        if (comprobacion) {
            return
        }
    }

    // Si se agotaron los intentos
    println("Lo siento, no adivinaste el número secreto $numeroAleatorio en $nintentos intentos.\n")
}

// Mostrar la última jugada registrada
fun ultimajugada() {
    val archivo = File("ultima_jugada.txt")
    if (archivo.exists()) {
        val contenido = archivo.readText()
        println("$contenido ")
    } else {
        println("No hay jugadas anteriores registradas.\n ")
    }
}

// Menú principal del programa
fun main() {
    while (true) {
        println("${DARK_GREEN}1. Jugar${RESET}")
        println("${DARK_GREEN}2. Ver traza de ultimo intento${RESET}")
        println("${DARK_GREEN}3. Salir${RESET}")
        print("${DARK_GREEN}Opción: ${RESET}")
        val entrada = readln().toInt()
        println()


        when (entrada) {
            1 -> jugar()
            2 -> ultimajugada()
            3 -> {
                println("Gracias por jugar")
                File("ultima_jugada.txt").delete()  // Eliminar archivo de la última jugada
                break
            }
            else -> println(" ${RED} *Número incorrecto* \n ${RESET}")
        }
    }
}
