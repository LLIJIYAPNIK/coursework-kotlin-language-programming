import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object TestDataGenerator {
    fun generate(filepath: String) {
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
}
