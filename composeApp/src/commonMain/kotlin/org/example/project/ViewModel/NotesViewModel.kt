package org.example.project.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.project.Note
import org.example.project.data.NoteRepository

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val notes: StateFlow<List<Note>> = _searchQuery
        .flatMapLatest { query: String ->
            if (query.isBlank()) repository.getAllNotes()
            else repository.searchNotes(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteNotes: StateFlow<List<Note>> = repository.getFavoriteNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    sealed class NotesUiState {
        object Loading : NotesUiState()
        object Empty : NotesUiState()
        data class Content(val notes: List<Note>) : NotesUiState()
    }

    val uiState: StateFlow<NotesUiState> = notes
        .map { list: List<Note> ->
            if (list.isEmpty()) NotesUiState.Empty
            else NotesUiState.Content(list)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotesUiState.Loading
        )

    fun addNote(title: String, content: String) {
        viewModelScope.launch { repository.insertNote(title, content) }
    }

    fun updateNote(noteId: String, newTitle: String, newContent: String) {
        viewModelScope.launch { repository.updateNote(noteId, newTitle, newContent) }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch { repository.deleteNote(noteId) }
    }

    fun toggleFavorite(note: Note) {
        viewModelScope.launch {
            val currentFavIds: List<String> = favoriteNotes.value.map { it.id }
            repository.toggleFavorite(note, currentFavIds)
        }
    }

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun clearSearch() { _searchQuery.value = "" }
}