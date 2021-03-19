package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest1 {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt
    private val reminder1 = ReminderDTO("testtitle1", "testdescription1", "testlocation1", 18.3,23.3)
    //private val task2 = Task("Title2", "Description2")
    private val reminder2 = ReminderDTO("testtitle2", "testdescription2", "testlocation2", 18.3,23.3)
    //private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localReminders = listOf(reminder1).sortedBy { it.id }
    private val newTasks = listOf(reminder2).sortedBy { it.id }

    //private lateinit var tasksRemoteDataSource: FakeDataSource
    //private lateinit var remindersLocalDataSource: FakeDataSource

}