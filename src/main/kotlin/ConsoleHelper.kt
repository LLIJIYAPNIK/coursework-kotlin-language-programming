import java.io.PrintStream

object ConsoleHelper {
    fun inputInt(prompt: String): Int {
        while (true) {
            try {
                print(prompt)
                return readln().toInt()
            } catch (e: Exception) {
                println("Введите целое число.")
            }
        }
    }

    fun inputDouble(prompt: String): Double {
        while (true) {
            try {
                print(prompt)
                return readln().replace(',', '.').toDouble()
            } catch (e: Exception) {
                println("Введите число.")
            }
        }
    }

    fun inputStr(prompt: String): String {
        print(prompt)
        return readln().trim()
    }

    fun printTable(
        list: List<Any>,
        entityName: String,
        output: PrintStream = System.out,
    ) {
        if (list.isEmpty()) {
            output.println("Записи отсутствуют.")
            return
        }

        output.println()
        output.println("Сущность: $entityName")
        output.println("Количество записей: ${list.size}")
        output.println()

        list.take(5).forEach {
            output.println(it)
        }

        if (list.size > 5) {
            output.println("... ещё ${list.size - 5} записей")
        }
    }
}
