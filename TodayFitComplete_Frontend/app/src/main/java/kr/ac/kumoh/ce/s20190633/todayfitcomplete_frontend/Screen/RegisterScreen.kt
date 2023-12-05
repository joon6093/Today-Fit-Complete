import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberRegisterDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Member.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: MemberViewModel, onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    var isEmailDuplicate by remember { mutableStateOf(false) }
    var isCheckingEmail by remember { mutableStateOf(false) }
    // 이메일 중복 검사 결과에 따른 메시지 표시
    var emailCheckMessage by remember { mutableStateOf<String?>(null) }
    // 회원가입 메시지
    val registrationStatus by viewModel.registrationStatus.observeAsState()

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            trailingIcon = {
                if (isCheckingEmail) {
                    CircularProgressIndicator()
                } else {
                    // 중복 검사 버튼 클릭 시 로직
                    Button(onClick = {
                        isCheckingEmail = true
                        viewModel.checkEmailDuplicate(email) { duplicate ->
                            isEmailDuplicate = duplicate
                            isCheckingEmail = false
                            emailCheckMessage = if (duplicate) {
                                "이메일이 중복됩니다."
                            } else {
                                "이메일이 사용 가능합니다."
                            }
                        }
                    }) {
                        Text("중복 확인")
                    }
                    // 이메일 중복 검사 결과 메시지 표시
                    emailCheckMessage?.let {
                        Text(it, color = if (isEmailDuplicate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                    }
                }
            }
        )
        if (isEmailDuplicate) {
            Text("이메일이 중복됩니다.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passwordCheck,
            onValueChange = { passwordCheck = it },
            label = { Text("비밀번호 확인") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("사용자 이름") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.register(MemberRegisterDto(email, password, passwordCheck, username))
            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.register(MemberRegisterDto(email, password, passwordCheck, username))
        }) {
            Text("회원가입")
        }

        Spacer(modifier = Modifier.height(16.dp))

        registrationStatus?.let {
            Text(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToLogin) {
            Text("뒤로 가기")
        }
    }
}
