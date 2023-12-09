package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend

import android.content.Context

// SharedPreferences를 사용하여 데이터를 저장하고 검색하는 유틸리티 클래스
object SharedPreferencesUtils {
    // SharedPreferences 파일에 대한 고유한 키
    private const val PREFERENCES_FILE_KEY = "kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.preferences"

    // 사용자 토큰을 저장하기 위한 키
    private const val TOKEN_KEY = "token"

    // 사용자 이메일을 저장하기 위한 키
    private const val EMAIL_KEY = "email"

    // 사용자 토큰을 저장하는 함수
    fun saveToken(context: Context, token: String) {
        // SharedPreferences 파일을 가져옴
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        // SharedPreferences 편집기를 얻어 토큰을 저장
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply() // 변경사항을 즉시 저장
        }
    }

    // 사용자 토큰을 검색하는 함수
    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        // 저장된 토큰을 반환하며, 없으면 null을 반환
        return sharedPref.getString(TOKEN_KEY, null)
    }

    // 사용자 이메일을 저장하는 함수
    fun saveEmail(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        // SharedPreferences 편집기를 얻어 이메일을 저장
        with(sharedPreferences.edit()) {
            putString(EMAIL_KEY, email)
            apply() // 변경사항을 즉시 저장
        }
    }

    // 사용자 이메일을 검색하는 함수
    fun getEmail(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        // 저장된 이메일을 반환하며, 없으면 null을 반환
        return sharedPreferences.getString(EMAIL_KEY, null)
    }
}
