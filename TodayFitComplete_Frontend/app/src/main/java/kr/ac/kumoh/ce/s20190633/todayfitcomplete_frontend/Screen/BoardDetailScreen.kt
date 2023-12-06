import android.widget.VideoView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.CommentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(boradViewModel: BoardViewModel, commentViewModel: CommentViewModel, boardId: Long) {
    var newCommentText by remember { mutableStateOf("") }

    LaunchedEffect(boardId) {
        boradViewModel.fetchBoardDetails(boardId)
        commentViewModel.fetchComments(boardId)
    }

    val boardDetails by boradViewModel.boardDetails.observeAsState()
    val comments by commentViewModel.commentsLiveData.observeAsState()

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

            // 댓글 입력 필드
            TextField(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                label = { Text("댓글 작성") }
            )
            Button(onClick = {
                commentViewModel.postComment(boardId, CommentDto(newCommentText))
                newCommentText = ""
            }) {
                Text("게시")
            }
            // 댓글 목록 표시
            comments?.forEach { comment ->
                Text(text = comment.content)
                Button(onClick = { /* 댓글 수정 로직 */ }) {
                    Text("수정")
                }
                Button(onClick = { commentViewModel.deleteComment(comment.commentId) }) {
                    Text("삭제")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("첨부 파일", style = MaterialTheme.typography.bodyMedium)
            details.files.forEach { file ->
                Text(text = file.originFileName, style = MaterialTheme.typography.bodySmall)
                when {
                    file.fileType.startsWith("image") -> {
                        AsyncImage(
                            model = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app/files/${file.filePath}",
                            contentDescription = "첨부 이미지",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                    file.fileType.startsWith("video") -> {
                        // 동영상 재생을 위한 VideoView 구현
                        AndroidView(
                            factory = { context ->
                                VideoView(context).apply {
                                    setVideoPath("https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app/files/${file.filePath}")
                                    start() // 자동 재생
                                }
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}