package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend

import BoardDetailScreen
import BoardListScreen
import BoardWriteScreen
import LoginScreen
import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.CommentViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.MemberViewModel

// 앱 화면의 다양한 섹션을 식별하기 위한 열거형 Screen 정의
enum class Screen(val route: String) {
    Login("login"),
    Register("register"),
    BoardList("boardList"),
    BoardDetail("boardDetail/{boardId}")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModels 초기화
        val memberViewModel: MemberViewModel by viewModels()
        val boardViewModel: BoardViewModel by viewModels()
        val commentViewModel: CommentViewModel by viewModels()

        // 액티비티 내용 설정
        setContent {
            // NavController 생성 및 초기화
            val navController = rememberNavController()

            // NavHost를 사용하여 앱 내비게이션을 정의
            NavHost(navController = navController, startDestination = Screen.Login.name) {
                composable(Screen.Login.route) {
                    // 로그인 화면 표시
                    LoginScreen(memberViewModel, navController)
                }
                composable(Screen.Register.route) {
                    // 회원가입 화면 표시
                    RegisterScreen(memberViewModel, navController)
                }
                composable(Screen.BoardList.route) {
                    // 게시글 목록 화면 표시
                    BoardListScreen(boardViewModel, navController)
                }
                composable(Screen.BoardDetail.route) { backStackEntry ->
                    // 게시글 상세 화면 표시
                    val boardId = backStackEntry.arguments?.getString("boardId")?.toLongOrNull()
                    if (boardId != null) {
                        BoardDetailScreen(
                            boardViewModel = boardViewModel,
                            commentViewModel = commentViewModel,
                            boardId = boardId,
                            navController = navController
                        )
                    }
                }
                composable("boardWrite") {
                    // 게시글 작성 화면 표시
                    BoardWriteScreen(boardViewModel, navController)
                }
            }
        }
    }
}
