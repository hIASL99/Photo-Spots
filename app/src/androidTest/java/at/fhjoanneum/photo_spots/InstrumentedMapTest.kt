package at.fhjoanneum.photo_spots
import android.graphics.Point
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.android.gms.maps.GoogleMap
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedMapTest {


    @get:Rule
    val rule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun clickingAnItem_opensDetail() {

        //Thread.sleep(5000)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.dashboard_recyclerview))
            .perform(
                RecyclerViewActions.actionOnItem<PostViewHolder>(hasDescendant(withText("Rainbow Building")), click())
            )
        intended(hasComponent(ViewLocationActivity::class.java.name))
        Thread.sleep(1000)
        onView(withId(R.id.viewloc_button_like))
            .perform(click())
    }

    @Test
    fun mapToCamera() {
        onView(withId(R.id.navigation_map)).perform(click())
        Thread.sleep(2000)
        val device = UiDevice.getInstance(getInstrumentation())
        val marker = device.findObject(UiSelector().descriptionContains("Your current location"))
        marker.clickAndWaitForNewWindow()
        val display = rule.activity.windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val screenWidth = size.x
        val screenHeight = size.y
        val x = screenWidth / 2
        val y = (screenHeight * 0.43).toInt()
        //Thread.sleep(1000)
        device.click(x, y)
        Thread.sleep(1000)
        intended(hasComponent(CameraActivity::class.java.name))

    }
}