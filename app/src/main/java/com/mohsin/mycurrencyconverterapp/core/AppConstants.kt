package com.mohsin.mycurrencyconverterapp.core

import com.mohsin.mycurrencyconverterapp.BuildConfig

object AppConstants {
    const val FLOW_RETAINED_TIME : Long =  6_000
    const val GRID_COUNT = 3
    const val UPDATE_INTERVAL = 30 * 60 * 1000 //30 minutes
    const val DB_NAME = "currency-converter-db"
    const val DEFAULT_BASE_CURRENCY = "USD"
    const val LAST_UPDATE_KEY = "last_update_rate"
    const val LATEST_BASE_KEY = "latest_base_exchange"
    const val PREFERENCE_DATASTORE_KEY = "time_tracker_data_store"


    // Reading from env.secrets.json to enable the use of environment variables for future production builds.
    const val BASE_URL = BuildConfig.BASE_URL
    const val APP_ID = BuildConfig.APP_ID
}