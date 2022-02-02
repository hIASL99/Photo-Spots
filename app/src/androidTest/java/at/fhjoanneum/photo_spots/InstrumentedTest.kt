package at.fhjoanneum.photo_spots
import androidx.fragment.app.testing.launchFragmentInContainer
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
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment
import at.fhjoanneum.photo_spots.ui.home.HomeFragment
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
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
    fun clickingAnItem_opensDetail() {

        val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
        )

        //val mockNavController = mock(NavController::class.java)

        val dashboardScenario = launchFragmentInContainer<DashboardFragment>()
        Thread.sleep(2000)
        dashboardScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        onView(withId(R.id.dashboard_recyclerview))
            .perform(
            RecyclerViewActions.actionOnItem<PostViewHolder>(hasDescendant(withText("Drucker")), click())
        )
        intended(hasComponent(ViewLocationActivity::class.java.name))
    }
}