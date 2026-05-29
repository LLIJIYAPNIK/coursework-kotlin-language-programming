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
        generateTestData(testFile)

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
    fun testLoadDatabase() {
        val result = manager.load()

        assertTrue(result)
        assertTrue(manager.db.tours.isNotEmpty())
        assertEquals(10, manager.db.tours.size)
    }

    @Test
    fun testLoadMissingFile() {
        val missingManager = DBManager("missing_file.json")

        val result = missingManager.load()

        assertFalse(result)
    }

    @Test
    fun testGetList() {
        val clients = manager.getList("clients")

        assertEquals(10, clients.size)
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
    fun testSearchExistingValue() {
        val result =
            manager.search(
                "clients",
                "firstName",
                "Клиент1",
            )

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSearchMissingValue() {
        val result =
            manager.search(
                "clients",
                "firstName",
                "НЕСУЩЕСТВУЕТ",
            )

        assertTrue(result.isEmpty())
    }

    @Test
    fun testSortAscending() {
        val result =
            manager.sort(
                "tours",
                "basePrice",
            )

        val first = result.first() as Tour
        val last = result.last() as Tour

        assertTrue(first.basePrice <= last.basePrice)
    }

    @Test
    fun testSortDescending() {
        val result =
            manager.sort(
                "tours",
                "basePrice",
                true,
            )

        val first = result.first() as Tour
        val last = result.last() as Tour

        assertTrue(first.basePrice >= last.basePrice)
    }

    @Test
    fun testAggregateSum() {
        val result =
            manager.aggregate(
                "tours",
                "basePrice",
                "sum",
            )

        assertEquals(205000.0, result)
    }

    @Test
    fun testAggregateAverage() {
        val result =
            manager.aggregate(
                "tours",
                "basePrice",
                "avg",
            )

        assertEquals(20500.0, result)
    }

    @Test
    fun testAggregateMin() {
        val result =
            manager.aggregate(
                "tours",
                "basePrice",
                "min",
            )

        assertEquals(16000.0, result)
    }

    @Test
    fun testAggregateMax() {
        val result =
            manager.aggregate(
                "tours",
                "basePrice",
                "max",
            )

        assertEquals(25000.0, result)
    }

    @Test
    fun testSaveDatabase() {
        manager.save()

        val file = File(testFile)

        assertTrue(file.exists())
        assertTrue(file.readText().isNotEmpty())
    }

    @Test
    fun testGeneratedData() {
        assertEquals(10, manager.db.clients.size)
        assertEquals(10, manager.db.bookings.size)
        assertEquals(10, manager.db.tourSchedules.size)
    }

    @Test
    fun testSearchCaseInsensitive() {
        val result =
            manager.search(
                "clients",
                "firstName",
                "клиент1",
            )

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testAggregateInvalidFunction() {
        val result =
            manager.aggregate(
                "tours",
                "basePrice",
                "invalid",
            )

        assertEquals(0.0, result)
    }

    @Test
    fun testSortInvalidField() {
        val result =
            manager.sort(
                "tours",
                "unknownField",
            )

        assertEquals(10, result.size)
    }

    @Test
    fun testDeleteFromEmptyEntity() {
        val result =
            manager.delete(
                "unknown_entity",
                1,
            )

        assertFalse(result)
    }
}
