import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.full.memberProperties

@Serializable
data class TourType(var id: Int, var name: String, var description: String)

@Serializable
data class Destination(
    var id: Int,
    var country: String,
    var city: String,
    var description: String,
)

@Serializable
data class Tour(
    var id: Int,
    var tourTypeId: Int,
    var destinationId: Int,
    var title: String,
    var description: String,
    var basePrice: Double,
    var durationDays: Int,
    var status: String,
)

@Serializable
data class TourSchedule(
    var id: Int,
    var tourId: Int,
    var startDate: String,
    var endDate: String,
    var availableSeats: Int,
    var priceMultiplier: Double,
)

@Serializable
data class Client(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String,
    var passportNumber: String,
)

@Serializable
data class Booking(
    var id: Int,
    var tourScheduleId: Int,
    var clientId: Int,
    var bookingDate: String,
    var status: String,
    var totalPrice: Double,
    var discount: Double,
)

@Serializable
data class Guide(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var certification: String,
    var experienceYears: Int,
)

@Serializable
data class TourGuideAssignment(
    var id: Int,
    var tourScheduleId: Int,
    var guideId: Int,
    var role: String,
)

@Serializable
data class Transport(
    var id: Int,
    var type: String,
    var provider: String,
    var capacity: Int,
    var costPerSeat: Double,
)

@Serializable
data class TourTransport(
    var id: Int,
    var tourScheduleId: Int,
    var transportId: Int,
    var departurePoint: String,
    var arrivalPoint: String,
)

@Serializable
data class Accommodation(
    var id: Int,
    var name: String,
    var address: String,
    var rating: Double,
    var roomTypes: String,
)

@Serializable
data class TourAccommodation(
    var id: Int,
    var tourScheduleId: Int,
    var accommodationId: Int,
    var roomType: String,
    var nights: Int,
    var pricePerNight: Double,
)

@Serializable
data class Service(
    var id: Int,
    var name: String,
    var category: String,
    var basePrice: Double,
)

@Serializable
data class BookingService(
    var id: Int,
    var bookingId: Int,
    var serviceId: Int,
    var quantity: Int,
    var finalPrice: Double,
)

@Serializable
data class Payment(
    var id: Int,
    var bookingId: Int,
    var amount: Double,
    var paymentDate: String,
    var paymentMethod: String,
    var status: String,
)

@Serializable
data class Employee(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var position: String,
    var email: String,
    var hireDate: String,
)

@Serializable
data class BookingEmployee(
    var id: Int,
    var bookingId: Int,
    var employeeId: Int,
)

@Serializable
data class Review(
    var id: Int,
    var bookingId: Int,
    var rating: Int,
    var comment: String,
    var reviewDate: String,
)

@Serializable
data class Insurance(
    var id: Int,
    var name: String,
    var provider: String,
    var coverageAmount: Double,
    var price: Double,
)

@Serializable
data class BookingInsurance(
    var id: Int,
    var bookingId: Int,
    var insuranceId: Int,
    var finalPrice: Double,
)

@Serializable
data class TourDB(
    var tourTypes: MutableList<TourType> = mutableListOf(),
    var destinations: MutableList<Destination> = mutableListOf(),
    var tours: MutableList<Tour> = mutableListOf(),
    var tourSchedules: MutableList<TourSchedule> = mutableListOf(),
    var clients: MutableList<Client> = mutableListOf(),
    var bookings: MutableList<Booking> = mutableListOf(),
    var guides: MutableList<Guide> = mutableListOf(),
    var tourGuideAssignments: MutableList<TourGuideAssignment> = mutableListOf(),
    var transports: MutableList<Transport> = mutableListOf(),
    var tourTransports: MutableList<TourTransport> = mutableListOf(),
    var accommodations: MutableList<Accommodation> = mutableListOf(),
    var tourAccommodations: MutableList<TourAccommodation> = mutableListOf(),
    var services: MutableList<Service> = mutableListOf(),
    var bookingServices: MutableList<BookingService> = mutableListOf(),
    var payments: MutableList<Payment> = mutableListOf(),
    var employees: MutableList<Employee> = mutableListOf(),
    var bookingEmployees: MutableList<BookingEmployee> = mutableListOf(),
    var reviews: MutableList<Review> = mutableListOf(),
    var insurances: MutableList<Insurance> = mutableListOf(),
    var bookingInsurances: MutableList<BookingInsurance> = mutableListOf(),
)

class DBManager(private val filepath: String) {
    private val json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

    var db = TourDB()

    fun load(): Boolean {
        val file = File(filepath)

        if (!file.exists()) {
            return false
        }

        return try {
            db = json.decodeFromString(file.readText(Charsets.UTF_8))
            true
        } catch (e: Exception) {
            println("Ошибка загрузки БД: ${e.message}")
            db = TourDB()
            true
        }
    }

    fun save() {
        try {
            File(filepath).writeText(
                json.encodeToString(db),
                Charsets.UTF_8,
            )

            println("Данные сохранены.")
        } catch (e: Exception) {
            println("Ошибка сохранения: ${e.message}")
        }
    }

    fun getList(entityName: String): MutableList<Any> {
        return when (entityName) {
            "tour_types" -> db.tourTypes as MutableList<Any>
            "destinations" -> db.destinations as MutableList<Any>
            "tours" -> db.tours as MutableList<Any>
            "tour_schedules" -> db.tourSchedules as MutableList<Any>
            "clients" -> db.clients as MutableList<Any>
            "bookings" -> db.bookings as MutableList<Any>
            "guides" -> db.guides as MutableList<Any>
            "tour_guide_assignments" -> db.tourGuideAssignments as MutableList<Any>
            "transports" -> db.transports as MutableList<Any>
            "tour_transports" -> db.tourTransports as MutableList<Any>
            "accommodations" -> db.accommodations as MutableList<Any>
            "tour_accommodations" -> db.tourAccommodations as MutableList<Any>
            "services" -> db.services as MutableList<Any>
            "booking_services" -> db.bookingServices as MutableList<Any>
            "payments" -> db.payments as MutableList<Any>
            "employees" -> db.employees as MutableList<Any>
            "booking_employees" -> db.bookingEmployees as MutableList<Any>
            "reviews" -> db.reviews as MutableList<Any>
            "insurances" -> db.insurances as MutableList<Any>
            "booking_insurances" -> db.bookingInsurances as MutableList<Any>
            else -> mutableListOf()
        }
    }

    fun delete(
        entityName: String,
        id: Int,
    ): Boolean {
        val list = getList(entityName)

        val result =
            list.removeIf {
                val prop = it::class.memberProperties.firstOrNull { p -> p.name == "id" }
                prop?.getter?.call(it) == id
            }

        return result
    }

    fun search(
        entityName: String,
        field: String,
        query: String,
    ): List<Any> {
        return getList(entityName).filter {
            val prop =
                it::class.memberProperties.firstOrNull { p ->
                    p.name == field
                }

            prop?.getter?.call(it)?.toString()?.lowercase()?.contains(query.lowercase()) == true
        }
    }

    fun sort(
        entityName: String,
        field: String,
        reverse: Boolean = false,
    ): List<Any> {
        val sorted =
            getList(entityName).sortedBy {
                val prop =
                    it::class.memberProperties.firstOrNull { p ->
                        p.name == field
                    }

                prop?.getter?.call(it)?.toString()
            }

        return if (reverse) sorted.reversed() else sorted
    }

    fun aggregate(
        entityName: String,
        field: String,
        func: String,
    ): Double {
        val values =
            getList(entityName).mapNotNull {
                val prop =
                    it::class.memberProperties.firstOrNull { p ->
                        p.name == field
                    }

                val value = prop?.getter?.call(it)

                when (value) {
                    is Int -> value.toDouble()
                    is Double -> value
                    is Float -> value.toDouble()
                    else -> null
                }
            }

        if (values.isEmpty()) {
            return 0.0
        }

        return when (func) {
            "sum" -> values.sum()
            "avg" -> values.average()
            "min" -> values.min()
            "max" -> values.max()
            else -> 0.0
        }
    }
}

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

fun chooseEntity(): String {
    val entities =
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

    println()
    println("Выберите сущность:")

    entities.forEachIndexed { index, value ->
        println("${index + 1}. $value")
    }

    val idx = inputInt("Номер: ")

    return if (idx in 1..entities.size) {
        entities[idx - 1]
    } else {
        entities[0]
    }
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

fun generateTestData(filepath: String) {
    val db = TourDB()

    for (i in 1..10) {
        db.tourTypes.add(
            TourType(
                i,
                "Тип $i",
                "Описание типа $i",
            ),
        )

        db.destinations.add(
            Destination(
                i,
                listOf("Россия", "Турция", "Египет", "Таиланд")[i % 4],
                "Город $i",
                "Локация $i",
            ),
        )

        db.tours.add(
            Tour(
                i,
                i,
                i,
                "Тур $i",
                "Маршрут $i",
                15000.0 + i * 1000,
                3 + i % 7,
                "active",
            ),
        )

        db.tourSchedules.add(
            TourSchedule(
                i,
                i,
                "2024-07-${10 + i}",
                "2024-07-${12 + i}",
                20 - i,
                1.0 + i / 20.0,
            ),
        )

        db.clients.add(
            Client(
                i,
                "Клиент$i",
                "Петров$i",
                "client$i@mail.ru",
                "+79999999999",
                "1234 ${500000 + i}",
            ),
        )

        db.bookings.add(
            Booking(
                i,
                i,
                i,
                "2024-06-$i",
                "confirmed",
                20000.0 + i * 500,
                0.05 * i,
            ),
        )
    }

    val json =
        Json {
            prettyPrint = true
        }

    File(filepath).writeText(
        json.encodeToString(db),
        Charsets.UTF_8,
    )
}

fun entityMenu(
    manager: DBManager,
    entityName: String,
) {
    while (true) {
        println()
        println("Меню сущности: $entityName")
        println("1. Показать записи")
        println("2. Удалить запись")
        println("3. Поиск")
        println("4. Сортировка")
        println("5. Агрегация")
        println("0. Назад")

        when (inputInt("Выбор: ")) {
            1 -> {
                printTable(
                    manager.getList(entityName),
                    entityName,
                )
            }

            2 -> {
                val id = inputInt("ID: ")

                if (manager.delete(entityName, id)) {
                    println("Удалено.")
                } else {
                    println("Запись не найдена.")
                }
            }

            3 -> {
                val field = inputStr("Поле: ")
                val query = inputStr("Запрос: ")

                val result =
                    manager.search(
                        entityName,
                        field,
                        query,
                    )

                printTable(result, entityName)
            }

            4 -> {
                val field = inputStr("Поле сортировки: ")

                val reverse =
                    inputStr(
                        "По убыванию? (y/n): ",
                    ).lowercase() == "y"

                val result =
                    manager.sort(
                        entityName,
                        field,
                        reverse,
                    )

                printTable(result, entityName)
            }

            5 -> {
                val field = inputStr("Поле: ")

                val func =
                    inputStr(
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

fun main() {
    val filepath = "tours_db.json"

    val manager = DBManager(filepath)

    if (!manager.load()) {
        generateTestData(filepath)
        manager.load()
    }

    while (true) {
        println()
        println("СИСТЕМА УЧЁТА ТУРОВ")
        println("1. Работа с сущностями")
        println("2. Сохранить")
        println("0. Выход")

        when (inputInt("Выбор: ")) {
            1 -> {
                val entity = chooseEntity()

                entityMenu(
                    manager,
                    entity,
                )
            }

            2 -> manager.save()

            0 -> {
                val answer =
                    inputStr(
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
