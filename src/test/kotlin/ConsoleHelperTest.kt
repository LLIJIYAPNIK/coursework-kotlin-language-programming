import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleHelperTest {
    @Test
    fun testPrintTableEmptyList() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        ConsoleHelper.printTable(emptyList(), "test_entity")

        val output = outputStream.toString()
        assertTrue(output.contains("Записи отсутствуют"))

        System.setOut(System.out)
    }

    @Test
    fun testPrintTableWithRecords() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val list =
            listOf(
                TourType(1, "Тип 1", "Описание 1"),
                TourType(2, "Тип 2", "Описание 2"),
                TourType(3, "Тип 3", "Описание 3"),
            )

        ConsoleHelper.printTable(list, "tour_types")

        val output = outputStream.toString()
        assertTrue(output.contains("Сущность: tour_types"))
        assertTrue(output.contains("Количество записей: 3"))
        assertTrue(output.contains("Тип 1"))

        System.setOut(System.out)
    }

    @Test
    fun testPrintTableMoreThanFiveRecords() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val list = (1..10).map { TourType(it, "Тип $it", "Описание $it") }

        ConsoleHelper.printTable(list, "tour_types")

        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 10"))
        assertTrue(output.contains("... ещё 5 записей"))

        System.setOut(System.out)
    }

    @Test
    fun testPrintTableExactlyFiveRecords() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val list = (1..5).map { TourType(it, "Тип $it", "Описание $it") }

        ConsoleHelper.printTable(list, "tour_types")

        val output = outputStream.toString()
        assertTrue(output.contains("Количество записей: 5"))
        assertTrue(!output.contains("... ещё"))

        System.setOut(System.out)
    }

    @Test
    fun testPrintTableShowsFirstFiveRecords() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val list = (1..10).map { TourType(it, "Тип $it", "Описание $it") }

        ConsoleHelper.printTable(list, "tour_types")

        val output = outputStream.toString()
        assertTrue(output.contains("Тип 1"))
        assertTrue(output.contains("Тип 5"))

        System.setOut(System.out)
    }
}
