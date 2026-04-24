
package org.example.project.UI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.example.project.Note
import org.example.project.Navigasi.Screen
import org.example.project.ViewModel.NotesViewModel
import org.example.project.ViewModel.SettingsViewModel


@Composable
fun NoteCard(
    note: Note,
    isFav: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onFavClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() }, onLongPress = { onLongPress() })
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    note.content,
                    maxLines = 2,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
            IconButton(onClick = onFavClick) {
                Icon(
                    if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint = if (isFav) Color.Red else Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    navController: NavController,
    notesList: List<Note>,
    favList: List<Note>,
    viewModel: NotesViewModel,
    settingsViewModel: SettingsViewModel? = null
) {
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val settingsState = settingsViewModel?.uiState?.collectAsState()?.value

    val sortedList = remember(notesList, settingsState) {
        when (settingsState?.sortOrder) {
            "oldest"  -> notesList.sortedBy { it.id }
            "title"   -> notesList.sortedBy { it.title }
            else      -> notesList
        }.let { list ->
            if (settingsState?.showFavoritesFirst == true) {
                list.sortedByDescending { note -> favList.any { it.id == note.id } }
            } else list
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            onClear = { viewModel.clearSearch() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            when (uiState) {
                is NotesViewModel.NotesUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                is NotesViewModel.NotesUiState.Empty -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.NoteAlt,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                if (searchQuery.isBlank()) "Belum ada catatan.\nTambah catatan baru!"
                                else "Catatan '${searchQuery}' tidak ditemukan.",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontSize = 15.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                is NotesViewModel.NotesUiState.Content -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp, top = 4.dp)
                    ) {
                        items(sortedList, key = { it.id }) { note ->
                            NoteCard(
                                note = note,
                                isFav = favList.any { it.id == note.id },
                                onClick = { navController.navigate(Screen.NoteDetail.createRoute(note.id)) },
                                onLongPress = { noteToDelete = note },
                                onFavClick = { viewModel.toggleFavorite(note) }
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddNote.route) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Catatan")
            }
        }
    }

    if (noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Hapus Catatan?") },
            text = { Text("Hapus '${noteToDelete!!.title}' secara permanen?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNote(noteToDelete!!.id)
                    noteToDelete = null
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Batal", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Cari catatan...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Cari",
                tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = "Hapus pencarian")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun FavoritesScreen(
    navController: NavController,
    favList: List<Note>,
    viewModel: NotesViewModel
) {
    if (favList.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Belum ada catatan favorit.\nTap ❤ pada catatan untuk menambahkan.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(favList, key = { it.id }) { note ->
                NoteCard(
                    note = note,
                    isFav = true,
                    onClick = { navController.navigate(Screen.NoteDetail.createRoute(note.id)) },
                    onLongPress = {},
                    onFavClick = { viewModel.toggleFavorite(note) }
                )
            }
        }
    }
}

@Composable
fun AddNoteScreen(onNoteSaved: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val isSaveEnabled = title.isNotBlank() && content.isNotBlank()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul Catatan") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth().weight(1f),
            minLines = 5,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onNoteSaved(title, content) },
            enabled = isSaveEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Simpan Catatan")
        }
    }
}

@Composable
fun NoteDetailContent(noteData: Note?, navController: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (noteData == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data tidak ditemukan", color = Color.Gray)
            }
        } else {
            Text(
                noteData.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text(
                noteData.content,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 24.sp
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.EditNote.createRoute(noteData.id)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Edit Catatan")
            }
        }
    }
}

@Composable
fun EditNoteScreen(noteData: Note?, onNoteSaved: (String, String) -> Unit) {
    var title by remember { mutableStateOf(noteData?.title ?: "") }
    var content by remember { mutableStateOf(noteData?.content ?: "") }
    val isSaveEnabled = title.isNotBlank() && content.isNotBlank()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul Catatan") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth().weight(1f),
            minLines = 5,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onNoteSaved(title, content) },
            enabled = isSaveEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Update Catatan")
        }
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Pengaturan Aplikasi", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(Modifier.height(20.dp))

        SettingsSectionHeader("Tampilan")
        Spacer(Modifier.height(8.dp))

        SettingsToggleItem(
            icon = Icons.Default.DarkMode,
            title = "Mode Gelap",
            subtitle = "Aktifkan tema gelap",
            checked = state.isDarkMode,
            onCheckedChange = { viewModel.toggleDarkMode(it) }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        SettingsSectionHeader("Catatan")
        Spacer(Modifier.height(8.dp))

        SettingsToggleItem(
            icon = Icons.Default.Favorite,
            title = "Favorit di Atas",
            subtitle = "Tampilkan catatan favorit di urutan pertama",
            checked = state.showFavoritesFirst,
            onCheckedChange = { viewModel.toggleShowFavoritesFirst(it) }
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Urutan Catatan",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))

        val sortOptions = listOf(
            Triple("newest", Icons.Default.AccessTime, "Terbaru"),
            Triple("oldest", Icons.Default.History, "Terlama"),
            Triple("title",  Icons.Default.SortByAlpha, "Abjad (A-Z)")
        )

        sortOptions.forEach { (value, icon, label) ->
            SortOrderItem(
                icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
                label = label,
                selected = state.sortOrder == value,
                onClick = { viewModel.setSortOrder(value) }
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp)
    )
}

@Composable
private fun SettingsToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SortOrderItem(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(8.dp))
        icon()
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 14.sp)
    }
}