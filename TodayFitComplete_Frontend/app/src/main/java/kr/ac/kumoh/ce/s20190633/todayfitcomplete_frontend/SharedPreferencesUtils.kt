package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend
import android.content.Context

object SharedPreferencesUtils {
    private const val PREFERENCES_FILE_KEY = "kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.preferences"
    private const val TOKEN_KEY = "token"

    fun saveToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, null)
    }
}
