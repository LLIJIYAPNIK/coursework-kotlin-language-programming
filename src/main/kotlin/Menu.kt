class Menu(private val manager: DBManager) {
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

    fun run() {
        while (true) {
            println()
            println("СИСТЕМА УЧЁТА ТУРОВ")
            println("1. Работа с сущностями")
            println("2. Сохранить")
            println("0. Выход")

            when (ConsoleHelper.inputInt("Выбор: ")) {
                1 -> showEntityMenu()
                2 -> manager.save()
                0 -> {
                    val answer =
                        ConsoleHelper.inputStr(
                            "Сохранить перед выходом? (y/n): ",
                        )

                    if (answer.lowercase() == "y") {
                        manager.save()
                    }

                    return
                }
            }
        }
    }

    private fun showEntityMenu() {
        val entityName = chooseEntity()

        while (true) {
            println()
            println("Меню сущности: $entityName")
            println("1. Показать записи")
            println("2. Удалить запись")
            println("3. Поиск")
            println("4. Сортировка")
            println("5. Агрегация")
            println("0. Назад")

            when (ConsoleHelper.inputInt("Выбор: ")) {
                1 -> {
                    ConsoleHelper.printTable(
                        manager.getList(entityName),
                        entityName,
                    )
                }

                2 -> {
                    val id = ConsoleHelper.inputInt("ID: ")

                    if (manager.delete(entityName, id)) {
                        println("Удалено.")
                    } else {
                        println("Запись не найдена.")
                    }
                }

                3 -> {
                    val field = ConsoleHelper.inputStr("Поле: ")
                    val query = ConsoleHelper.inputStr("Запрос: ")

                    val result =
                        manager.search(
                            entityName,
                            field,
                            query,
                        )

                    ConsoleHelper.printTable(result, entityName)
                }

                4 -> {
                    val field = ConsoleHelper.inputStr("Поле сортировки: ")

                    val reverse =
                        ConsoleHelper.inputStr(
                            "По убыванию? (y/n): ",
                        ).lowercase() == "y"

                    val result =
                        manager.sort(
                            entityName,
                            field,
                            reverse,
                        )

                    ConsoleHelper.printTable(result, entityName)
                }

                5 -> {
                    val field = ConsoleHelper.inputStr("Поле: ")

                    val func =
                        ConsoleHelper.inputStr(
                            "Функция (sum/avg/min/max): ",
                        )

                    val result =
                        manager.aggregate(
                            entityName,
                            field,
                            func,
                        )

                    println("$func($field) = $result")
                }

                0 -> return
            }
        }
    }

    private fun chooseEntity(): String {
        println()
        println("Выберите сущность:")

        entities.forEachIndexed { index, value ->
            println("${index + 1}. $value")
        }

        val idx = ConsoleHelper.inputInt("Номер: ")

        return if (idx in 1..entities.size) {
            entities[idx - 1]
        } else {
            entities[0]
        }
    }
}
