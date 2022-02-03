package at.fhjoanneum.photo_spots

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InstrumentedCamTest {
    @get:Rule
    val rule = ActivityScenarioRule(CameraActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun retakePicture() {
        onView(ViewMatchers.withId(R.id.camera_button_camera)).perform(click())
        intended(IntentMatchers.hasComponent(PostPictureActivity::class.java.name))
        Thread.sleep(2000)
        closeSoftKeyboard()

        onView(ViewMatchers.withId(R.id.viewpic_button_retake))
            .perform(ViewActions.scrollTo(), click())
        Thread.sleep(2000)
        intended(IntentMatchers.hasComponent(CameraActivity::class.java.name))
    }
}