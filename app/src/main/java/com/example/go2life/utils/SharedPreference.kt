package com.example.go2life.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference private constructor() {
    private var mEditor: SharedPreferences.Editor? = null

    companion object {
        private const val SHARED_PREF = "Go2Life" //change app name here...
        lateinit var sharedPreferences: SharedPreferences
        fun getInstance(context: Context): SharedPreference {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return SharedPreference()
        }
    }

    enum class Key {
        OTP, EMAILOTP,COUNTRY, LANGUAGE, LATITUDE, LONGITUDE, ADDRESS, PASSWORD, TRADESMENNAME, TOKEN, ISUSERLOGIN, FIRSTNAME, LASTNAME, PHONE, POSTCODE, USERDETIALS, GENDER, DOB, INJURIES, USERID, USERTYPE, ISLANDINGCOMPLETE, ISTRADESMENLANDINGCOMPLETE, DEVICETOKEN,ISGREEN,ISRED,ISGREY, USERNAME, EMAIL, Address, PROFILEIMAGE, TOTALCREDIT, ISCARDNOTEMPTY
    }

    fun putString(key: Key, value: String) {
        doEdit()
        mEditor?.putString(key.name, value)
        doApply()

    }

    fun putInt(key: Key, value: Int) {
        doEdit()
        mEditor?.putInt(key.name, value)
        doApply()

    }

    fun putBoolean(key: Key, value: Boolean) {
        doEdit()
        mEditor?.putBoolean(key.name, value)
        doApply()
    }

    fun getString(key: Key, valueDefault: String = ""): String? {
        return sharedPreferences.getString(key.name, valueDefault)
    }

    fun getInt(key: Key, valueDefault: Int = 0): Int  {
        return sharedPreferences.getInt(key.name, valueDefault)
    }

    fun getBoolean(key: Key, valueDefault: Boolean = false ): Boolean  {
        return sharedPreferences.getBoolean(key.name, valueDefault)
    }


    private fun doApply() {
        if (mEditor != null) {
            mEditor!!.apply()
        }else{
            mEditor=null
        }
    }

    private fun doEdit() {
        if (mEditor == null) {
            mEditor = sharedPreferences.edit()
        }
    }

    private fun clear() {
        doEdit()
        mEditor?.clear()
        doApply()
    }

}