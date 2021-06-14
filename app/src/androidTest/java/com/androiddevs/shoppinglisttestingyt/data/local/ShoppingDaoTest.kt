package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.shoppingDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun CRUD_InsertItem_ReturnsExist() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun CRUD_DeleteItem_ReturnsDoesntExist() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun CRUD_ObserveTotalPrice_ReturnSumCorrectly() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 1, 1f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 2, 2f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 3, 3f, "url", id = 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val shoppingITemPrices = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(shoppingITemPrices).isEqualTo(1*1f + 2*2f + 3*3f)
    }

    @Test
    fun CRUD_ObserveCertainShoppingItem_ReturnItemCorrectly() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 1, 1f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 2, 2f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 3, 3f, "url", id = 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val certainShoppingItem = dao.getShoppingItemWithId(2).getOrAwaitValue()

        assertThat(certainShoppingItem).isEqualTo(shoppingItem2)

    }
}