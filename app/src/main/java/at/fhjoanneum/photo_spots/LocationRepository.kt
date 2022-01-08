package at.fhjoanneum.photo_spots

/*

!!!

LocationRepository nur fürs Testen gedacht

!!!

 */

object LocationRepository {
    private val locations: List<UploadPostModel2>

    init {
        locations = listOf(
            UploadPostModel2(
                "0001",
                "myHome",
                4.5f,
                "test description",
                GpsDataModel(0.0, 0.0, 0.0, "TestStraße 21"),
                listOf<String>("test1", "123", "456"),
                "",
                mutableListOf<PostComment>(PostComment("user1", "test1", mutableListOf()), PostComment("user2", "123", mutableListOf(Pair("user2", false), Pair("user3", false))), PostComment("user3", "456", mutableListOf(Pair("user3", true)))),
                mutableListOf<Pair<String, Boolean>>(Pair("user1", true), Pair("user2", true),Pair("user3", true))
            ), UploadPostModel2(
                "0002",
                "myHome",
                4.5f,
                "test description",
                GpsDataModel(0.0, 20.0, 40.0, "TestStraße 21"),
                listOf<String>("test1", "123", ""),
                "",
                mutableListOf<PostComment>(PostComment("user1", "test1", mutableListOf())),
                mutableListOf<Pair<String, Boolean>>(Pair("user1", true), Pair("user2", false),Pair("user3", true))
            ), UploadPostModel2(
                "0003",
                "myHome",
                4.5f,
                "test description",
                GpsDataModel(0.0, 10.0, 30.0, "TestStraße 21"),
                listOf<String>("test1", "123", ""),
                "",
                mutableListOf<PostComment>(),
                mutableListOf<Pair<String, Boolean>>(Pair("user1", false), Pair("user2", false),Pair("user3", false))
            )

        )

    }

    fun locationById(id :String) :UploadPostModel2? {
        return locations.find{it.id == id}
    }

    fun getLocationIds() :List<String> {
        return locations.map{it.id}
    }


}