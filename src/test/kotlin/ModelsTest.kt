import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
        assertEquals("Описание", copy.description)
    }

    @Test
    fun testTourTypeCopyAllFields() {
        val original = TourType(1, "Test", "Desc")
        val copy = original.copy(id = 2, name = "New", description = "NewDesc")
        assertEquals(2, copy.id)
        assertEquals("New", copy.name)
        assertEquals("NewDesc", copy.description)
    }

    @Test
    fun testTourTypeEquals() {
        val type1 = TourType(1, "Test", "Desc")
        val type2 = TourType(1, "Test", "Desc")
        assertEquals(type1, type2)
    }

    @Test
    fun testTourTypeNotEquals() {
        val type1 = TourType(1, "Test", "Desc")
        val type2 = TourType(2, "Test", "Desc")
        assertNotEquals(type1, type2)
    }

    @Test
    fun testTourTypeHashCode() {
        val type1 = TourType(1, "Test", "Desc")
        val type2 = TourType(1, "Test", "Desc")
        assertEquals(type1.hashCode(), type2.hashCode())
    }

    @Test
    fun testTourTypeToString() {
        val tourType = TourType(1, "Test", "Desc")
        val str = tourType.toString()
        assertTrue(str.contains("Test"))
        assertTrue(str.contains("Desc"))
    }

    @Test
    fun testTourTypeMutation() {
        val tourType = TourType(1, "Test", "Desc")
        tourType.name = "New"
        assertEquals("New", tourType.name)
    }

    @Test
    fun testTourTypeWithEmptyStrings() {
        val tourType = TourType(1, "", "")
        assertEquals("", tourType.name)
        assertEquals("", tourType.description)
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
    fun testDestinationCopy() {
        val original = Destination(1, "Россия", "Москва", "Столица")
        val copy = original.copy(city = "Санкт-Петербург")
        assertEquals("Санкт-Петербург", copy.city)
        assertEquals("Россия", copy.country)
    }

    @Test
    fun testDestinationEquals() {
        val dest1 = Destination(1, "Россия", "Москва", "Столица")
        val dest2 = Destination(1, "Россия", "Москва", "Столица")
        assertEquals(dest1, dest2)
    }

    @Test
    fun testDestinationMutation() {
        val dest = Destination(1, "Россия", "Москва", "Столица")
        dest.city = "Новый"
        assertEquals("Новый", dest.city)
    }

    @Test
    fun testTourCreation() {
        val tour = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        assertEquals(1, tour.id)
        assertEquals(1, tour.tourTypeId)
        assertEquals(1, tour.destinationId)
        assertEquals("Тур", tour.title)
        assertEquals("Описание", tour.description)
        assertEquals(10000.0, tour.basePrice)
        assertEquals(7, tour.durationDays)
        assertEquals("active", tour.status)
    }

    @Test
    fun testTourCopy() {
        val original = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        val copy = original.copy(basePrice = 20000.0)
        assertEquals(20000.0, copy.basePrice)
        assertEquals(1, copy.id)
    }

    @Test
    fun testTourEquals() {
        val tour1 = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        val tour2 = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        assertEquals(tour1, tour2)
    }

    @Test
    fun testTourMutation() {
        val tour = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        tour.basePrice = 15000.0
        tour.status = "inactive"
        assertEquals(15000.0, tour.basePrice)
        assertEquals("inactive", tour.status)
    }

    @Test
    fun testTourWithZeroPrice() {
        val tour = Tour(1, 1, 1, "Тур", "Описание", 0.0, 7, "active")
        assertEquals(0.0, tour.basePrice)
    }

    @Test
    fun testTourScheduleCreation() {
        val schedule = TourSchedule(1, 1, "2024-01-01", "2024-01-10", 20, 1.5)
        assertEquals(1, schedule.id)
        assertEquals(1, schedule.tourId)
        assertEquals("2024-01-01", schedule.startDate)
        assertEquals("2024-01-10", schedule.endDate)
        assertEquals(20, schedule.availableSeats)
        assertEquals(1.5, schedule.priceMultiplier)
    }

    @Test
    fun testTourScheduleCopy() {
        val original = TourSchedule(1, 1, "2024-01-01", "2024-01-10", 20, 1.5)
        val copy = original.copy(availableSeats = 15)
        assertEquals(15, copy.availableSeats)
    }

    @Test
    fun testTourScheduleMutation() {
        val schedule = TourSchedule(1, 1, "2024-01-01", "2024-01-10", 20, 1.5)
        schedule.availableSeats = 10
        schedule.priceMultiplier = 2.0
        assertEquals(10, schedule.availableSeats)
        assertEquals(2.0, schedule.priceMultiplier)
    }

    @Test
    fun testClientCreation() {
        val client = Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890")
        assertEquals(1, client.id)
        assertEquals("Иван", client.firstName)
        assertEquals("Петров", client.lastName)
        assertEquals("ivan@mail.ru", client.email)
        assertEquals("+79991234567", client.phone)
        assertEquals("1234567890", client.passportNumber)
    }

    @Test
    fun testClientCopy() {
        val original = Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890")
        val copy = original.copy(email = "new@mail.ru")
        assertEquals("new@mail.ru", copy.email)
        assertEquals("Иван", copy.firstName)
    }

    @Test
    fun testClientEquals() {
        val client1 = Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890")
        val client2 = Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890")
        assertEquals(client1, client2)
    }

    @Test
    fun testClientMutation() {
        val client = Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890")
        client.firstName = "Пётр"
        assertEquals("Пётр", client.firstName)
    }

    @Test
    fun testBookingCreation() {
        val booking = Booking(1, 1, 1, "2024-01-01", "confirmed", 50000.0, 0.1)
        assertEquals(1, booking.id)
        assertEquals(1, booking.tourScheduleId)
        assertEquals(1, booking.clientId)
        assertEquals("2024-01-01", booking.bookingDate)
        assertEquals("confirmed", booking.status)
        assertEquals(50000.0, booking.totalPrice)
        assertEquals(0.1, booking.discount)
    }

    @Test
    fun testBookingCopy() {
        val original = Booking(1, 1, 1, "2024-01-01", "confirmed", 50000.0, 0.1)
        val copy = original.copy(status = "cancelled")
        assertEquals("cancelled", copy.status)
    }

    @Test
    fun testBookingMutation() {
        val booking = Booking(1, 1, 1, "2024-01-01", "confirmed", 50000.0, 0.1)
        booking.totalPrice = 60000.0
        assertEquals(60000.0, booking.totalPrice)
    }

    @Test
    fun testGuideCreation() {
        val guide = Guide(1, "Анна", "Сидорова", "+79991234567", "Сертификат", 5)
        assertEquals(1, guide.id)
        assertEquals("Анна", guide.firstName)
        assertEquals("Сидорова", guide.lastName)
        assertEquals(5, guide.experienceYears)
    }

    @Test
    fun testGuideCopy() {
        val original = Guide(1, "Анна", "Сидорова", "+79991234567", "Сертификат", 5)
        val copy = original.copy(experienceYears = 10)
        assertEquals(10, copy.experienceYears)
    }

    @Test
    fun testTourGuideAssignmentCreation() {
        val assignment = TourGuideAssignment(1, 1, 1, "Основной гид")
        assertEquals(1, assignment.id)
        assertEquals(1, assignment.tourScheduleId)
        assertEquals(1, assignment.guideId)
        assertEquals("Основной гид", assignment.role)
    }

    @Test
    fun testTourGuideAssignmentCopy() {
        val original = TourGuideAssignment(1, 1, 1, "Основной гид")
        val copy = original.copy(role = "Помощник")
        assertEquals("Помощник", copy.role)
    }

    @Test
    fun testTransportCreation() {
        val transport = Transport(1, "Автобус", "Перевозчик", 50, 1000.0)
        assertEquals(1, transport.id)
        assertEquals("Автобус", transport.type)
        assertEquals("Перевозчик", transport.provider)
        assertEquals(50, transport.capacity)
        assertEquals(1000.0, transport.costPerSeat)
    }

    @Test
    fun testTransportMutation() {
        val transport = Transport(1, "Автобус", "Перевозчик", 50, 1000.0)
        transport.capacity = 60
        assertEquals(60, transport.capacity)
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
        assertEquals("Адрес", accommodation.address)
        assertEquals(4.5, accommodation.rating)
        assertEquals("Стандарт, Люкс", accommodation.roomTypes)
    }

    @Test
    fun testAccommodationMutation() {
        val accommodation = Accommodation(1, "Отель", "Адрес", 4.5, "Стандарт")
        accommodation.rating = 5.0
        assertEquals(5.0, accommodation.rating)
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
        assertEquals("Дополнительно", service.category)
        assertEquals(3000.0, service.basePrice)
    }

    @Test
    fun testBookingServiceCreation() {
        val bookingService = BookingService(1, 1, 1, 2, 6000.0)
        assertEquals(1, bookingService.id)
        assertEquals(1, bookingService.bookingId)
        assertEquals(1, bookingService.serviceId)
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
    fun testPaymentMutation() {
        val payment = Payment(1, 1, 50000.0, "2024-01-01", "Карта", "completed")
        payment.status = "pending"
        assertEquals("pending", payment.status)
    }

    @Test
    fun testEmployeeCreation() {
        val employee = Employee(1, "Петр", "Иванов", "Менеджер", "petr@mail.ru", "2024-01-01")
        assertEquals(1, employee.id)
        assertEquals("Петр", employee.firstName)
        assertEquals("Менеджер", employee.position)
        assertEquals("petr@mail.ru", employee.email)
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
        assertEquals(1, review.bookingId)
        assertEquals(5, review.rating)
        assertEquals("Отлично!", review.comment)
        assertEquals("2024-01-15", review.reviewDate)
    }

    @Test
    fun testReviewMutation() {
        val review = Review(1, 1, 5, "Отлично!", "2024-01-15")
        review.rating = 4
        review.comment = "Хорошо"
        assertEquals(4, review.rating)
        assertEquals("Хорошо", review.comment)
    }

    @Test
    fun testInsuranceCreation() {
        val insurance = Insurance(1, "Страхование", "СК", 100000.0, 1500.0)
        assertEquals(1, insurance.id)
        assertEquals("Страхование", insurance.name)
        assertEquals("СК", insurance.provider)
        assertEquals(100000.0, insurance.coverageAmount)
        assertEquals(1500.0, insurance.price)
    }

    @Test
    fun testBookingInsuranceCreation() {
        val bookingInsurance = BookingInsurance(1, 1, 1, 1500.0)
        assertEquals(1, bookingInsurance.id)
        assertEquals(1, bookingInsurance.bookingId)
        assertEquals(1, bookingInsurance.insuranceId)
        assertEquals(1500.0, bookingInsurance.finalPrice)
    }

    @Test
    fun testTourDBCreation() {
        val db = TourDB()
        assertEquals(0, db.tourTypes.size)
        assertEquals(0, db.destinations.size)
        assertEquals(0, db.tours.size)
        assertEquals(0, db.tourSchedules.size)
        assertEquals(0, db.clients.size)
        assertEquals(0, db.bookings.size)
        assertEquals(0, db.guides.size)
        assertEquals(0, db.tourGuideAssignments.size)
        assertEquals(0, db.transports.size)
        assertEquals(0, db.tourTransports.size)
        assertEquals(0, db.accommodations.size)
        assertEquals(0, db.tourAccommodations.size)
        assertEquals(0, db.services.size)
        assertEquals(0, db.bookingServices.size)
        assertEquals(0, db.payments.size)
        assertEquals(0, db.employees.size)
        assertEquals(0, db.bookingEmployees.size)
        assertEquals(0, db.reviews.size)
        assertEquals(0, db.insurances.size)
        assertEquals(0, db.bookingInsurances.size)
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

    @Test
    fun testTourDBAddElements() {
        val db = TourDB()
        db.tours.add(Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active"))
        db.clients.add(Client(1, "Иван", "Петров", "ivan@mail.ru", "+79991234567", "1234567890"))
        assertEquals(1, db.tours.size)
        assertEquals(1, db.clients.size)
    }

    @Test
    fun testTourDBRemoveElements() {
        val db = TourDB()
        db.tours.add(Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active"))
        db.tours.removeAt(0)
        assertEquals(0, db.tours.size)
    }

    @Test
    fun testTourDBClear() {
        val db = TourDB()
        db.tours.add(Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active"))
        db.tours.clear()
        assertEquals(0, db.tours.size)
    }

    @Test
    fun testTourDBMutation() {
        val db = TourDB()
        db.tourTypes.add(TourType(1, "Test", "Desc"))
        db.tourTypes[0].name = "New"
        assertEquals("New", db.tourTypes[0].name)
    }

    @Test
    fun testTourDBEquals() {
        val db1 = TourDB()
        val db2 = TourDB()
        assertEquals(db1, db2)
    }

    @Test
    fun testTourDBCopy() {
        val original = TourDB()
        original.tours.add(Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active"))
        val copy = original.copy()
        assertEquals(original.tours.size, copy.tours.size)
    }

    @Test
    fun testTourTypeDestructuring() {
        val tourType = TourType(1, "Test", "Desc")
        val (id, name, description) = tourType
        assertEquals(1, id)
        assertEquals("Test", name)
        assertEquals("Desc", description)
    }

    @Test
    fun testDestinationDestructuring() {
        val dest = Destination(1, "Россия", "Москва", "Столица")
        val (id, country, city, description) = dest
        assertEquals(1, id)
        assertEquals("Россия", country)
        assertEquals("Москва", city)
        assertEquals("Столица", description)
    }

    @Test
    fun testTourDestructuring() {
        val tour = Tour(1, 1, 1, "Тур", "Описание", 10000.0, 7, "active")
        val (id, tourTypeId, destinationId, title, description, basePrice, durationDays, status) = tour
        assertEquals(1, id)
        assertEquals("Тур", title)
        assertEquals(10000.0, basePrice)
    }
}
