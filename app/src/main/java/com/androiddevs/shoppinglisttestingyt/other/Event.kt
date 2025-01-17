package com.androiddevs.shoppinglisttestingyt.other

open class Event<out T> (private val content: T){

    var hasBeenHandled = false
    private set // Allow external read but not write

    /**
     * Returns the content and prevent its use again
     */
    fun getContentIfNotHandled(): T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}