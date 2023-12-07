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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.CommentViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.MemberViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.MemberViewModelFactory

enum class Screen(val route: String) {
    Login("login"),
    Register("register"),
    BoardList("boardList"),
    BoardDetail("boardDetail/{boardId}")
}


class MainActivity : ComponentActivity() {
    private lateinit var memberViewModel: MemberViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = MemberViewModelFactory(application)
        memberViewModel = ViewModelProvider(this, viewModelFactory).get(MemberViewModel::class.java)
        val boardViewModel: BoardViewModel by viewModels()
        val commentViewModel: CommentViewModel by viewModels()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.Login.name) {
                composable(Screen.Login.route) {
                    LoginScreen(memberViewModel, navController)
                }
                composable(Screen.Register.route) {
                    RegisterScreen(memberViewModel, navController)
                }
                composable(Screen.BoardList.route){
                    BoardListScreen(boardViewModel, navController)
                }
                composable(Screen.BoardDetail.route) { backStackEntry ->
                    val boardId = backStackEntry.arguments?.getString("boardId")?.toLongOrNull()
                    if (boardId != null) {
                        BoardDetailScreen(boardViewModel = boardViewModel, commentViewModel = commentViewModel, boardId = boardId, navController = navController)
                    }
                }
                composable("boardWrite") {
                    BoardWriteScreen(boardViewModel, navController)
                }
            }
        }
    }
}
