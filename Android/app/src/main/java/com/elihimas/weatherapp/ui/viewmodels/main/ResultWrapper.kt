package com.elihimas.weatherapp.ui.viewmodels.main

enum class ResultStatus { Success, RetryError, FinalError, Loading }

open class ResultWrapper(val status: ResultStatus)
