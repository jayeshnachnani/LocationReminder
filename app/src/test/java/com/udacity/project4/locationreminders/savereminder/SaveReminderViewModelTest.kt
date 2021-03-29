package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SaveReminderViewModelTest {
    private lateinit var remindersLocalDataSource: FakeDataSource
    val reminder1 = ReminderDataItem("testtitle3", "testdescription3", "Location3", 20.0, 25.0, "testID")
    val reminderList = mutableListOf<ReminderDTO>()
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createDataSource() = runBlockingTest {
        remindersLocalDataSource = FakeDataSource(reminderList)
        saveReminderViewModel =
                SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersLocalDataSource)

    }

    @After
    fun cleanupDataSource() = runBlockingTest {
        stopKoin()
        remindersLocalDataSource.deleteAllReminders()

    }

    @Test
    fun saveReminder_loading() {
        mainCoroutineRule.pauseDispatcher()
        //Given: Reminder

        //When reminder is saved
        saveReminderViewModel.saveReminder(reminder1)

        //Then progress is initially shown
        MatcherAssert.assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), CoreMatchers.`is`(true))

        mainCoroutineRule.resumeDispatcher()

        //And later not shown

        MatcherAssert.assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), CoreMatchers.`is`(false))
        MatcherAssert.assertThat(saveReminderViewModel.showToast.getOrAwaitValue(), CoreMatchers.`is`("Reminder Saved !"))

    }

}