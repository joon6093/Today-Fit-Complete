package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.MemberApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberLoginDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberRegisterDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MemberViewModel : ViewModel() {
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val memberApiService: MemberApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        memberApiService = retrofit.create(MemberApiService::class.java)
    }

    //회원가입
    private val _registrationStatus = MutableLiveData<String?>()
    val registrationStatus: LiveData<String?> = _registrationStatus
    fun register(memberRegisterDto: MemberRegisterDto) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    memberApiService.register(memberRegisterDto).execute()
                }
                if (response.isSuccessful) {
                    _registrationStatus.postValue("회원가입에 성공했습니다.")
                } else {
                    _registrationStatus.postValue("회원가입에 실패했습니다.")
                    Log.e("MemberViewModel", "Registration failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MemberViewModel", "Error on member registration: ${e.message}")
                _registrationStatus.postValue("오류 발생: ${e.message}")
            }
        }
    }

    // 이메일 중복 검사
    fun checkEmailDuplicate(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            memberApiService.checkIdDuplicate(email).enqueue(object : retrofit2.Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        onResult(response.body() ?: false)
                    } else {
                        onResult(false)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("MemberViewModel", "Error checking email duplicate: ${t.message}")
                    onResult(false)
                }
            })
        }
    }

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val memberLoginDto = MemberLoginDto(email, password)
                val response = withContext(Dispatchers.IO) {
                    memberApiService.login(memberLoginDto).execute()
                }
                if (response.isSuccessful) {
                    // 로그인 성공 처리
                    _isLoggedIn.postValue(true)
                } else {
                    // 로그인 실패 처리
                    _isLoggedIn.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("MemberViewModel", "Error on member login: ${e.message}")
                _isLoggedIn.postValue(false)
            }
        }
    }
}
