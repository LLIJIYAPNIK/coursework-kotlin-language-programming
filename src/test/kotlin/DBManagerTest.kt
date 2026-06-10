import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class DBManagerTest {
    private val testFile = "test_tours_db.json"
    private lateinit var manager: DBManager

    @BeforeEach
    fun setup() {
        TestDataGenerator.generate(testFile)
        manager = DBManager(testFile)
        manager.load()
    }

    @AfterEach
    fun cleanup() {
        val file = File(testFile)
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun testLoadExistingFile() {
        val result = manager.load()
        assertTrue(result)
        assertTrue(manager.db.tours.isNotEmpty())
    }

    @Test
    fun testLoadMissingFile() {
        val missingManager = DBManager("missing_file.json")
        val result = missingManager.load()
        assertFalse(result)
    }

    @Test
    fun testLoadCorruptedFile() {
        File(testFile).writeText("invalid json content")
        val result = manager.load()
        assertTrue(result) // Возвращает true даже при ошибке
        assertEquals(0, manager.db.tours.size)
    }

    @Test
    fun testSaveDatabase() {
        manager.save()
        val file = File(testFile)
        assertTrue(file.exists())
        assertTrue(file.readText().isNotEmpty())
    }

    @Test
    fun testSaveAndReload() {
        manager.db.tourTypes.add(TourType(100, "Новый тип", "Описание"))
        manager.save()

        val newManager = DBManager(testFile)
        newManager.load()
        assertEquals(11, newManager.db.tourTypes.size)
    }

    @Test
    fun testGetListTourTypes() {
        val list = manager.getList("tour_types")
        assertEquals(10, list.size)
    }

    @Test
    fun testGetListDestinations() {
        val list = manager.getList("destinations")
        assertEquals(10, list.size)
    }

    @Test
    fun testGetListTours() {
        val list = manager.getList("tours")
        assertEquals(10, list.size)
    }

    @Test
    fun testGetListTourSchedules() {
        val list = manager.getList("tour_schedules")
        assertEquals(10, list.size)
    }

    @Test
    fun testGetListClients() {
        val list = manager.getList("clients")
        assertEquals(10, list.size)
    }

    @Test
    fun testGetListBookings() {
        val list = manager.getList("bookings")
        assertEquals(10, list.size)
    }

    @Test
    fun testGetListGuides() {
        val list = manager.getList("guides")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListTourGuideAssignments() {
        val list = manager.getList("tour_guide_assignments")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListTransports() {
        val list = manager.getList("transports")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListTourTransports() {
        val list = manager.getList("tour_transports")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListAccommodations() {
        val list = manager.getList("accommodations")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListTourAccommodations() {
        val list = manager.getList("tour_accommodations")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListServices() {
        val list = manager.getList("services")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListBookingServices() {
        val list = manager.getList("booking_services")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListPayments() {
        val list = manager.getList("payments")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListEmployees() {
        val list = manager.getList("employees")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListBookingEmployees() {
        val list = manager.getList("booking_employees")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListReviews() {
        val list = manager.getList("reviews")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListInsurances() {
        val list = manager.getList("insurances")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListBookingInsurances() {
        val list = manager.getList("booking_insurances")
        assertEquals(0, list.size)
    }

    @Test
    fun testGetListInvalidEntity() {
        val list = manager.getList("invalid_entity")
        assertEquals(0, list.size)
    }

    @Test
    fun testDeleteExistingRecord() {
        val result = manager.delete("clients", 1)
        assertTrue(result)
        assertEquals(9, manager.getList("clients").size)
    }

    @Test
    fun testDeleteNonExistingRecord() {
        val result = manager.delete("clients", 999)
        assertFalse(result)
    }

    @Test
    fun testDeleteFromEmptyEntity() {
        val result = manager.delete("guides", 1)
        assertFalse(result)
    }

    @Test
    fun testDeleteFromInvalidEntity() {
        val result = manager.delete("invalid_entity", 1)
        assertFalse(result)
    }

    @Test
    fun testDeleteMultipleRecords() {
        manager.delete("clients", 1)
        manager.delete("clients", 2)
        manager.delete("clients", 3)
        assertEquals(7, manager.getList("clients").size)
    }

    // Тесты search()
    @Test
    fun testSearchExistingValue() {
        val result = manager.search("clients", "firstName", "Клиент1")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSearchMissingValue() {
        val result = manager.search("clients", "firstName", "НЕСУЩЕСТВУЕТ")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testSearchCaseInsensitive() {
        val result = manager.search("clients", "firstName", "клиент1")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSearchPartialMatch() {
        val result = manager.search("clients", "firstName", "Клиент")
        assertEquals(10, result.size)
    }

    @Test
    fun testSearchInvalidField() {
        val result = manager.search("clients", "invalidField", "test")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testSearchInTours() {
        val result = manager.search("tours", "title", "Тур 1")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSearchInDestinations() {
        val result = manager.search("destinations", "country", "Россия")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSearchInInvalidEntity() {
        val result = manager.search("invalid_entity", "field", "query")
        assertTrue(result.isEmpty())
    }

    // Тесты sort()
    @Test
    fun testSortAscending() {
        val result = manager.sort("tours", "basePrice")
        val first = result.first() as Tour
        val last = result.last() as Tour
        assertTrue(first.basePrice <= last.basePrice)
    }

    @Test
    fun testSortDescending() {
        val result = manager.sort("tours", "basePrice", true)
        val first = result.first() as Tour
        val last = result.last() as Tour
        assertTrue(first.basePrice >= last.basePrice)
    }

    @Test
    fun testSortByString() {
        val result = manager.sort("clients", "firstName")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSortByInvalidField() {
        val result = manager.sort("tours", "unknownField")
        assertEquals(10, result.size)
    }

    @Test
    fun testSortEmptyList() {
        val result = manager.sort("guides", "firstName")
        assertEquals(0, result.size)
    }

    @Test
    fun testSortInInvalidEntity() {
        val result = manager.sort("invalid_entity", "field")
        assertEquals(0, result.size)
    }

    // Тесты aggregate()
    @Test
    fun testAggregateSum() {
        val result = manager.aggregate("tours", "basePrice", "sum")
        assertEquals(205000.0, result)
    }

    @Test
    fun testAggregateAverage() {
        val result = manager.aggregate("tours", "basePrice", "avg")
        assertEquals(20500.0, result)
    }

    @Test
    fun testAggregateMin() {
        val result = manager.aggregate("tours", "basePrice", "min")
        assertEquals(16000.0, result)
    }

    @Test
    fun testAggregateMax() {
        val result = manager.aggregate("tours", "basePrice", "max")
        assertEquals(25000.0, result)
    }

    @Test
    fun testAggregateInvalidFunction() {
        val result = manager.aggregate("tours", "basePrice", "invalid")
        assertEquals(0.0, result)
    }

    @Test
    fun testAggregateInvalidField() {
        val result = manager.aggregate("tours", "invalidField", "sum")
        assertEquals(0.0, result)
    }

    @Test
    fun testAggregateEmptyList() {
        val result = manager.aggregate("guides", "experienceYears", "sum")
        assertEquals(0.0, result)
    }

    @Test
    fun testAggregateInInvalidEntity() {
        val result = manager.aggregate("invalid_entity", "field", "sum")
        assertEquals(0.0, result)
    }

    @Test
    fun testAggregateDurationDays() {
        val result = manager.aggregate("tours", "durationDays", "sum")
        assertTrue(result > 0)
    }

    @Test
    fun testAggregatePriceMultiplier() {
        val result = manager.aggregate("tour_schedules", "priceMultiplier", "avg")
        assertTrue(result > 0)
    }

    // Интеграционные тесты
    @Test
    fun testGeneratedData() {
        assertEquals(10, manager.db.clients.size)
        assertEquals(10, manager.db.bookings.size)
        assertEquals(10, manager.db.tourSchedules.size)
        assertEquals(10, manager.db.tours.size)
        assertEquals(10, manager.db.destinations.size)
        assertEquals(10, manager.db.tourTypes.size)
    }

    @Test
    fun testDeleteAndSearch() {
        manager.delete("clients", 2)
        val result = manager.search("clients", "id", "Клиент2")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testMultipleOperations() {
        manager.db.tourTypes.add(TourType(100, "Новый", "Описание"))
        manager.save()

        val newManager = DBManager(testFile)
        newManager.load()
        assertEquals(11, newManager.db.tourTypes.size)

        newManager.delete("tour_types", 100)
        assertEquals(10, newManager.db.tourTypes.size)
    }
}
