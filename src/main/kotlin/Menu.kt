import java.io.InputStream
import java.io.PrintStream

class Menu(
    private val manager: DBManager,
    private val input: InputStream = System.`in`,
    private val output: PrintStream = System.out,
) {
    private val entities =
        listOf(
            "tour_types",
            "destinations",
            "tours",
            "tour_schedules",
            "clients",
            "bookings",
            "guides",
            "tour_guide_assignments",
            "transports",
            "tour_transports",
            "accommodations",
            "tour_accommodations",
            "services",
            "booking_services",
            "payments",
            "employees",
            "booking_employees",
            "reviews",
            "insurances",
            "booking_insurances",
        )

    private val scanner = input.bufferedReader()

    fun run() {
        while (true) {
            output.println()
            output.println("СИСТЕМА УЧЁТА ТУРОВ")
            output.println("1. Работа с сущностями")
            output.println("2. Сохранить")
            output.println("0. Выход")

            when (inputInt("Выбор: ")) {
                1 -> showEntityMenu()
                2 -> manager.save()
                0 -> {
                    val answer = inputStr("Сохранить перед выходом? (y/n): ")
                    if (answer.lowercase() == "y") {
                        manager.save()
                    }
                    return
                }
            }
        }
    }

    fun showEntityMenu() {
        val entityName = chooseEntity()

        while (true) {
            output.println()
            output.println("Меню сущности: $entityName")
            output.println("1. Показать записи")
            output.println("2. Удалить запись")
            output.println("3. Поиск")
            output.println("4. Сортировка")
            output.println("5. Агрегация")
            output.println("0. Назад")

            when (inputInt("Выбор: ")) {
                1 -> {
                    ConsoleHelper.printTable(manager.getList(entityName), entityName, output)
                }

                2 -> {
                    val id = inputInt("ID: ")
                    if (manager.delete(entityName, id)) {
                        output.println("Удалено.")
                    } else {
                        output.println("Запись не найдена.")
                    }
                }

                3 -> {
                    val field = inputStr("Поле: ")
                    val query = inputStr("Запрос: ")
                    val result = manager.search(entityName, field, query)
                    ConsoleHelper.printTable(result, entityName, output)
                }

                4 -> {
                    val field = inputStr("Поле сортировки: ")
                    val reverse = inputStr("По убыванию? (y/n): ").lowercase() == "y"
                    val result = manager.sort(entityName, field, reverse)
                    ConsoleHelper.printTable(result, entityName, output)
                }

                5 -> {
                    val field = inputStr("Поле: ")
                    val func = inputStr("Функция (sum/avg/min/max): ")
                    val result = manager.aggregate(entityName, field, func)
                    output.println("$func($field) = $result")
                }

                0 -> return
            }
        }
    }

    fun chooseEntity(): String {
        output.println()
        output.println("Выберите сущность:")
        entities.forEachIndexed { index, value ->
            output.println("${index + 1}. $value")
        }

        val idx = inputInt("Номер: ")
        return if (idx in 1..entities.size) {
            entities[idx - 1]
        } else {
            entities[0]
        }
    }

    fun inputInt(prompt: String): Int {
        while (true) {
            try {
                output.print(prompt)
                return scanner.readLine().toInt()
            } catch (e: Exception) {
                output.println("Введите целое число.")
            }
        }
    }

    fun inputDouble(prompt: String): Double {
        while (true) {
            try {
                output.print(prompt)
                return scanner.readLine().replace(',', '.').toDouble()
            } catch (e: Exception) {
                output.println("Введите число.")
            }
        }
    }

    fun inputStr(prompt: String): String {
        output.print(prompt)
        return scanner.readLine().trim()
    }

    fun getEntities(): List<String> = entities
}
