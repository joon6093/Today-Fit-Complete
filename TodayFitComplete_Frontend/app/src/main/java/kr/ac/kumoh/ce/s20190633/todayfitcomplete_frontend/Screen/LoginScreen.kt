
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.R
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Screen
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: MemberViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // ViewModel에서 로그인 상태를 관찰하고, 로그인되었을 때 화면을 이동하는 부분입니다.
    viewModel.isLoggedIn.observeAsState().value?.let { loggedIn ->
        if (loggedIn) {
            navController.navigate(Screen.BoardList.route) {
                popUpTo(Screen.Login.route) { inclusive = true } // 로그인 화면을 스택에서 제거
            }
        }
    }

    // 로그인 화면의 UI를 구성하는 부분입니다.
    Column(modifier = Modifier
        .verticalScroll(scrollState)
        .padding(16.dp)
        .fillMaxWidth()) {
        // 화면 상단에 로고와 앱 이름을 표시합니다.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.FitnessCenter, contentDescription = "오운완 로고", Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "오운완", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ": 오늘 운동 완료 인증 애플리케이션",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // 이미지를 표시합니다.
        Image(
            painter = painterResource(id = R.drawable.todayfitcomplete_login),
            contentDescription = "로그인 사진",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(5.dp))

        // 로그인 안내 메시지를 표시합니다.
        Text(
            text = "다른 사용자들에게 오늘의 운동을 인증하고 인정받으세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))

        // 이메일 입력 필드
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 비밀번호 입력 필드
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 로그인 버튼
        Button(
            onClick = {
                isLoading = true
                viewModel.login(email, password)
                isLoading = false
            },
            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("로그인")
            }
        }

        // 로그인 오류 메시지를 표시합니다.
        if (loginError) {
            Text(
                text = "아이디와 비밀번호가 일치하지 않습니다.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회원가입 화면으로 이동하는 버튼
        Button(
            onClick = { navController.navigate(Screen.Register.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입 하러 가기")
        }
    }
}
