
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberRegisterDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.R
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Screen
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: MemberViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isCheckingEmail by remember { mutableStateOf(false) }
    var emailDuplicationPassed by remember { mutableStateOf(false) }
    val registrationStatus by viewModel.registrationStatus.observeAsState()
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    LaunchedEffect(registrationStatus) {
        registrationStatus?.let { status ->
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
            if (status == "회원가입에 성공했습니다.") {
                email = ""
                password = ""
                passwordCheck = ""
                username = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.todayfitcomplete_register),
            contentDescription = "타이틀 사진",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("회원가입",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailDuplicationPassed = false
            },
            label = { Text("이메일") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            trailingIcon = {
                if (isCheckingEmail) {
                    CircularProgressIndicator()
                } else {
                    Button(onClick = {
                        isCheckingEmail = true
                        viewModel.checkEmailDuplicate(email) { duplicate ->
                            isCheckingEmail = false
                            emailDuplicationPassed = duplicate
                            val message = if (emailDuplicationPassed) {
                                "이메일 사용이 가능합니다."
                            } else {
                                "이메일이 중복됩니다."
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("중복 확인")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passwordCheck,
            onValueChange = { passwordCheck = it },
            label = { Text("비밀번호 확인") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("사용자 이름") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.register(MemberRegisterDto(email, password, passwordCheck, username))
            }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.register(MemberRegisterDto(email, password, passwordCheck, username))
            },
            enabled = emailDuplicationPassed && email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && username.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입")
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인 하러 가기")
        }
    }
}