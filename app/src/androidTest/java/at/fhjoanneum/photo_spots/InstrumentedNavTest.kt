package at.fhjoanneum.photo_spots

import android.content.Intent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedNavTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun dashboard_clickingAnItem_opensDetail() {
        onView(withId(R.id.navigation_dashboard)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.dashboard_recyclerview))
            .perform(
                RecyclerViewActions.actionOnItem<PostViewHolder>(hasDescendant(withText("Numpad")), click())
            )
        intended(hasComponent(ViewLocationActivity::class.java.name))

        Thread.sleep(2000)
        onView(withId(R.id.viewloc_text_title))
            .check(
                matches(withText("Numpad"))
            )
        onView(withId(R.id.viewloc_button_share))
            .perform(click())
        intended(hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun camera_createNewPost() {
        onView(withId(R.id.navigation_Camera)).perform(click())
        Thread.sleep(2000)
        intended(hasComponent(CameraActivity::class.java.name))

        onView(withId(R.id.camera_button_camera)).perform(click())
        intended(hasComponent(PostPictureActivity::class.java.name))
    }

    /*
    @Test
    fun checkHome() {
        Thread.sleep(2000)
        // doesn't work because ActionLabel does not expose its text via getText()
        onView(withText("My Photo-Spots")).check(matches(isDisplayed()))
    }
    */

    @Test
    fun checkHeaderMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000)
        onView(withText("Change Password"))
            .check(matches(isDisplayed()))
        onView(withText("Logout"))
            .check(matches(isDisplayed()))
        onView(withText("Settings"))
            .check(matches(isDisplayed()))
            .perform(click())
        Thread.sleep(1000)
        intended(hasComponent(SettingsActivity::class.java.name))
    }
}