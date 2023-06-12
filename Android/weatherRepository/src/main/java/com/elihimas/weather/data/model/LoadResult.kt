package com.elihimas.weather.data.model

sealed class LoadResult<out T> {

    class SuccessResult<T>(val resultData: T) : LoadResult<T>()
    object NotFoundResult : LoadResult<Nothing>()
}