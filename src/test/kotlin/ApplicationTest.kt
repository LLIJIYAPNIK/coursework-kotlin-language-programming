import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ApplicationTest {
    private val testFile = "test_application.json"
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setup() {
        outputStream = ByteArrayOutputStream()
        File(testFile).delete()
    }

    @AfterEach
    fun cleanup() {
        val file = File(testFile)
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun testApplicationCreationWithDefaultParams() {
        val app = Application()
        assertNotNull(app)
        File("tours_db.json").delete()
    }

    @Test
    fun testApplicationCreationWithCustomFilepath() {
        val app = Application(testFile)
        assertNotNull(app)
    }

    @Test
    fun testApplicationCreationWithCustomManager() {
        val manager = DBManager(testFile, PrintStream(outputStream))
        val app = Application(testFile, manager, PrintStream(outputStream))
        assertNotNull(app)
    }

    @Test
    fun testApplicationCreationWithAllParams() {
        val manager = DBManager(testFile, PrintStream(outputStream))
        val output = PrintStream(outputStream)
        val app = Application(testFile, manager, output)
        assertNotNull(app)
    }

    @Test
    fun testGetManagerReturnsNonNull() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val manager = app.getManager()
        assertNotNull(manager)
    }

    @Test
    fun testGetManagerReturnsSameInstance() {
        val manager = DBManager(testFile, PrintStream(outputStream))
        val app = Application(testFile, manager, PrintStream(outputStream))
        assertSame(manager, app.getManager())
    }

    @Test
    fun testGetManagerMultipleCalls() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val manager1 = app.getManager()
        val manager2 = app.getManager()
        assertSame(manager1, manager2)
    }

    @Test
    fun testInitializeWithExistingFile() {
        TestDataGenerator.generate(testFile)
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val result = app.initialize()
        assertTrue(result)
    }

    @Test
    fun testInitializeWithExistingFileLoadsData() {
        TestDataGenerator.generate(testFile)
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val manager = app.getManager()
        assertTrue(manager.db.tours.isNotEmpty())
        assertEquals(10, manager.db.tours.size)
    }

    @Test
    fun testInitializeWithExistingFilePreservesData() {
        TestDataGenerator.generate(testFile)
        val manager = DBManager(testFile, PrintStream(outputStream))
        manager.load()
        manager.db.tourTypes.add(TourType(100, "Test", "Desc"))
        manager.save()

        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        assertEquals(11, app.getManager().db.tourTypes.size)
    }

    @Test
    fun testInitializeWithMissingFile() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val result = app.initialize()
        assertTrue(result)
    }

    @Test
    fun testInitializeWithMissingFileCreatesFile() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        assertTrue(File(testFile).exists())
    }

    @Test
    fun testInitializeWithMissingFileCreatesTestData() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val manager = app.getManager()
        assertEquals(10, manager.db.tours.size)
        assertEquals(10, manager.db.clients.size)
        assertEquals(10, manager.db.bookings.size)
    }

    @Test
    fun testInitializeWithMissingFileCreatesAllEntities() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val manager = app.getManager()
        assertEquals(10, manager.db.tourTypes.size)
        assertEquals(10, manager.db.destinations.size)
        assertEquals(10, manager.db.tourSchedules.size)
    }

    @Test
    fun testMultipleInitializations() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val firstSize = app.getManager().db.tours.size
        app.initialize()
        val secondSize = app.getManager().db.tours.size
        assertEquals(firstSize, secondSize)
    }

    @Test
    fun testInitializeAfterDataModification() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        app.getManager().db.tours.add(Tour(100, 1, 1, "Test", "Desc", 1000.0, 1, "active"))
        app.getManager().save()
        app.initialize()
        assertEquals(11, app.getManager().db.tours.size)
    }

    @Test
    fun testApplicationManagerCanSearch() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val result = app.getManager().search("tours", "title", "Тур")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testApplicationManagerCanSort() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val result = app.getManager().sort("tours", "basePrice")
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testApplicationManagerCanAggregate() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val result = app.getManager().aggregate("tours", "basePrice", "sum")
        assertTrue(result > 0)
    }

    @Test
    fun testApplicationManagerCanDelete() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        val initialSize = app.getManager().getList("clients").size
        app.getManager().delete("clients", 1)
        assertEquals(initialSize - 1, app.getManager().getList("clients").size)
    }

    @Test
    fun testApplicationManagerCanSave() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()
        app.getManager().db.tourTypes.add(TourType(100, "Test", "Desc"))
        app.getManager().save()
        assertTrue(File(testFile).readText().contains("Test"))
    }

    @Test
    fun testStartMethodExists() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val method = app::class.members.find { it.name == "start" }
        assertNotNull(method)
    }

    @Test
    fun testStartWithImmediateExit() {
        val originalIn = System.`in`
        try {
            System.setIn(ByteArrayInputStream("0\nn\n".toByteArray()))
            val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
            app.start()
            assertTrue(File(testFile).exists())
        } finally {
            System.setIn(originalIn)
        }
    }

    @Test
    fun testApplicationWithRelativePath() {
        val relativePath = "./test_relative.json"
        val app = Application(relativePath, DBManager(relativePath), PrintStream(outputStream))
        app.initialize()
        assertTrue(File(relativePath).exists())
        File(relativePath).delete()
    }

    @Test
    fun testApplicationWithAbsolutePath() {
        val absolutePath = File(testFile).absolutePath
        val app = Application(absolutePath, DBManager(absolutePath), PrintStream(outputStream))
        app.initialize()
        assertTrue(File(absolutePath).exists())
    }

    @Test
    fun testApplicationWithDefaultOutput() {
        val app = Application(testFile)
        assertNotNull(app)
        app.initialize()
        File(testFile).delete()
    }

    @Test
    fun testFullApplicationWorkflow() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))

        assertTrue(app.initialize())

        val manager = app.getManager()
        assertEquals(10, manager.db.tours.size)

        val searchResult = manager.search("tours", "title", "Тур 1")
        assertTrue(searchResult.isNotEmpty())

        manager.db.tours.add(Tour(100, 1, 1, "New", "Desc", 1000.0, 1, "active"))
        manager.save()

        val newApp = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        newApp.initialize()
        assertEquals(11, newApp.getManager().db.tours.size)
    }

    @Test
    fun testApplicationDataIntegrity() {
        val app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()

        val manager = app.getManager()
        val tour = manager.db.tours[0]

        assertTrue(tour.id > 0)
        assertTrue(tour.title.isNotEmpty())
        assertTrue(tour.basePrice > 0)
        assertTrue(tour.durationDays > 0)
    }
}
