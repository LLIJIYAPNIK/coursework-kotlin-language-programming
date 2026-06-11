import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ApplicationTest {
    private val testFile = "test_application.json"
    private lateinit var app: Application
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
    fun testApplicationCreation() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        assertNotNull(app)
    }

    @Test
    fun testApplicationInitializeWithExistingFile() {
        TestDataGenerator.generate(testFile)
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val result = app.initialize()
        assertTrue(result)
    }

    @Test
    fun testApplicationInitializeWithMissingFile() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val result = app.initialize()
        assertTrue(result)
        assertTrue(File(testFile).exists())
    }

    @Test
    fun testApplicationInitializeCreatesTestData() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()

        val manager = app.getManager()
        assertEquals(10, manager.db.tours.size)
        assertEquals(10, manager.db.clients.size)
    }

    @Test
    fun testApplicationGetManager() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        val manager = app.getManager()
        assertNotNull(manager)
    }

    @Test
    fun testApplicationManagerLoadsData() {
        TestDataGenerator.generate(testFile)
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()

        val manager = app.getManager()
        assertTrue(manager.db.tours.isNotEmpty())
        assertTrue(manager.db.clients.isNotEmpty())
    }

    @Test
    fun testApplicationWithCustomFilepath() {
        val customFile = "custom_test.json"
        app = Application(customFile, DBManager(customFile), PrintStream(outputStream))
        app.initialize()

        assertTrue(File(customFile).exists())
        File(customFile).delete()
    }

    @Test
    fun testApplicationPreservesExistingData() {
        TestDataGenerator.generate(testFile)

        val manager = DBManager(testFile)
        manager.load()
        manager.db.tourTypes.add(TourType(100, "Тест", "Описание"))
        manager.save()

        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()

        val appManager = app.getManager()
        assertEquals(11, appManager.db.tourTypes.size)
    }

    @Test
    fun testApplicationMultipleInitializations() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()

        val firstSize = app.getManager().db.tours.size

        app.initialize()
        val secondSize = app.getManager().db.tours.size

        assertEquals(firstSize, secondSize)
    }

    @Test
    fun testApplicationManagerCanPerformOperations() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        app.initialize()

        val manager = app.getManager()

        val searchResult = manager.search("tours", "title", "Тур")
        assertTrue(searchResult.isNotEmpty())

        val sorted = manager.sort("tours", "basePrice")
        assertTrue(sorted.isNotEmpty())

        val sum = manager.aggregate("tours", "basePrice", "sum")
        assertTrue(sum > 0)
    }

    @Test
    fun testApplicationStartMethodExists() {
        app = Application(testFile, DBManager(testFile), PrintStream(outputStream))
        assertNotNull(app::class.members.find { it.name == "start" })
    }

    @Test
    fun testApplicationDefaultFilepath() {
        val defaultApp = Application()
        val manager = defaultApp.getManager()
        assertNotNull(manager)
        File("tours_db.json").delete()
    }

    @Test
    fun testApplicationWithCustomOutput() {
        val customOutput = ByteArrayOutputStream()
        app = Application(testFile, DBManager(testFile), PrintStream(customOutput))
        app.initialize()

        assertNotNull(app.getManager())
    }
}
