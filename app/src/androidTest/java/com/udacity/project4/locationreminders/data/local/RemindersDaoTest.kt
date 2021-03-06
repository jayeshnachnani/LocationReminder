package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    //    TODO: Add testing implementation to the RemindersDao.kt
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RemindersDatabase::class.java).allowMainThreadQueries().build()

    }

    @Test
    fun insertRemindersAndGetReminderById() = runBlockingTest {
        // GIVEN - Insert a reminder.
        val reminder = ReminderDTO("testtitle", "testdescription", "testlocation", 18.3, 23.3)
        database.reminderDao().saveReminder(reminder)

        // WHEN - Get the reminder by id from the database.
        val loaded = database.reminderDao().getReminderById(reminder.id)


        // THEN - The loaded data contains the expected values.
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
        assertThat(loaded.location, `is`(reminder.location))
        assertThat(loaded.latitude, `is`(reminder.latitude))
        assertThat(loaded.longitude, `is`(reminder.longitude))


    }

    @Test
    fun insertRemindersAndGetRemindersCount() = runBlockingTest {
        // GIVEN - Insert 2 reminders
        val reminder1 = ReminderDTO("testtitle", "testdescription", "testlocation", 18.3, 23.3)
        database.reminderDao().saveReminder(reminder1)
        val reminder2 = ReminderDTO("testtitle2", "testdescription2", "testlocation2", 19.3, 24.3)
        database.reminderDao().saveReminder(reminder2)

        // WHEN - Get the reminders
        val loadedlist = database.reminderDao().getReminders()

        // THEN - The reminders count is 2
        assertThat(loadedlist.count(), `is`(2))

    }


    @After
    fun closeDb() = runBlockingTest {
        database.reminderDao().deleteAllReminders()
        database.close()
    }

}