package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.android.dx.util.Bits.clear
import com.udacity.project4.R
//import com.udacity.project4.locationreminders.data.FakeDataSource

import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {



//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.

    private val reminder1 = ReminderDTO("testtitle1", "testdescription1", "testlocation1", 18.3,23.3)
    private val reminder2 = ReminderDTO("testtitle2", "testdescription2", "testlocation2", 18.3,23.3)
    private val localReminders:MutableList<ReminderDTO> = mutableListOf(reminder1)
    private val newReminders = Result.Success(listOf(reminder1))
    private val x = Result.Success(localReminders)
    //private val newReminders1 = Result<ReminderDTO>
    private lateinit var remindersLocalDataSource: FakeDataSource

    @Before
    fun createDataSource() = runBlockingTest {
        //tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        remindersLocalDataSource = FakeDataSource(localReminders.toMutableList())

        remindersLocalDataSource.saveReminder(reminder1)
        // Get a reference to the class under test
        /*remindersRepository = RemindersLocalRepository(
                // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
                //  this requires understanding more about coroutines + testing
                //  so we will keep this as Unconfined for now.
                 remindersLocalDataSource, Dispatchers.Unconfined
        )*/
    }

    @After
    fun cleanupDataSource() = runBlockingTest {
        remindersLocalDataSource.deleteAllReminders()
        localReminders.removeAll(localReminders)


    }

    @Test
    fun activeTaskDetails_DisplayedInUi() = runBlockingTest{
        // GIVEN - Add active (incomplete) task to the DB
        remindersLocalDataSource.saveReminder(reminder1)



        // WHEN - Details fragment launched to display task
        launchFragmentInContainer<ReminderListFragment>(null,R.style.AppTheme)


        // THEN - Task details are displayed on the screen
        // make sure that the title/description are both shown and correct
        onView(withId(R.id.reminderTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.reminderTitle)).check(matches(withText("testtitle1")))
        onView(withId(R.id.reminderDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.reminderDescription)).check(matches(withText("testdescription1")))
        // and make sure the "active" checkbox is shown unchecked

    }

    @Test
    fun clickAddReminder_navigateTo_AddReminderFragment() {
        // GIVEN - On the Reminder List screen
        val fragmentScenario = launchFragmentInContainer<ReminderListFragment>(null, R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        fragmentScenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN -  Add is clicked
        Espresso.onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())

        // THEN - navigate to the add screen
        Mockito.verify(navController).navigate(
                ReminderListFragmentDirections.toSaveReminder()
        )
    }
}