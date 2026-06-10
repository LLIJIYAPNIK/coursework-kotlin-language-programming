import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.full.memberProperties

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
