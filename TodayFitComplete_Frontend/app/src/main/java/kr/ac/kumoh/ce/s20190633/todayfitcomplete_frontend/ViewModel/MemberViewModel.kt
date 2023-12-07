package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel

import android.app.Application
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
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.NullOnEmptyConverterFactory
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.SharedPreferencesUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MemberViewModel(private val application: Application) : ViewModel() {
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val memberApiService: MemberApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(NullOnEmptyConverterFactory())
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
            try {
                val response = withContext(Dispatchers.IO) {
                    memberApiService.checkIdDuplicate(email).execute()
                }
                if (response.isSuccessful) {
                    onResult(true)
                } else {
                    Log.e("MemberViewModel", "Check email duplicate failed: ${response.errorBody()?.string()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("MemberViewModel", "Error checking email duplicate: ${e.message}")
                onResult(false)
            }
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
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    // SharedPreferences를 사용하여 토큰과 사용자 이메일 저장
                    SharedPreferencesUtils.saveToken(application, token)
                    SharedPreferencesUtils.saveEmail(application, email)
                    _isLoggedIn.postValue(true)
                } else {
                    _isLoggedIn.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("MemberViewModel", "Error on member login: ${e.message}")
                _isLoggedIn.postValue(false)
            }
        }
    }

}
