package at.fhjoanneum.photo_spots

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// before executing this tests make sure that you are not logged in in the emulator

class InstrumentedLogScreenTest {
    @get:Rule
    val rule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun registerUser_navigationBack() {
        onView(withId(R.id.login_txt_register)).perform(click())
        intended(IntentMatchers.hasComponent(RegisterActivity::class.java.name))
        Thread.sleep(1000)

        val registeredButton = onView(
            Matchers.allOf(withContentDescription("Navigate up"), isDisplayed())
        )
        registeredButton.perform(click())
        Thread.sleep(1000)
        onView(withText("Login")).check(matches(isDisplayed()))
    }

    @Test
    fun loginSuccessfully_and_logout() {
        val user = "InstrumentedTests"
        val password = "MAPPDEVima20"

        Thread.sleep(2000)
        onView(withId(R.id.login_input_username)).perform(typeText(user))
        onView(withId(R.id.login_input_password)).perform(typeText(password))
        closeSoftKeyboard()
        onView(withId(R.id.login_btn_login)).perform(click())
        Thread.sleep(1000)
        intended(IntentMatchers.hasComponent(MainActivity::class.java.name))

        openActionBarOverflowOrOptionsMenu(
            getInstrumentation().getTargetContext()
        )

        onView(withText("Logout"))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Login")).check(matches(isDisplayed()))
    }
}