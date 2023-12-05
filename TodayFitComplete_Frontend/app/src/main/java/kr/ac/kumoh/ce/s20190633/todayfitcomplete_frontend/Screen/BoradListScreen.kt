import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad.BoardListResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Board.BoardViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Screen
@Composable
fun BoardListScreen(viewModel: BoardViewModel, navController: NavController) {
    val boardList by viewModel.boardList.observeAsState(initial = emptyList())

    LazyColumn {
        items(boardList) { board ->
            BoardListItem(board = board) {
                navController.navigate(Screen.BoardDetail.route)
            }
        }
    }
}

@Composable
fun BoardListItem(board: BoardListResponse, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = board.title, style = MaterialTheme.typography.titleMedium)
        Text(text = "작성자: ${board.writerName}", style = MaterialTheme.typography.bodyMedium)
        Text(text = board.content, style = MaterialTheme.typography.bodySmall)
    }
}