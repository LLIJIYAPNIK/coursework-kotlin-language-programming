fun main() {
    val filepath = "tours_db.json"

    val manager = DBManager(filepath)

    if (!manager.load()) {
        TestDataGenerator.generate(filepath)
        manager.load()
    }

    val menu = Menu(manager)
    menu.run()
}
