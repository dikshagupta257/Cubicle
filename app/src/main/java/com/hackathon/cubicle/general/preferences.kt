package com.hackathon.cubicle.general

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class preferences {
    companion object {
        val DATA_LOGIN = "status_login"
        val DATA_AS = "as"
        val USER_ID = "UserID"

        fun getSharedPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        fun getDataLogin(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(DATA_LOGIN, false)
        }

        fun setDataLogin(context: Context, status: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(DATA_LOGIN, status)
            editor.apply()

        }

        fun getDataAs(context: Context): String? {
            return getSharedPreferences(context).getString(DATA_AS, "")
        }

        fun setDataAs(context: Context, data: String?) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(DATA_AS, data)
            editor.apply()
        }

        fun getUserID(context: Context?): String? {
            return getSharedPreferences(context!!).getString(USER_ID, "")
        }

        fun setUserID(context: Context?, id: String?) {
            val editor = getSharedPreferences(context!!).edit()
            editor.putString(USER_ID, id)
            editor.apply()
        }

        fun clearData(context: Context) {
            val editor = getSharedPreferences(context).edit()
            editor.remove(DATA_AS)
            editor.remove(DATA_LOGIN)
            editor.apply()
        }
    }
}