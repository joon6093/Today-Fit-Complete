package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class MemberViewModel(application: Application) : AndroidViewModel(application) {
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val memberApiService: MemberApiService

    init {
        // Retrofit을 초기화하여 서버와 통신할 준비를 합니다.
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(NullOnEmptyConverterFactory()) // 빈 응답을 처리하기 위한 컨버터
            .addConverterFactory(GsonConverterFactory.create()) // JSON 응답을 파싱하기 위한 컨버터
            .build()

        memberApiService = retrofit.create(MemberApiService::class.java) // Retrofit 인터페이스 구현 생성
    }

    // 회원가입과 관련된 기능
    private val _registrationStatus = MutableLiveData<String?>()
    val registrationStatus: LiveData<String?> = _registrationStatus
    fun register(memberRegisterDto: MemberRegisterDto) {
        viewModelScope.launch {
            try {
                // 회원가입 API를 호출하고 응답을 받아옵니다.
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

    // 이메일 중복 검사를 수행하는 함수
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

    // 로그인과 관련된 기능
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                // 로그인 정보를 담은 DTO 객체 생성
                val memberLoginDto = MemberLoginDto(email, password)
                // 로그인 API를 호출하고 응답을 받아옵니다.
                val response = withContext(Dispatchers.IO) {
                    memberApiService.login(memberLoginDto).execute()
                }
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    // SharedPreferences를 사용하여 토큰과 사용자 이메일을 저장합니다.
                    val context = getApplication<Application>().applicationContext
                    SharedPreferencesUtils.saveToken(context, token)
                    SharedPreferencesUtils.saveEmail(context, email)
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
