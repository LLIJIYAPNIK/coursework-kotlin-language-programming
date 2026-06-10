import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ModelsTest {
    @Test
    fun testTourTypeCreation() {
        val tourType = TourType(1, "Экскурсионный", "Описание")
        assertEquals(1, tourType.id)
        assertEquals("Экскурсионный", tourType.name)
        assertEquals("Описание", tourType.description)
    }

    @Test
    fun testTourTypeCopy() {
        val original = TourType(1, "Экскурсионный", "Описание")
        val copy = original.copy(name = "Пляжный")
        assertEquals(1, copy.id)
        assertEquals("Пляжный", copy.name)
        assertNotEquals(original.name, copy.name)
    }

    @Test
    fun testDestinationCreation() {
        val dest = Destination(1, "Россия", "Москва", "Столица")
        assertEquals(1, dest.id)
        assertEquals("Россия", dest.country)
        assertEquals("Москва", dest.city)
        assertEquals("Столица", dest.description)
    }

    @Test
    fun testTourCreation() {
        val tour = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        assertEquals(1, tour.id)
        assertEquals(10000.0, tour.basePrice)
        assertEquals(7, tour.durationDays)
        assertEquals("active", tour.status)
    }

    @Test
    fun testTourScheduleCreation() {
        val schedule = TourSchedule(1, 1, "2024-01-01", "2024-01-10", 20, 1.5)
        assertEquals(1, schedule.id)
        assertEquals("2024-01-01", schedule.startDate)
        assertEquals(20, schedule.availableSeats)
        assertEquals(1.5, schedule.priceMultiplier)
    }

    @Test
    fun testClientCreation() {
        val client = Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890")
        assertEquals(1, client.id)
        assertEquals("Иван", client.firstName)
        assertEquals("Петров", client.lastName)
        assertEquals("ivan@mail.ru", client.email)
    }

    @Test
    fun testBookingCreation() {
        val booking = Booking(1, 1, 1, "2024-01-01", "confirmed", 50000.0, 0.1)
        assertEquals(1, booking.id)
        assertEquals("confirmed", booking.status)
        assertEquals(50000.0, booking.totalPrice)
        assertEquals(0.1, booking.discount)
    }

    @Test
    fun testGuideCreation() {
        val guide = Guide(1, "Анна", "Сидорова", "+79991234567", "Сертификат", 5)
        assertEquals(1, guide.id)
        assertEquals("Анна", guide.firstName)
        assertEquals(5, guide.experienceYears)
    }

    @Test
    fun testTourGuideAssignmentCreation() {
        val assignment = TourGuideAssignment(1, 1, 1, "Основной гид")
        assertEquals(1, assignment.id)
        assertEquals("Основной гид", assignment.role)
    }

    @Test
    fun testTransportCreation() {
        val transport = Transport(1, "Автобус", "Перевозчик", 50, 1000.0)
        assertEquals(1, transport.id)
        assertEquals("Автобус", transport.type)
        assertEquals(50, transport.capacity)
        assertEquals(1000.0, transport.costPerSeat)
    }

    @Test
    fun testTourTransportCreation() {
        val tourTransport = TourTransport(1, 1, 1, "Москва", "Санкт-Петербург")
        assertEquals(1, tourTransport.id)
        assertEquals("Москва", tourTransport.departurePoint)
        assertEquals("Санкт-Петербург", tourTransport.arrivalPoint)
    }

    @Test
    fun testAccommodationCreation() {
        val accommodation = Accommodation(1, "Отель", "Адрес", 4.5, "Стандарт, Люкс")
        assertEquals(1, accommodation.id)
        assertEquals("Отель", accommodation.name)
        assertEquals(4.5, accommodation.rating)
    }

    @Test
    fun testTourAccommodationCreation() {
        val tourAccommodation = TourAccommodation(1, 1, 1, "Люкс", 5, 5000.0)
        assertEquals(1, tourAccommodation.id)
        assertEquals("Люкс", tourAccommodation.roomType)
        assertEquals(5, tourAccommodation.nights)
        assertEquals(5000.0, tourAccommodation.pricePerNight)
    }

    @Test
    fun testServiceCreation() {
        val service = Service(1, "Экскурсия", "Дополнительно", 3000.0)
        assertEquals(1, service.id)
        assertEquals("Экскурсия", service.name)
        assertEquals(3000.0, service.basePrice)
    }

    @Test
    fun testBookingServiceCreation() {
        val bookingService = BookingService(1, 1, 1, 2, 6000.0)
        assertEquals(1, bookingService.id)
        assertEquals(2, bookingService.quantity)
        assertEquals(6000.0, bookingService.finalPrice)
    }

    @Test
    fun testPaymentCreation() {
        val payment = Payment(1, 1, 50000.0, "2024-01-01", "Карта", "completed")
        assertEquals(1, payment.id)
        assertEquals(50000.0, payment.amount)
        assertEquals("Карта", payment.paymentMethod)
        assertEquals("completed", payment.status)
    }

    @Test
    fun testEmployeeCreation() {
        val employee = Employee(1, "Петр", "Иванов", "Менеджер", "petr@mail.ru", "2024-01-01")
        assertEquals(1, employee.id)
        assertEquals("Петр", employee.firstName)
        assertEquals("Менеджер", employee.position)
    }

    @Test
    fun testBookingEmployeeCreation() {
        val bookingEmployee = BookingEmployee(1, 1, 1)
        assertEquals(1, bookingEmployee.id)
        assertEquals(1, bookingEmployee.bookingId)
        assertEquals(1, bookingEmployee.employeeId)
    }

    @Test
    fun testReviewCreation() {
        val review = Review(1, 1, 5, "Отлично!", "2024-01-15")
        assertEquals(1, review.id)
        assertEquals(5, review.rating)
        assertEquals("Отлично!", review.comment)
    }

    @Test
    fun testInsuranceCreation() {
        val insurance = Insurance(1, "Страхование", "СК", 100000.0, 1500.0)
        assertEquals(1, insurance.id)
        assertEquals("Страхование", insurance.name)
        assertEquals(100000.0, insurance.coverageAmount)
        assertEquals(1500.0, insurance.price)
    }

    @Test
    fun testBookingInsuranceCreation() {
        val bookingInsurance = BookingInsurance(1, 1, 1, 1500.0)
        assertEquals(1, bookingInsurance.id)
        assertEquals(1500.0, bookingInsurance.finalPrice)
    }

    @Test
    fun testTourDBCreation() {
        val db = TourDB()
        assertEquals(0, db.tourTypes.size)
        assertEquals(0, db.destinations.size)
        assertEquals(0, db.tours.size)
        assertEquals(0, db.clients.size)
    }

    @Test
    fun testTourDBWithInitialData() {
        val db =
            TourDB(
                tourTypes = mutableListOf(TourType(1, "Тип", "Описание")),
                destinations = mutableListOf(Destination(1, "Россия", "Москва", "Столица")),
            )
        assertEquals(1, db.tourTypes.size)
        assertEquals(1, db.destinations.size)
        assertEquals(0, db.tours.size)
    }
}
