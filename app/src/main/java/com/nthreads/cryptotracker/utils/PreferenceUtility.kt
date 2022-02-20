package com.nthreads.cryptotracker.utils

import android.content.Context

class PreferenceUtility {
    companion object {

        /*
     * Function for getting a value from shared preferences
     */

        fun getPreference(
            context: Context,
            prefName: String, key: String
        ): String {

            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getString(key, "") ?: ""
        }

        fun getPreference(
            context: Context,
            prefName: String,
            key: String,
            defValue: String
        ): String? {
            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getString(key, defValue)
        }

        fun getMutableSetPreference(
            context: Context,
            prefName: String, key: String
        ): MutableSet<String>? {

            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getStringSet(key, emptySet())
        }


        /*
         * Function for setting values for shared preferences
         */

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: String
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: MutableSet<String>
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putStringSet(key, value)
            editor.apply()
        }

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: Long
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun setPreference(
            base: Context, prefName: String,
            key: String, value: Boolean
        ) {

            val userPref = base.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun getBoolPreference(
            base: Context,
            prefName: String, key: String
        ): Boolean {

            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getBoolean(key, false)
        }

        fun getBoolPreference(
            base: Context,
            prefName: String, key: String, defVal: Boolean
        ): Boolean {

            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getBoolean(key, defVal)
        }

        fun setPreference(
            base: Context, prefName: String,
            key: String, value: Int
        ) {

            val userPref = base.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getIntPreference(
            base: Context, prefName: String,
            key: String
        ): Int {

            val usePref = base.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            return usePref.getInt(key, 0)
        }

        fun getLongPreference(base: Context, prefName: String, key: String): Long {
            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getLong(key, 0)
        }

        fun getLongPreference(base: Context, prefName: String, key: String, defValue: Long): Long {
            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getLong(key, defValue)
        }

        fun removePreferences(base: Context, prefName: String, key: String) {

            val userPref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = userPref.edit()
            editor.remove(key).apply()
        }

        fun removeAllPreferences(base: Context, prefName: String) {

            val userPref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = userPref.edit()
            editor.clear()
            editor.apply()
        }
    }
}
