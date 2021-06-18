package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.other.Constants
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import getOrAwaitValueTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.res.android.ResourceString.buildString

class ShoppingViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun INSERT_ShoppingItemWithEmptyField_ReturnsError(){
        viewModel.insertShoppingItem("name", "", "20.5")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun INSERT_ShoppingItemWithTooLongName_ReturnsError(){
        viewModel.insertShoppingItem("name is longer than max length", "5", "20.5")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun INSERT_ShoppingItemWithTooLongPrice_ReturnsError(){
        viewModel.insertShoppingItem("name", "5", "20.5010101010101010101010")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun INSERT_ShoppingItemWithInvalidAmount_ReturnsError(){
        viewModel.insertShoppingItem("name", "999999999999999", "20")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun INSERT_ShoppingItemWithValidInput_ReturnsSuccess(){
        viewModel.insertShoppingItem("name", "10", "20")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


}