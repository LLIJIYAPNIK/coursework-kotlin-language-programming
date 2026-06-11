import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class IntegrationTest {
    private val testFile = "test_integration.json"
    private lateinit var manager: DBManager

    @BeforeEach
    fun setup() {
        manager = DBManager(testFile)
    }

    @AfterEach
    fun cleanup() {
        val file = File(testFile)
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun testFullWorkflow() {
        TestDataGenerator.generate(testFile)

        assertTrue(manager.load())

        assertEquals(10, manager.db.tours.size)

        val searchResult = manager.search("tours", "title", "Тур 1")
        assertTrue(searchResult.isNotEmpty())

        val sorted = manager.sort("tours", "basePrice")
        assertTrue(sorted.isNotEmpty())

        val sum = manager.aggregate("tours", "basePrice", "sum")
        assertEquals(205000.0, sum)

        assertTrue(manager.delete("tours", 1))
        assertEquals(9, manager.db.tours.size)

        manager.save()

        val newManager = DBManager(testFile)
        assertTrue(newManager.load())
        assertEquals(9, newManager.db.tours.size)
    }

    @Test
    fun testEmptyDatabaseWorkflow() {
        val db = TourDB()
        manager.db = db
        manager.save()

        assertTrue(manager.load())
        assertEquals(0, manager.db.tours.size)

        manager.db.tours.add(Tour(1, 1, 1, "Тест", "Описание", 10000.0, 7, "active"))
        manager.save()

        val newManager = DBManager(testFile)
        assertTrue(newManager.load())
        assertEquals(1, newManager.db.tours.size)
    }

    @Test
    fun testMultipleEntitiesWorkflow() {
        TestDataGenerator.generate(testFile)
        manager.load()

        val tours = manager.getList("tours")
        val clients = manager.getList("clients")
        val bookings = manager.getList("bookings")

        assertEquals(10, tours.size)
        assertEquals(10, clients.size)
        assertEquals(10, bookings.size)

        manager.delete("tours", 1)
        manager.delete("clients", 1)
        manager.delete("bookings", 1)

        assertEquals(9, manager.getList("tours").size)
        assertEquals(9, manager.getList("clients").size)
        assertEquals(9, manager.getList("bookings").size)
    }

    @Test
    fun testSearchAcrossDifferentEntities() {
        TestDataGenerator.generate(testFile)
        manager.load()

        val tourResult = manager.search("tours", "title", "Тур")
        val clientResult = manager.search("clients", "firstName", "Клиент")
        val destResult = manager.search("destinations", "country", "Россия")

        assertTrue(tourResult.isNotEmpty())
        assertTrue(clientResult.isNotEmpty())
        assertTrue(destResult.isNotEmpty())
    }

    @Test
    fun testAggregateAcrossDifferentFields() {
        TestDataGenerator.generate(testFile)
        manager.load()

        val priceSum = manager.aggregate("tours", "basePrice", "sum")
        val durationAvg = manager.aggregate("tours", "durationDays", "avg")
        val seatsMin = manager.aggregate("tour_schedules", "availableSeats", "min")
        val multiplierMax = manager.aggregate("tour_schedules", "priceMultiplier", "max")

        assertTrue(priceSum > 0)
        assertTrue(durationAvg > 0)
        assertTrue(seatsMin > 0)
        assertTrue(multiplierMax > 0)
    }

    @Test
    fun testSortAcrossDifferentFields() {
        TestDataGenerator.generate(testFile)
        manager.load()

        val byPrice = manager.sort("tours", "basePrice")
        val byDuration = manager.sort("tours", "durationDays")
        val byTitle = manager.sort("tours", "title")

        assertTrue(byPrice.isNotEmpty())
        assertTrue(byDuration.isNotEmpty())
        assertTrue(byTitle.isNotEmpty())
    }

    @Test
    fun testDataConsistencyAfterSaveLoad() {
        TestDataGenerator.generate(testFile)
        manager.load()

        manager.db.tours[0].title = "Измененный тур"
        manager.db.clients[0].firstName = "Измененный клиент"
        manager.save()

        val newManager = DBManager(testFile)
        newManager.load()

        assertEquals("Измененный тур", newManager.db.tours[0].title)
        assertEquals("Измененный клиент", newManager.db.clients[0].firstName)
    }

    @Test
    fun testLargeDatasetOperations() {
        val db = TourDB()
        for (i in 1..100) {
            db.tours.add(Tour(i, i, i, "Тур $i", "Описание $i", 10000.0 + i * 100, 7, "active"))
        }

        manager.db = db
        manager.save()
        manager.load()

        val searchResult = manager.search("tours", "title", "Тур 50")
        assertTrue(searchResult.isNotEmpty())

        val sorted = manager.sort("tours", "basePrice")
        assertEquals(100, sorted.size)

        val sum = manager.aggregate("tours", "basePrice", "sum")
        assertTrue(sum > 0)
    }
}
