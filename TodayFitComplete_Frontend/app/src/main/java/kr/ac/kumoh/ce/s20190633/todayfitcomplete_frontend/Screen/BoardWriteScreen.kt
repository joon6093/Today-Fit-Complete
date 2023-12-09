import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardWriteScreen(viewModel: BoardViewModel, navController: NavController) {
    // 게시물 제목과 내용을 저장하는 변수
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    // 선택된 파일 URI 목록을 저장하는 변수
    var selectedFileUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val context = LocalContext.current
    // 갤러리 열기 액티비티를 실행하는 런처를 초기화
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        uris?.let {
            selectedFileUris = it
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // 제목 입력 필드
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 내용 입력 필드 (다중 라인)
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 갤러리 열기 버튼
        Button(
            onClick = { galleryLauncher.launch("*/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("이미지 및 동영상 선택")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 게시글 작성 버튼
        Button(
            onClick = {
                // 제목, 내용, 선택된 파일 URI 목록을 사용하여 게시글 작성 요청
                viewModel.writePostWithFiles(title, content, selectedFileUris) {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("게시글 작성")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 선택된 파일 목록 표시
        Text("선택된 파일 목록:", style = MaterialTheme.typography.bodyMedium)
        selectedFileUris.forEachIndexed { index, uri ->
            Card(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 파일 아이콘
                    Icon(
                        imageVector = Icons.Default.FileCopy,
                        contentDescription = "파일",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // 파일 정보
                    Column {
                        Text(
                            text = "${index + 1}. ${uri.lastPathSegment}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
