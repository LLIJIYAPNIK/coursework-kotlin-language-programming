import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleHelperTest {
    private lateinit var originalOut: PrintStream
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setup() {
        originalOut = System.out
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun cleanup() {
        System.setOut(originalOut)
    }

    @Test
    fun testPrintTableEmptyList() {
        ConsoleHelper.printTable(emptyList(), "test_entity")
        val output = outputStream.toString()
        assertTrue(output.contains("Записи отсутствуют"))
    }

    @Test
    fun testPrintTableWithOneRecord() {
        val list = listOf(TourType(1, "Тип 1", "Описание 1"))
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: tour_types"))
        assertTrue(output.contains("Количество записей: 1"))
        assertTrue(output.contains("Тип 1"))
    }

    @Test
    fun testPrintTableWithThreeRecords() {
        val list =
            listOf(
                TourType(1, "Тип 1", "Описание 1"),
                TourType(2, "Тип 2", "Описание 2"),
                TourType(3, "Тип 3", "Описание 3"),
            )
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 3"))
        assertTrue(output.contains("Тип 1"))
        assertTrue(output.contains("Тип 2"))
        assertTrue(output.contains("Тип 3"))
    }

    @Test
    fun testPrintTableWithFiveRecords() {
        val list = (1..5).map { TourType(it, "Тип $it", "Описание $it") }
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 5"))
        assertFalse(output.contains("... ещё"))
    }

    @Test
    fun testPrintTableWithSixRecords() {
        val list = (1..6).map { TourType(it, "Тип $it", "Описание $it") }
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 6"))
        assertTrue(output.contains("... ещё 1 записей"))
    }

    @Test
    fun testPrintTableWithTenRecords() {
        val list = (1..10).map { TourType(it, "Тип $it", "Описание $it") }
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 10"))
        assertTrue(output.contains("... ещё 5 записей"))
    }

    @Test
    fun testPrintTableShowsOnlyFirstFive() {
        val list = (1..10).map { TourType(it, "Тип $it", "Описание $it") }
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Тип 1"))
        assertTrue(output.contains("Тип 5"))
        assertFalse(output.contains("Тип 6"))
        assertFalse(output.contains("Тип 10"))
    }

    @Test
    fun testPrintTableWithDifferentEntity() {
        val list =
            listOf(
                Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890"),
            )
        ConsoleHelper.printTable(list, "clients")
        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: clients"))
        assertTrue(output.contains("Иван"))
    }

    @Test
    fun testPrintTableWithTour() {
        val list = listOf(Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active"))
        ConsoleHelper.printTable(list, "tours")
        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: tours"))
        assertTrue(output.contains("Тур"))
    }

    @Test
    fun testPrintTableWithMixedTypes() {
        val list =
            listOf(
                TourType(1, "Тип 1", "Описание 1"),
                Destination(2, "Россия", "Москва", "Столица"),
            )
        ConsoleHelper.printTable(list, "mixed")
        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 2"))
    }

    @Test
    fun testPrintTableWithCustomPrintStream() {
        val customOutput = ByteArrayOutputStream()
        val list = listOf(TourType(1, "Test", "Desc"))
        ConsoleHelper.printTable(list, "test", PrintStream(customOutput))
        val output = customOutput.toString()
        assertTrue(output.contains("Test"))
    }

    @Test
    fun testPrintTableWithDefaultPrintStream() {
        val list = listOf(TourType(1, "Test", "Desc"))
        ConsoleHelper.printTable(list, "test")
        val output = outputStream.toString()
        assertTrue(output.contains("Test"))
    }

    @Test
    fun testPrintTableWithLargeDataset() {
        val list = (1..100).map { TourType(it, "Тип $it", "Описание $it") }
        ConsoleHelper.printTable(list, "tour_types")
        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 100"))
        assertTrue(output.contains("... ещё 95 записей"))
    }

    @Test
    fun testPrintTableOutputFormat() {
        val list = listOf(TourType(1, "Test", "Desc"))
        ConsoleHelper.printTable(list, "test_entity")
        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: test_entity"))
        assertTrue(output.contains("Количество записей: 1"))
    }

    @Test
    fun testPrintTableEmptyEntityName() {
        val list = listOf(TourType(1, "Test", "Desc"))
        ConsoleHelper.printTable(list, "")
        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: "))
    }

    @Test
    fun testPrintTableWithNullElements() {
        val list = listOf<Any>(TourType(1, "Test", "Desc"))
        ConsoleHelper.printTable(list, "test")
        val output = outputStream.toString()
        assertTrue(output.contains("Test"))
    }

    @Test
    fun testInputIntMethodExists() {
        val method = ConsoleHelper::class.members.find { it.name == "inputInt" }
        assertNotNull(method)
    }

    @Test
    fun testInputIntReturnType() {
        val method = ConsoleHelper::class.members.find { it.name == "inputInt" }
        assertNotNull(method)
        assertEquals("kotlin.Int", method!!.returnType.toString())
    }

    @Test
    fun testInputDoubleMethodExists() {
        val method = ConsoleHelper::class.members.find { it.name == "inputDouble" }
        assertNotNull(method)
    }

    @Test
    fun testInputDoubleReturnType() {
        val method = ConsoleHelper::class.members.find { it.name == "inputDouble" }
        assertNotNull(method)
        assertEquals("kotlin.Double", method!!.returnType.toString())
    }

    @Test
    fun testInputStrMethodExists() {
        val method = ConsoleHelper::class.members.find { it.name == "inputStr" }
        assertNotNull(method)
    }

    @Test
    fun testInputStrReturnType() {
        val method = ConsoleHelper::class.members.find { it.name == "inputStr" }
        assertNotNull(method)
        assertEquals("kotlin.String", method!!.returnType.toString())
    }

    @Test
    fun testConsoleHelperIsObject() {
        val instance1 = ConsoleHelper
        val instance2 = ConsoleHelper
        assertSame(instance1, instance2)
    }

    @Test
    fun testConsoleHelperNotNull() {
        assertNotNull(ConsoleHelper)
    }

    @Test
    fun testConsoleHelperHasAllMethods() {
        val methods = ConsoleHelper::class.members.map { it.name }
        assertTrue(methods.contains("inputInt"))
        assertTrue(methods.contains("inputDouble"))
        assertTrue(methods.contains("inputStr"))
        assertTrue(methods.contains("printTable"))
    }

    @Test
    fun testPrintTableMultipleTimes() {
        val list1 = listOf(TourType(1, "Test1", "Desc1"))
        val list2 = listOf(TourType(2, "Test2", "Desc2"))

        ConsoleHelper.printTable(list1, "entity1")
        ConsoleHelper.printTable(list2, "entity2")

        val output = outputStream.toString()
        assertTrue(output.contains("Test1"))
        assertTrue(output.contains("Test2"))
        assertTrue(output.contains("entity1"))
        assertTrue(output.contains("entity2"))
    }

    @Test
    fun testPrintTableWithEmptyEntityName() {
        val list = listOf(TourType(1, "Test", "Desc"))
        ConsoleHelper.printTable(list, "")
        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: "))
    }

    @Test
    fun testPrintTablePreservesOrder() {
        val list =
            listOf(
                TourType(3, "Third", "Desc3"),
                TourType(1, "First", "Desc1"),
                TourType(2, "Second", "Desc2"),
            )
        ConsoleHelper.printTable(list, "test")
        val output = outputStream.toString()
        val thirdPos = output.indexOf("Third")
        val firstPos = output.indexOf("First")
        val secondPos = output.indexOf("Second")
        assertTrue(thirdPos < firstPos)
        assertTrue(firstPos < secondPos)
    }
}
