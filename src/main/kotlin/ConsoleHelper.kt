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
    ) {
        if (list.isEmpty()) {
            println("Записи отсутствуют.")
            return
        }

        println()
        println("Сущность: $entityName")
        println("Количество записей: ${list.size}")
        println()

        list.take(5).forEach {
            println(it)
        }

        if (list.size > 5) {
            println("... ещё ${list.size - 5} записей")
        }
    }
}
