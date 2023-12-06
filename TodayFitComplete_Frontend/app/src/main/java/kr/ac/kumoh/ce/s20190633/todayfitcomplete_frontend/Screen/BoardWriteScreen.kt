import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.BoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardWriteScreen(viewModel: BoardViewModel) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedFileUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // 현재 Composable의 Context를 가져옴
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        uris?.let {
            selectedFileUris = it
        }
    }

    Column {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") }
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용") }
        )
        Button(onClick = { galleryLauncher.launch("*/*") }) {
            Text("게시글에 올릴 이미지 및 동영상 선택")
        }
        Button(onClick = {
            // Context를 직접 전달하지 않고 필요한 정보만을 viewModel 함수에 전달
            viewModel.writePostWithFiles(title, content, selectedFileUris)
        }) {
            Text("게시글 작성")
        }
        Text("선택된 파일 목록:")
        selectedFileUris.forEachIndexed { index, uri ->
            Text("${index + 1}. ${uri.toString()}")
        }
    }
}
