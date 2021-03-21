package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private val reminder1 = ReminderDTO("testtitle1", "testdescription1", "testlocation1", 18.3,23.3)
    //private val task2 = Task("Title2", "Description2")
    private val reminder2 = ReminderDTO("testtitle2", "testdescription2", "testlocation2", 18.3,23.3)
    //private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localReminders = listOf(reminder1).sortedBy { it.id }
    private val newReminders = Result.Success(listOf(reminder1))
    private val x = Result.Success(localReminders)
    //private val newReminders1 = Result<ReminderDTO>
    private lateinit var remindersLocalDataSource: FakeDataSource

    //private lateinit var remindersRepository: RemindersLocalRepository


    @Before
    fun createDataSource() = runBlockingTest {
        //tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        remindersLocalDataSource = FakeDataSource(localReminders.toMutableList())

        //remindersLocalDataSource.saveReminder(reminder1)
        // Get a reference to the class under test
        /*remindersRepository = RemindersLocalRepository(
                // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
                //  this requires understanding more about coroutines + testing
                //  so we will keep this as Unconfined for now.
                 remindersLocalDataSource, Dispatchers.Unconfined
        )*/
    }

    @Test
    fun getReminders() = runBlockingTest {

        //GIVEN: A reminder
        remindersLocalDataSource.saveReminder(reminder1)

        // WHEN - Get the reminders
        val loadedlist = remindersLocalDataSource.getReminders()
        val y = Result.Success(loadedlist)
        // THEN - The reminders count is 1
        //assertThat(y,`is`(x))

    }

    fun cleanupDataSource() = runBlockingTest {
        remindersLocalDataSource.deleteAllReminders()

    }

}