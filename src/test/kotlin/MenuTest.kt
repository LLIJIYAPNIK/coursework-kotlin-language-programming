import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class MenuTest {
    private val testFile = "test_menu.json"
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setup() {
        TestDataGenerator.generate(testFile)
        outputStream = ByteArrayOutputStream()
    }

    @AfterEach
    fun cleanup() {
        val file = File(testFile)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun createMenuWithInput(input: String): Menu {
        val inputStream = ByteArrayInputStream(input.toByteArray())
        val manager = DBManager(testFile, PrintStream(outputStream))
        manager.load()
        return Menu(manager, inputStream, PrintStream(outputStream))
    }

    @Test
    fun testInputIntValid() {
        val menu = createMenuWithInput("42\n")
        val result = menu.inputInt("Введите число: ")
        assertEquals(42, result)
    }

    @Test
    fun testInputIntNegative() {
        val menu = createMenuWithInput("-10\n")
        val result = menu.inputInt("Введите число: ")
        assertEquals(-10, result)
    }

    @Test
    fun testInputIntZero() {
        val menu = createMenuWithInput("0\n")
        val result = menu.inputInt("Введите число: ")
        assertEquals(0, result)
    }

    @Test
    fun testInputIntWithInvalidThenValid() {
        val menu = createMenuWithInput("abc\n42\n")
        val result = menu.inputInt("Введите число: ")
        assertEquals(42, result)
        assertTrue(outputStream.toString().contains("Введите целое число"))
    }

    @Test
    fun testInputIntMultipleInvalid() {
        val menu = createMenuWithInput("abc\nxyz\n123\n")
        val result = menu.inputInt("Введите число: ")
        assertEquals(123, result)
    }

    @Test
    fun testInputDoubleValid() {
        val menu = createMenuWithInput("3.14\n")
        val result = menu.inputDouble("Введите число: ")
        assertEquals(3.14, result, 0.001)
    }

    @Test
    fun testInputDoubleWithComma() {
        val menu = createMenuWithInput("3,14\n")
        val result = menu.inputDouble("Введите число: ")
        assertEquals(3.14, result, 0.001)
    }

    @Test
    fun testInputDoubleNegative() {
        val menu = createMenuWithInput("-5.5\n")
        val result = menu.inputDouble("Введите число: ")
        assertEquals(-5.5, result, 0.001)
    }

    @Test
    fun testInputDoubleInteger() {
        val menu = createMenuWithInput("42\n")
        val result = menu.inputDouble("Введите число: ")
        assertEquals(42.0, result, 0.001)
    }

    @Test
    fun testInputDoubleWithInvalidThenValid() {
        val menu = createMenuWithInput("abc\n3.14\n")
        val result = menu.inputDouble("Введите число: ")
        assertEquals(3.14, result, 0.001)
        assertTrue(outputStream.toString().contains("Введите число"))
    }

    // Тесты inputStr()
    @Test
    fun testInputStrValid() {
        val menu = createMenuWithInput("test string\n")
        val result = menu.inputStr("Введите строку: ")
        assertEquals("test string", result)
    }

    @Test
    fun testInputStrWithSpaces() {
        val menu = createMenuWithInput("  test  \n")
        val result = menu.inputStr("Введите строку: ")
        assertEquals("test", result)
    }

    @Test
    fun testInputStrEmpty() {
        val menu = createMenuWithInput("\n")
        val result = menu.inputStr("Введите строку: ")
        assertEquals("", result)
    }

    @Test
    fun testInputStrRussian() {
        val menu = createMenuWithInput("Привет мир\n")
        val result = menu.inputStr("Введите строку: ")
        assertEquals("Привет мир", result)
    }

    @Test
    fun testChooseEntityFirst() {
        val menu = createMenuWithInput("1\n")
        val result = menu.chooseEntity()
        assertEquals("tour_types", result)
    }

    @Test
    fun testChooseEntityLast() {
        val menu = createMenuWithInput("20\n")
        val result = menu.chooseEntity()
        assertEquals("booking_insurances", result)
    }

    @Test
    fun testChooseEntityMiddle() {
        val menu = createMenuWithInput("10\n")
        val result = menu.chooseEntity()
        assertEquals("tour_transports", result)
    }

    @Test
    fun testChooseEntityInvalidZero() {
        val menu = createMenuWithInput("0\n")
        val result = menu.chooseEntity()
        assertEquals("tour_types", result)
    }

    @Test
    fun testChooseEntityInvalidTooLarge() {
        val menu = createMenuWithInput("100\n")
        val result = menu.chooseEntity()
        assertEquals("tour_types", result)
    }

    @Test
    fun testChooseEntityDisplaysList() {
        val menu = createMenuWithInput("1\n")
        menu.chooseEntity()
        val output = outputStream.toString()
        assertTrue(output.contains("Выберите сущность:"))
        assertTrue(output.contains("1. tour_types"))
        assertTrue(output.contains("20. booking_insurances"))
    }

    @Test
    fun testGetEntities() {
        val menu = createMenuWithInput("")
        val entities = menu.getEntities()
        assertEquals(20, entities.size)
        assertTrue(entities.contains("tour_types"))
        assertTrue(entities.contains("clients"))
        assertTrue(entities.contains("bookings"))
    }

    @Test
    fun testRunExit() {
        val menu = createMenuWithInput("0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("СИСТЕМА УЧЁТА ТУРОВ"))
        assertTrue(output.contains("Сохранить перед выходом?"))
    }

    @Test
    fun testRunExitWithSave() {
        val menu = createMenuWithInput("0\ny\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Данные сохранены."))
    }

    @Test
    fun testRunSave() {
        val menu = createMenuWithInput("2\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Данные сохранены."))
    }

    @Test
    fun testRunInvalidChoiceThenExit() {
        val menu = createMenuWithInput("99\n0\nn\n")
        menu.run()
        assertTrue(outputStream.toString().contains("СИСТЕМА УЧЁТА ТУРОВ"))
    }

    @Test
    fun testShowEntityMenuShowRecords() {
        val menu = createMenuWithInput("1\n1\n1\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Меню сущности: tour_types"))
        assertTrue(output.contains("Количество записей:"))
    }

    @Test
    fun testShowEntityMenuDeleteExisting() {
        val menu = createMenuWithInput("1\n1\n2\n1\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Удалено."))
    }

    @Test
    fun testShowEntityMenuDeleteNonExisting() {
        val menu = createMenuWithInput("1\n1\n2\n999\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Запись не найдена."))
    }

    @Test
    fun testShowEntityMenuSearch() {
        val menu = createMenuWithInput("1\n5\n3\nfirstName\nКлиент1\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Меню сущности: clients"))
        assertTrue(output.contains("Количество записей:"))
    }

    @Test
    fun testShowEntityMenuSort() {
        val menu = createMenuWithInput("1\n3\n4\nbasePrice\ny\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Меню сущности: tours"))
    }

    @Test
    fun testShowEntityMenuSortAscending() {
        val menu = createMenuWithInput("1\n3\n4\nbasePrice\nn\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Меню сущности: tours"))
    }

    @Test
    fun testShowEntityMenuAggregate() {
        val menu = createMenuWithInput("1\n3\n5\nbasePrice\nsum\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("sum(basePrice) ="))
    }

    @Test
    fun testShowEntityMenuAggregateAvg() {
        val menu = createMenuWithInput("1\n3\n5\nbasePrice\navg\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("avg(basePrice) ="))
    }

    @Test
    fun testShowEntityMenuAggregateMin() {
        val menu = createMenuWithInput("1\n3\n5\nbasePrice\nmin\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("min(basePrice) ="))
    }

    @Test
    fun testShowEntityMenuAggregateMax() {
        val menu = createMenuWithInput("1\n3\n5\nbasePrice\nmax\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("max(basePrice) ="))
    }

    @Test
    fun testShowEntityMenuBack() {
        val menu = createMenuWithInput("1\n1\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Меню сущности: tour_types"))
        assertTrue(output.contains("СИСТЕМА УЧЁТА ТУРОВ"))
    }

    @Test
    fun testShowEntityMenuInvalidChoice() {
        val menu = createMenuWithInput("1\n99\n0\n0\nn\n")
        menu.run()
        assertTrue(outputStream.toString().contains("Меню сущности: tour_types"))
    }

    @Test
    fun testMenuFullWorkflow() {
        val menu =
            createMenuWithInput(
                "1\n" +
                    "5\n" +
                    "1\n" +
                    "3\n" +
                    "firstName\n" +
                    "Клиент1\n" +
                    "4\n" +
                    "firstName\n" +
                    "n\n" +
                    "5\n" +
                    "id\n" +
                    "sum\n" +
                    "2\n" +
                    "1\n" +
                    "0\n" +
                    "2\n" +
                    "0\n" +
                    "y\n",
            )
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("clients"))
        assertTrue(output.contains("Данные сохранены"))
    }

    @Test
    fun testMenuSearchNoResults() {
        val menu = createMenuWithInput("1\n5\n3\nfirstName\nНЕСУЩЕСТВУЕТ\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Записи отсутствуют"))
    }

    @Test
    fun testMenuDeleteAndVerify() {
        val menu = createMenuWithInput("1\n5\n2\n1\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Удалено."))
    }

    @Test
    fun testMenuSortDescending() {
        val menu = createMenuWithInput("1\n3\n4\nbasePrice\ny\n0\n0\nn\n")
        menu.run()
        val output = outputStream.toString()
        assertTrue(output.contains("Меню сущности: tours"))
    }
}
