package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import org.junit.Before
import org.junit.Test

class RemindersLocalRepositoryTest {


    private val reminder1 = ReminderDTO("testtitle1", "testdescription1", "testlocation1", 18.3, 23.3)

    //private val task2 = Task("Title2", "Description2")
    private val reminder2 = ReminderDTO("testtitle2", "testdescription2", "testlocation2", 18.3, 23.3)

    //private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localReminders = listOf(reminder1).sortedBy { it.id }
    private val newTasks = listOf(reminder2).sortedBy { it.id }

    //private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var remindersLocalDataSource: FakeDataSource
    private lateinit var remindersRepository: RemindersLocalRepository


    @Before
    fun createRepository() {
        //tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        remindersLocalDataSource = FakeDataSource(localReminders.toMutableList())
        // Get a reference to the class under test
        /*remindersRepository = RemindersLocalRepository(
                // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
                //  this requires understanding more about coroutines + testing
                //  so we will keep this as Unconfined for now.
                 remindersLocalDataSource, Dispatchers.Unconfined
        )*/
    }

    @Test
    fun getReminders() {
    }

    @Test
    fun saveReminder() {
    }

    @Test
    fun getReminder() {
    }

    @Test
    fun deleteAllReminders() {
    }
}