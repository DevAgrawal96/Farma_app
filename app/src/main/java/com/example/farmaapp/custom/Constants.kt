package com.example.farmaapp.custom

object Constants {
    const val BASE_URL = "https://api.open-meteo.com/v1/"
    const val MARKET_RATE_WEB_URL = "https://vegetablemarketprice.com/"
    const val MARKET_RATE_WEB_HIDE_CLASS_USING_ELEMENT_URL = "javascript:(function() { " +
            "document.getElementsByClassName('container-fluid')[0].style.display='none';" +
            "document.getElementsByClassName('col-sm-12')[0].style.display='none'; })() "

    const val MARKET_RATE_WEB_HIDE_BY_ID_USING_ELEMENT_URL = "javascript:(function() { " +
            "document.getElementById('copywriteSpanTagFooter').style.display='none';" +
            "document.getElementById('versionOfAppSpan').style.display='none';})()"



    const val WEATHER_END_POINT = "forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relativehumidity_2m,weathercode,is_day&current_weather=true"
    const val NEWS_BASE_URL = "https://newsapi.ai/api/v1/article/"
    const val NEWS_END_POINT = "getArticles?query=%7B%22%24query%22%3A%7B%22%24and%22%3A%5B%7B%22%24and%22%3A%5B%7B%22conceptUri%22%3A%22http%3A%2F%2Fen.wikipedia.org%2Fwiki%2FAgriculture%22%7D%2C%7B%22conceptUri%22%3A%22http%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFarmer%22%7D%5D%7D%2C%7B%22lang%22%3A%22hin%22%7D%5D%7D%2C%22%24filter%22%3A%7B%22forceMaxDataTimeWindow%22%3A%2231%22%7D%7D&resultType=articles&articlesSortBy=date&articlesCount=100&articleBodyLen=-1&apiKey=b0424d12-dd6f-45c5-bdf4-cfa8420bf4c9"
    const val BANNER_ONE_URL = "https://thefoodtech.com/wp-content/uploads/2022/02/La-biotecnologi%CC%81a-en-la-agricultura-828x548.jpg"
    const val BANNER_TWO_URL = "https://www.fundacionaquae.org/wp-content/uploads/2021/04/agricultura-sostenible.jpg"
    const val HOURLY_DATA_KEY = "today_weather"
    const val LOCATION_KEY = "location"
    const val NEWS_ARR = "newsArr"
    const val NEWS_POSITION = "position"
    const val EXIT = "exit"
    const val WEATHER_DATABASE_NAME = "weatherInformation"
    const val NEWS_DATABASE_NAME = "NewsInformation"
    const val CHECK_INTERNET_TOAST_MSG = "Check your internet Connection and try again"
    const val CHART_ANIMATION_DURATION = 1000L
    const val NEWS_TABLE_INDEX = "02"
    const val WEATHER_TABLE_INDEX = "1"
    const val CHAR_PATTERN_OUT = "hh a"
    const val CHAR_PATTERN_IN = "hh:mm"
    const val FORECAST_ITEM_POSITION = "position"
    const val FORECAST_ITEM_POSITION_ARR = "arr"

    const val DATE_PATTER_OUT = "EEEE,dd MMM"
    const val DATE_PATTER_IN = "yyyy/mm/dd"

    const val NEWS_CHAR_PATTERN_OUT = "hh:mm a"
    const val NEWS_CHAR_PATTERN_IN = "hh:mm"
    const val LOCATION_REQUEST_CODE = 1001
    const val PUNE_LON = 73.856255
    const val PUNE_LATITUDE = 18.516726
    const val UTC = "UTC"
    const val T = "T"
    const val MM = "MM"
    const val COLON = ":"
    const val HYPHEN = "-"
    const val COMMA = ","
    const val SPACE = " "
    const val DAY_24 = 24

    const val JANUARY = "January"
    const val FEBRUARY = "February"
    const val MARCH = "March"
    const val APRIL = "April"
    const val MAY = "May"
    const val JUNE = "June"
    const val JULY = "July"
    const val AUGUST = "August"
    const val SEPTEMBER = "September"
    const val OCTOBER = "October"
    const val NOVEMBER = "November"
    const val DECEMBER = "December"

    const val CLEAR_SKY = "Clear sky"
    const val MAINLY_CLEAR = "Mainly clear"
    const val PARTLY_CLOUDY = "Partly cloudy"
    const val CLOUDY = "Cloudy"
    const val FOG = "Fog"
    const val DRIZZLE_LIGHT = "Drizzle: Light"
    const val DRIZZLE_MODERATE = "Drizzle: moderate"
    const val DRIZZLE_DENSE_INTENSITY = "Drizzle: dense intensity"
    const val FREEZING_DRIZZLE_LIGHT = "Freezing Drizzle: Light"
    const val FREEZING_DRIZZLE_DENSE_INTENSITY = "Freezing Drizzle: dense intensity"
    const val RAIN_SLIGHT = "Rain: Slight"
    const val RAIN_MODERATE = "Rain: moderate"
    const val RAIN_HEAVY_INTENSITY = "Rain: heavy intensity"
    const val FREEZING_RAIN_LIGHT = "Freezing Rain: Light"
    const val FREEZING_RAIN_HEAVY_INTENSITY = "Freezing Rain: heavy intensity"
    const val SNOW_FALL_SLIGHT = "Snow fall: Slight"
    const val SNOW_FALL_MODERATE = "Snow fall: moderate"
    const val SNOW_FALL_HEAVY_INTENSITY = "Snow fall: heavy intensity"
    const val SNOW_GRAINS = "Snow grains"
    const val RAIN_SHOWERS_SLIGHT = "Rain showers: Slight"
    const val RAIN_SHOWERS_MODERATE = "Rain showers: moderate"
    const val RAIN_SHOWERS_VIOLENT = "Rain showers: violent"
    const val SNOW_SHOWERS_SLIGHT = "Snow showers slight"
    const val SNOW_SHOWERS_HEAVY = "Snow showers heavy"
    const val THUNDERSTORM_SLIGHT_OR_MODERATE = "Thunderstorm: Slight or moderate"
    const val THUNDERSTORM_WITH_SLIGHT = "Thunderstorm with slight"
    const val THUNDERSTORM_WITH_HEAVY_HAIL = "Thunderstorm with heavy hail"
}