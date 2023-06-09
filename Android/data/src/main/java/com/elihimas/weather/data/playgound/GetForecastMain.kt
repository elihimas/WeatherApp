package com.elihimas.weather.data.playgound

import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.repository.remote.APIBuilder
import com.elihimas.weather.data.repository.remote.model.GetForecastResponse
import com.elihimas.weather.data.repository.remote.model.RemoteForecastData
import com.elihimas.weather.data.repository.remote.model.RemoteForecastItem
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun main() = runBlocking {
    println("Get forecast example")
    println()

    val forecastResponse = getForecast()

    showFullForecast(forecastResponse)

    val mappedItems = mapItems(forecastResponse)

    showGroupedItems(mappedItems)

    showAllItems(mappedItems)
}

private suspend fun getForecast(): GetForecastResponse {
    val api = APIBuilder.createAPI()
    val city = "Recife"

    return api.getForecast(city)
}

private fun showFullForecast(forecast: GetForecastResponse) {
    println("Full forecast:")
    println(forecast)
    println()
}

private fun mapItems(
    forecastResponse: GetForecastResponse,
): List<ForecastItem> {
    val offset = forecastResponse.city.localSecondsOffset

    return forecastResponse.forecastList.map { remoteForecastItem ->
        val date = LocalDateTime.ofEpochSecond(
            remoteForecastItem.dateTime, 0,
            ZoneOffset.ofTotalSeconds(offset)
        )
        ForecastItem(
            remoteForecastItem.forecastData.temperature,
            date
        )
    }
}

private fun showGroupedItems(mappedItems: List<ForecastItem>) {
    println("Grouped items:")
    val groupedForecasts = mappedItems.groupBy { forecastItem ->
        forecastItem.date.dayOfMonth
    }
    groupedForecasts.toSortedMap { e1, e2 ->
        e1 - e2
    }.forEach { entry ->
        println(entry)
    }
    println()
}

private fun showAllItems(mappedItems: List<ForecastItem>) {
    println("All items:")
    val formatter = DateTimeFormatter.ofPattern("dd/MM hh:mm")
    mappedItems.forEach { forecastItem ->
        val dateStr = formatter.format(forecastItem.date)

        val temperature = forecastItem.temperature
        println("Date: ${forecastItem.date}")
        println("Formated date: $dateStr")
        println("Temp: $temperature")
        println()
    }
}

fun getFakeItems() =
    listOf(
        RemoteForecastItem(
            dateTime = 1686247200,
            forecastData = RemoteForecastData(temperature = 25.02)
        ),
        RemoteForecastItem(
            dateTime = 1686258000,
            forecastData = RemoteForecastData(temperature = 25.16)
        ),
        RemoteForecastItem(
            dateTime = 1686268800,
            forecastData = RemoteForecastData(temperature = 25.24)
        ),
        RemoteForecastItem(
            dateTime = 1686279600,
            forecastData = RemoteForecastData(temperature = 24.86)
        ),
        RemoteForecastItem(
            dateTime = 1686290400,
            forecastData = RemoteForecastData(temperature = 24.49)
        ),
        RemoteForecastItem(
            dateTime = 1686301200,
            forecastData = RemoteForecastData(temperature = 24.65)
        ),
        RemoteForecastItem(
            dateTime = 1686312000,
            forecastData = RemoteForecastData(temperature = 26.37)
        ),
        RemoteForecastItem(
            dateTime = 1686322800,
            forecastData = RemoteForecastData(temperature = 27.0)
        ),
        RemoteForecastItem(
            dateTime = 1686333600,
            forecastData = RemoteForecastData(temperature = 26.95)
        ),
        RemoteForecastItem(
            dateTime = 1686344400,
            forecastData = RemoteForecastData(temperature = 25.48)
        ),
        RemoteForecastItem(
            dateTime = 1686355200,
            forecastData = RemoteForecastData(temperature = 25.23)
        ),
        RemoteForecastItem(
            dateTime = 1686366000,
            forecastData = RemoteForecastData(temperature = 25.01)
        ),
        RemoteForecastItem(
            dateTime = 1686376800,
            forecastData = RemoteForecastData(temperature = 25.38)
        ),
        RemoteForecastItem(
            dateTime = 1686387600,
            forecastData = RemoteForecastData(temperature = 24.99)
        ),
        RemoteForecastItem(
            dateTime = 1686398400,
            forecastData = RemoteForecastData(temperature = 27.08)
        ),
        RemoteForecastItem(
            dateTime = 1686409200,
            forecastData = RemoteForecastData(temperature = 27.7)
        ),
        RemoteForecastItem(
            dateTime = 1686420000,
            forecastData = RemoteForecastData(temperature = 27.32)
        ),
        RemoteForecastItem(
            dateTime = 1686430800,
            forecastData = RemoteForecastData(temperature = 25.63)
        ),
        RemoteForecastItem(
            dateTime = 1686441600,
            forecastData = RemoteForecastData(temperature = 24.77)
        ),
        RemoteForecastItem(
            dateTime = 1686452400,
            forecastData = RemoteForecastData(temperature = 24.24)
        ),
        RemoteForecastItem(
            dateTime = 1686463200,
            forecastData = RemoteForecastData(temperature = 23.94)
        ),
        RemoteForecastItem(
            dateTime = 1686474000,
            forecastData = RemoteForecastData(temperature = 24.07)
        ),
        RemoteForecastItem(
            dateTime = 1686484800,
            forecastData = RemoteForecastData(temperature = 26.77)
        ),
        RemoteForecastItem(
            dateTime = 1686495600,
            forecastData = RemoteForecastData(temperature = 27.73)
        ),
        RemoteForecastItem(
            dateTime = 1686506400,
            forecastData = RemoteForecastData(temperature = 27.09)
        ),
        RemoteForecastItem(
            dateTime = 1686517200,
            forecastData = RemoteForecastData(temperature = 25.32)
        ),
        RemoteForecastItem(
            dateTime = 1686528000,
            forecastData = RemoteForecastData(temperature = 24.98)
        ),
        RemoteForecastItem(
            dateTime = 1686538800,
            forecastData = RemoteForecastData(temperature = 24.49)
        ),
        RemoteForecastItem(
            dateTime = 1686549600,
            forecastData = RemoteForecastData(temperature = 24.64)
        ),
        RemoteForecastItem(
            dateTime = 1686560400,
            forecastData = RemoteForecastData(temperature = 24.88)
        ),
        RemoteForecastItem(
            dateTime = 1686571200,
            forecastData = RemoteForecastData(temperature = 27.46)
        ),
        RemoteForecastItem(
            dateTime = 1686582000,
            forecastData = RemoteForecastData(temperature = 28.07)
        ),
        RemoteForecastItem(
            dateTime = 1686592800,
            forecastData = RemoteForecastData(temperature = 27.15)
        ),
        RemoteForecastItem(
            dateTime = 1686603600,
            forecastData = RemoteForecastData(temperature = 25.48)
        ),
        RemoteForecastItem(
            dateTime = 1686614400,
            forecastData = RemoteForecastData(temperature = 24.82)
        ),
        RemoteForecastItem(
            dateTime = 1686625200,
            forecastData = RemoteForecastData(temperature = 25.1)
        ),
        RemoteForecastItem(
            dateTime = 1686636000,
            forecastData = RemoteForecastData(temperature = 25.12)
        ),
        RemoteForecastItem(
            dateTime = 1686646800,
            forecastData = RemoteForecastData(temperature = 25.31)
        ),
        RemoteForecastItem(
            dateTime = 1686657600,
            forecastData = RemoteForecastData(temperature = 27.55)
        ),
        RemoteForecastItem(
            dateTime = 1686668400,
            forecastData = RemoteForecastData(temperature = 28.15)
        )
    )