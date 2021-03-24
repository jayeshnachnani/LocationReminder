package com.udacity.project4.locationreminders.reminderslist

//import com.udacity.project4.locationreminders.data.FakeDataSource

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {
    private val reminder1 = ReminderDTO("testtitle1", "testdescription1", "testlocation1", 18.3, 23.3)
    private val localReminders: MutableList<ReminderDTO> = mutableListOf(reminder1)

    @Test
    fun clickAddReminder_navigateTo_AddReminderFragment() {
        // GIVEN - Reminder List screen

        val fragmentScenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        fragmentScenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN -  Add is clicked
        onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())

        // THEN - navigate to the add screen
        verify(navController).navigate(
                ReminderListFragmentDirections.toSaveReminder()
        )
    }
}