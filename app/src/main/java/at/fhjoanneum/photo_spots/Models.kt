package at.fhjoanneum.photo_spots

class Location(val id: String, val title: String, val rating: Float, val description: String, val GpsData: GpsData, val Categories: List<String>, val ImageUriString: String)

class GpsData (val Altitude: Double, val Latitude: Double, val Longitude: Double, val Address: String)