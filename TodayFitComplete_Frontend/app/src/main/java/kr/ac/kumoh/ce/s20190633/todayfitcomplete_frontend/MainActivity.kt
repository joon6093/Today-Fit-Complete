package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend

import BoardDetailScreen
import BoardListScreen
import LoginScreen
import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Board.BoardViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Member.MemberViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screen(val route: String) {
    Login("login"),
    Register("register"),
    BoardList("boardList"),
    BoardDetail("boardDetail/{boardId}")
}


class MainActivity : ComponentActivity() {
    private val memberViewModel: MemberViewModel by viewModels()
    private val boardViewModel: BoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screen.Login.name) {
                composable(Screen.Login.route) {
                    LoginScreen(memberViewModel,
                        onNavigateToRegister = { navController.navigate(Screen.Register.name) },
                        onLoginSuccess = { navController.navigate(Screen.BoardList.name) }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(memberViewModel) { navController.popBackStack() }
                }
                composable(Screen.BoardList.route){
                    BoardListScreen(boardViewModel, navController)
                }
                composable(Screen.BoardDetail.route)  { backStackEntry ->
                    val boardId = backStackEntry.arguments?.getString("boardId")?.toLongOrNull()
                    boardId?.let {
                        BoardDetailScreen(viewModel = boardViewModel, boardId = it)
                    }
                }
            }
        }
    }
}
