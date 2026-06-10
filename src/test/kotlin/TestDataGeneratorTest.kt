import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class TestDataGeneratorTest {
    private val testFile = "test_generator.json"

    @AfterEach
    fun cleanup() {
        val file = File(testFile)
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun testGenerateCreatesFile() {
        TestDataGenerator.generate(testFile)
        val file = File(testFile)
        assertTrue(file.exists())
    }

    @Test
    fun testGenerateCreatesValidJson() {
        TestDataGenerator.generate(testFile)
        val content = File(testFile).readText()
        assertTrue(content.isNotEmpty())
        assertTrue(content.contains("tourTypes"))
        assertTrue(content.contains("destinations"))
        assertTrue(content.contains("tours"))
    }

    @Test
    fun testGenerateCreatesCorrectNumberOfRecords() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        assertEquals(10, manager.db.tourTypes.size)
        assertEquals(10, manager.db.destinations.size)
        assertEquals(10, manager.db.tours.size)
        assertEquals(10, manager.db.tourSchedules.size)
        assertEquals(10, manager.db.clients.size)
        assertEquals(10, manager.db.bookings.size)
    }

    @Test
    fun testGenerateCreatesValidTourTypes() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val tourType = manager.db.tourTypes[0]
        assertEquals(1, tourType.id)
        assertEquals("Тип 1", tourType.name)
        assertEquals("Описание типа 1", tourType.description)
    }

    @Test
    fun testGenerateCreatesValidDestinations() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val destination = manager.db.destinations[0]
        assertEquals(1, destination.id)
        assertTrue(destination.country.isNotEmpty())
        assertTrue(destination.city.isNotEmpty())
    }

    @Test
    fun testGenerateCreatesValidTours() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val tour = manager.db.tours[0]
        assertEquals(1, tour.id)
        assertEquals("Тур 1", tour.title)
        assertTrue(tour.basePrice > 0)
        assertTrue(tour.durationDays > 0)
        assertEquals("active", tour.status)
    }

    @Test
    fun testGenerateCreatesValidTourSchedules() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val schedule = manager.db.tourSchedules[0]
        assertEquals(1, schedule.id)
        assertTrue(schedule.startDate.isNotEmpty())
        assertTrue(schedule.endDate.isNotEmpty())
        assertTrue(schedule.availableSeats > 0)
        assertTrue(schedule.priceMultiplier > 0)
    }

    @Test
    fun testGenerateCreatesValidClients() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val client = manager.db.clients[0]
        assertEquals(1, client.id)
        assertEquals("Клиент1", client.firstName)
        assertEquals("Петров1", client.lastName)
        assertEquals("client1@mail.ru", client.email)
        assertEquals("+79999999999", client.phone)
    }

    @Test
    fun testGenerateCreatesValidBookings() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val booking = manager.db.bookings[0]
        assertEquals(1, booking.id)
        assertEquals("confirmed", booking.status)
        assertTrue(booking.totalPrice > 0)
        assertTrue(booking.discount >= 0)
    }

    @Test
    fun testGenerateOverwritesExistingFile() {
        File(testFile).writeText("old content")

        TestDataGenerator.generate(testFile)

        val content = File(testFile).readText()
        assertTrue(content.contains("tourTypes"))
        assertTrue(!content.contains("old content"))
    }

    @Test
    fun testGenerateCreatesUniqueIds() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val ids = manager.db.tours.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun testGenerateCreatesCorrectTourPrices() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val tour1 = manager.db.tours[0]
        assertEquals(16000.0, tour1.basePrice)

        val tour10 = manager.db.tours[9]
        assertEquals(25000.0, tour10.basePrice)
    }

    @Test
    fun testGenerateCreatesCorrectBookingPrices() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        val booking1 = manager.db.bookings[0]
        assertEquals(20500.0, booking1.totalPrice)
        assertEquals(0.05, booking1.discount)
    }

    @Test
    fun testGenerateCreatesEmptyCollections() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()

        assertEquals(0, manager.db.guides.size)
        assertEquals(0, manager.db.tourGuideAssignments.size)
        assertEquals(0, manager.db.transports.size)
        assertEquals(0, manager.db.tourTransports.size)
        assertEquals(0, manager.db.accommodations.size)
        assertEquals(0, manager.db.tourAccommodations.size)
        assertEquals(0, manager.db.services.size)
        assertEquals(0, manager.db.bookingServices.size)
        assertEquals(0, manager.db.payments.size)
        assertEquals(0, manager.db.employees.size)
        assertEquals(0, manager.db.bookingEmployees.size)
        assertEquals(0, manager.db.reviews.size)
        assertEquals(0, manager.db.insurances.size)
        assertEquals(0, manager.db.bookingInsurances.size)
    }
}
