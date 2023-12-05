import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Board.BoardViewModel

@Composable
fun BoardDetailScreen(viewModel: BoardViewModel, boardId: Long) {
    // 게시글 상세 정보를 가져옵니다.
    LaunchedEffect(boardId) {
        viewModel.fetchBoardDetails(boardId)
    }

    val boardDetails by viewModel.boardDetails.observeAsState()
    boardDetails?.let { details ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = details.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "작성자: ${details.writerName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = details.content, style = MaterialTheme.typography.bodySmall)
            Text(text = "조회수: ${details.viewCount}", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "작성일: ${details.createdDate}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "수정일: ${details.modifiedDate}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text("댓글", style = MaterialTheme.typography.bodyMedium)
            details.comments.forEach { comment ->
                Text(text = comment.content, style = MaterialTheme.typography.bodySmall)
                // 여기에 댓글에 대한 추가 정보를 표시할 수 있음
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("첨부 파일", style = MaterialTheme.typography.bodyMedium)
            details.files.forEach { file ->
                Text(text = file.originFileName, style = MaterialTheme.typography.bodySmall)
                // 파일에 대한 추가 정보를 표시할 수 있음
            }
        }
    }
}
