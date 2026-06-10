import kotlinx.serialization.Serializable

@Serializable
data class TourType(var id: Int, var name: String, var description: String)

@Serializable
data class Destination(
    var id: Int,
    var country: String,
    var city: String,
    var description: String,
)

@Serializable
data class Tour(
    var id: Int,
    var tourTypeId: Int,
    var destinationId: Int,
    var title: String,
    var description: String,
    var basePrice: Double,
    var durationDays: Int,
    var status: String,
)

@Serializable
data class TourSchedule(
    var id: Int,
    var tourId: Int,
    var startDate: String,
    var endDate: String,
    var availableSeats: Int,
    var priceMultiplier: Double,
)

@Serializable
data class Client(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String,
    var passportNumber: String,
)

@Serializable
data class Booking(
    var id: Int,
    var tourScheduleId: Int,
    var clientId: Int,
    var bookingDate: String,
    var status: String,
    var totalPrice: Double,
    var discount: Double,
)

@Serializable
data class Guide(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var certification: String,
    var experienceYears: Int,
)

@Serializable
data class TourGuideAssignment(
    var id: Int,
    var tourScheduleId: Int,
    var guideId: Int,
    var role: String,
)

@Serializable
data class Transport(
    var id: Int,
    var type: String,
    var provider: String,
    var capacity: Int,
    var costPerSeat: Double,
)

@Serializable
data class TourTransport(
    var id: Int,
    var tourScheduleId: Int,
    var transportId: Int,
    var departurePoint: String,
    var arrivalPoint: String,
)

@Serializable
data class Accommodation(
    var id: Int,
    var name: String,
    var address: String,
    var rating: Double,
    var roomTypes: String,
)

@Serializable
data class TourAccommodation(
    var id: Int,
    var tourScheduleId: Int,
    var accommodationId: Int,
    var roomType: String,
    var nights: Int,
    var pricePerNight: Double,
)

@Serializable
data class Service(
    var id: Int,
    var name: String,
    var category: String,
    var basePrice: Double,
)

@Serializable
data class BookingService(
    var id: Int,
    var bookingId: Int,
    var serviceId: Int,
    var quantity: Int,
    var finalPrice: Double,
)

@Serializable
data class Payment(
    var id: Int,
    var bookingId: Int,
    var amount: Double,
    var paymentDate: String,
    var paymentMethod: String,
    var status: String,
)

@Serializable
data class Employee(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var position: String,
    var email: String,
    var hireDate: String,
)

@Serializable
data class BookingEmployee(
    var id: Int,
    var bookingId: Int,
    var employeeId: Int,
)

@Serializable
data class Review(
    var id: Int,
    var bookingId: Int,
    var rating: Int,
    var comment: String,
    var reviewDate: String,
)

@Serializable
data class Insurance(
    var id: Int,
    var name: String,
    var provider: String,
    var coverageAmount: Double,
    var price: Double,
)

@Serializable
data class BookingInsurance(
    var id: Int,
    var bookingId: Int,
    var insuranceId: Int,
    var finalPrice: Double,
)

@Serializable
data class TourDB(
    var tourTypes: MutableList<TourType> = mutableListOf(),
    var destinations: MutableList<Destination> = mutableListOf(),
    var tours: MutableList<Tour> = mutableListOf(),
    var tourSchedules: MutableList<TourSchedule> = mutableListOf(),
    var clients: MutableList<Client> = mutableListOf(),
    var bookings: MutableList<Booking> = mutableListOf(),
    var guides: MutableList<Guide> = mutableListOf(),
    var tourGuideAssignments: MutableList<TourGuideAssignment> = mutableListOf(),
    var transports: MutableList<Transport> = mutableListOf(),
    var tourTransports: MutableList<TourTransport> = mutableListOf(),
    var accommodations: MutableList<Accommodation> = mutableListOf(),
    var tourAccommodations: MutableList<TourAccommodation> = mutableListOf(),
    var services: MutableList<Service> = mutableListOf(),
    var bookingServices: MutableList<BookingService> = mutableListOf(),
    var payments: MutableList<Payment> = mutableListOf(),
    var employees: MutableList<Employee> = mutableListOf(),
    var bookingEmployees: MutableList<BookingEmployee> = mutableListOf(),
    var reviews: MutableList<Review> = mutableListOf(),
    var insurances: MutableList<Insurance> = mutableListOf(),
    var bookingInsurances: MutableList<BookingInsurance> = mutableListOf(),
)
