import java.io.PrintStream

class Application(
    private val filepath: String = "tours_db.json",
    private val manager: DBManager = DBManager(filepath),
    private val output: PrintStream = System.out,
) {
    fun start() {
        if (!manager.load()) {
            TestDataGenerator.generate(filepath)
            manager.load()
        }

        val menu = Menu(manager, System.`in`, output)
        menu.run()
    }

    fun initialize(): Boolean {
        if (!manager.load()) {
            TestDataGenerator.generate(filepath)
            return manager.load()
        }
        return true
    }

    fun getManager(): DBManager = manager
}

fun main() {
    val app = Application()
    app.start()
}
