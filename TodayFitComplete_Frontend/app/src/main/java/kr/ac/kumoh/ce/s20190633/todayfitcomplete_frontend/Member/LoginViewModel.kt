package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberLoginDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberTokenResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<MemberTokenResponse?>()
    val loginResult: LiveData<MemberTokenResponse?>
        get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            val loginRequest = MemberLoginDto(email, password)

            try {
                val response = apiService.login(loginRequest).execute()
                if (response.isSuccessful) {
                    _loginResult.postValue(response.body())
                } else {
                    _loginResult.postValue(null)
                }
            } catch (e: Exception) {
                _loginResult.postValue(null)
            }
        }
    }
}
