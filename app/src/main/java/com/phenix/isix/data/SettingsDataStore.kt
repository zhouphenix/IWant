package com.phenix.isix.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.phenix.isix.BuildConfig

public val Context.dataStore by preferencesDataStore(name = BuildConfig.DATA_STORE_NAME)

object SettingsDataStore {

}