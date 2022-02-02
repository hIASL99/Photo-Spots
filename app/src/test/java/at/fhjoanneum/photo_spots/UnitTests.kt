package at.fhjoanneum.photo_spots

import org.junit.Assert.assertEquals
import org.junit.Test


class UnitTests {

    @Test
    fun averageForEmptyRates_isCorrect() {
        val post = PostModel(1, "userId",
            "This is the title", "",
            "Description", "",
            0.0, 0.0,
            0.0, "Username",
            listOf(),
            mutableListOf(),
            mutableListOf())

        val calculatedVal = post.getRating()
        val assertedVal = 0.0f
        val allowableDifference = 0.000001f

        assertEquals(assertedVal, calculatedVal, allowableDifference)
    }

    @Test
    fun averageForRates_isCorrect() {
        val post = PostModel(1, "userId",
            "This is the title", "",
            "Description", "",
            0.0, 0.0,
            0.0, "Username",
            listOf(),
            mutableListOf(Rating("",true), Rating("", true), Rating("", false)),
            mutableListOf())

        val calculatedVal = post.getRating()
        val assertedVal = 2/3F * 5
        val allowableDifference = 0.000001F

        assertEquals(assertedVal, calculatedVal, allowableDifference)
    }
}