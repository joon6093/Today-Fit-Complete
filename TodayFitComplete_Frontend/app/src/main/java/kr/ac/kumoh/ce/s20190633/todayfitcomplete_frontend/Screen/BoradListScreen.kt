
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardListResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel

@Composable
fun BoardListScreen(viewModel: BoardViewModel, navController: NavController) {
    // ViewModel에서 게시판 목록을 가져옵니다.
    val boardList by viewModel.boardList.observeAsState(initial = emptyList())

    // 화면 전체를 채우는 Box를 만듭니다.
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(8.dp)) {
            // 화면 상단에 앱 로고와 제목을 표시합니다.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FitnessCenter, contentDescription = "오운완 로고", Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "오늘의 오운완", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
            }

            // LazyColumn을 사용하여 게시판 목록을 스크롤 가능한 리스트로 표시합니다.
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(boardList) { board ->
                    // 각 게시물 항목을 표시하는 BoardListItem을 호출합니다.
                    BoardListItem(board = board) {
                        // 게시물 항목을 클릭하면 해당 게시물의 상세 화면으로 이동합니다.
                        navController.navigate("boardDetail/${board.boardId}")
                    }
                }
            }
        }

        // 화면 하단에 게시글 작성 버튼(FloatingActionButton)을 표시합니다.
        FloatingActionButton(
            onClick = { navController.navigate("boardWrite") },
            content = { Icon(Icons.Filled.Add, "게시글 작성") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun BoardListItem(board: BoardListResponse, onClick: () -> Unit) {
    // 각 게시물 항목을 Card로 감싸서 표시합니다.
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick), // 항목을 클릭하면 onClick 이벤트가 발생합니다.
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 게시물 제목과 조회수, 작성일을 표시합니다.
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = board.title, style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Visibility, contentDescription = "조회수", Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${board.viewCount}회", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Filled.Event, contentDescription = "작성일", Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = board.createdDate.substringBeforeLast(':'), style = MaterialTheme.typography.bodySmall)
                }
            }
            // 작성자 정보를 표시합니다.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(Icons.Filled.Person, contentDescription = "작성자", Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = board.writerName, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            // 게시물 내용을 표시합니다.
            Text(text = board.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}