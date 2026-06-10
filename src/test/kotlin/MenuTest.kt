import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class MenuTest {
    private val testFile = "test_menu.json"
    private lateinit var manager: DBManager
    private lateinit var menu: Menu

    @BeforeEach
    fun setup() {
        TestDataGenerator.generate(testFile)
        manager = DBManager(testFile)
        manager.load()
        menu = Menu(manager)
    }

    @AfterEach
    fun cleanup() {
        val file = File(testFile)
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun testMenuCreation() {
        assertTrue(menu != null)
    }

    @Test
    fun testMenuHasAccessToManager() {
        assertEquals(10, manager.db.tours.size)
    }

    @Test
    fun testMenuCanAccessAllEntities() {
        val entities =
            listOf(
                "tour_types", "destinations", "tours", "tour_schedules",
                "clients", "bookings", "guides", "tour_guide_assignments",
                "transports", "tour_transports", "accommodations", "tour_accommodations",
                "services", "booking_services", "payments", "employees",
                "booking_employees", "reviews", "insurances", "booking_insurances",
            )

        entities.forEach { entity ->
            val list = manager.getList(entity)
            assertTrue(list != null)
        }
    }

    @Test
    fun testMenuCanPerformSearch() {
        val result = manager.search("clients", "firstName", "Клиент1")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testMenuCanPerformSort() {
        val result = manager.sort("tours", "basePrice")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testMenuCanPerformAggregate() {
        val result = manager.aggregate("tours", "basePrice", "sum")
        assertTrue(result > 0)
    }

    @Test
    fun testMenuCanDeleteRecord() {
        val initialSize = manager.getList("clients").size
        manager.delete("clients", 1)
        assertEquals(initialSize - 1, manager.getList("clients").size)
    }

    @Test
    fun testMenuCanSaveDatabase() {
        manager.db.tourTypes.add(TourType(100, "Тест", "Описание"))
        manager.save()

        val file = File(testFile)
        assertTrue(file.exists())
        assertTrue(file.readText().contains("Тест"))
    }
}
