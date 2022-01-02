package at.fhjoanneum.photo_spots

object LocationRepository {
    private val locations: List<Location>

    init {
        locations = listOf(
            Location(
                "0001",
                "myHome",
                4.5f,
                "test description",
                GpsData(0.0, 0.0, 0.0, "TestStraße 21"),
                listOf<String>("test1", "123", "456"),
                ""
            ),Location(
                "0002",
                "myHome",
                4.5f,
                "test description",
                GpsData(0.0, 20.0, 40.0, "TestStraße 21"),
                listOf<String>("test1", "123", ""),
                ""
            ),Location(
                "0003",
                "myHome",
                4.5f,
                "test description",
                GpsData(0.0, 10.0, 30.0, "TestStraße 21"),
                listOf<String>("test1", "123", ""),
                ""
            )

        )

    }

    fun locationById(id :String) :Location? {
        return locations.find{it.id == id}
    }

    fun getLocationIds() :List<String> {
        return locations.map{it.id}
    }


}