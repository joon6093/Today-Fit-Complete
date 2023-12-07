
import android.widget.VideoView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.SharedPreferencesUtils
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.CommentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(boardViewModel: BoardViewModel, commentViewModel: CommentViewModel, boardId: Long, navController: NavController) {
    var newCommentText by remember { mutableStateOf("") }
    val boardDetails by boardViewModel.boardDetails.observeAsState()
    val comments by commentViewModel.commentsLiveData.observeAsState()
    val context = LocalContext.current
    val currentUserEmail = SharedPreferencesUtils.getEmail(context)
    val scrollState = rememberScrollState()

    LaunchedEffect(boardId) {
        boardViewModel.fetchBoardDetails(boardId)
        commentViewModel.fetchComments(boardId)
    }

    boardDetails?.let { details ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = details.title, style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Visibility, contentDescription = "조회수", Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${details.viewCount}회", style = MaterialTheme.typography.bodySmall)
                    Icon(Icons.Filled.Event, contentDescription = "작성일", Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = details.createdDate.substringBeforeLast(':'),
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (details.writerName == currentUserEmail) {
                        IconButton(
                            onClick = {
                                boardViewModel.deleteBoard(boardId)
                                navController.popBackStack() // 게시글 삭제 후 이전 화면으로 돌아가기
                            },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "게시글 삭제")
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Person, contentDescription = "작성자", Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = details.writerName, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = details.content, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            details.files.forEach { file ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        when {
                            file.fileType.startsWith("image") -> {
                                AsyncImage(
                                    model = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app/files/${file.filePath}",
                                    contentDescription = "첨부 이미지",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            }

                            file.fileType.startsWith("video") -> {
                                AndroidView(
                                    factory = { context ->
                                        VideoView(context).apply {
                                            setVideoPath("https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app/files/${file.filePath}")
                                            setOnClickListener {
                                                if (!isPlaying) {
                                                    start() // 클릭 시 비디오 재생
                                                } else {
                                                    pause() // 이미 재생 중일 경우 일시 정지
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = file.originFileName,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            comments?.forEach { comment ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = "작성자",
                                        Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = comment.commentWriterName,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.Event,
                                        contentDescription = "작성일",
                                        Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = comment.createdDate.substringBeforeLast(':'),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comment.content,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            if (comment.commentWriterName == currentUserEmail) {
                                                commentViewModel.deleteComment(comment.commentId)
                                                commentViewModel.fetchComments(boardId)
                                            }
                                        },
                                        enabled = comment.commentWriterName == currentUserEmail,
                                        modifier = Modifier.alpha(if (comment.commentWriterName == currentUserEmail) 1f else 0f)
                                    ) {
                                        Icon(Icons.Filled.Delete, contentDescription = "삭제")
                                    }
                                }
                            }
                        }
                    }
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    label = { Text("댓글 작성") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    commentViewModel.postComment(boardId, CommentDto(newCommentText))
                    newCommentText = ""
                }) {
                    Icon(Icons.Filled.Send, contentDescription = "게시")
                }
            }

        }
    }
}
