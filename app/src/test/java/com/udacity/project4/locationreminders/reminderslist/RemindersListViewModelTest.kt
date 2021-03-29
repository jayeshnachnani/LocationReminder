package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private val reminder1 = ReminderDTO("testtitle1", "testdescription1", "testlocation1", 18.3, 23.3)
    private val reminder2 = ReminderDTO("testtitle2", "testdescription2", "testlocation2", 18.3, 23.3)
    val reminderList = mutableListOf<ReminderDTO>()
    private lateinit var remindersLocalDataSource: FakeDataSource

    @Before
    fun createDataSource() = runBlockingTest {
        remindersLocalDataSource = FakeDataSource(reminderList)

    }

    @Test
    fun getReminders() = runBlockingTest {

        //GIVEN: 2 reminders
        remindersLocalDataSource.saveReminder(reminder1)
        remindersLocalDataSource.saveReminder(reminder2)

        // WHEN - Get the reminders
        val loadedlist = remindersLocalDataSource.getReminders()

        // THEN - The reminders count is 2
        assertThat((loadedlist as Result.Success).data.size, `is`(2))

    }

    @Test
    fun ErrorInGetReminders() = runBlockingTest {

        //GIVEN: Error in reminders
        remindersLocalDataSource.setReturnError(true)
        remindersLocalDataSource.saveReminder(reminder1)
        remindersLocalDataSource.saveReminder(reminder2)

        // WHEN - Get the reminders
        val loadedlist = remindersLocalDataSource.getReminders()

        // THEN - Error is returned
        assertThat((loadedlist as Result.Error),`is`(Result.Error("No tasks")))

    }
    @After
    fun cleanupDataSource() = runBlockingTest {
        stopKoin()
        remindersLocalDataSource.deleteAllReminders()

    }

}