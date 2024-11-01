package com.app.todo.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todo.db.AppDatabase
import com.app.todo.db.entities.TodoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(appDatabase: AppDatabase) : ViewModel() {
    private val todoDao = appDatabase.todoDao()

    private val _allTodosState = MutableStateFlow<State<List<TodoEntity>>>(State.Loading)
    val allTodosState: StateFlow<State<List<TodoEntity>>> get() = _allTodosState

    private val _todoState = MutableLiveData<State<Boolean>>()
    val todoState: LiveData<State<Boolean>> get() = _todoState

    private var searchList = emptyList<TodoEntity>()

    fun getAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = todoDao.getAllTodos()
            if (list.isEmpty()) {
                _allTodosState.value = State.Error("No todos found")
            } else {
                _allTodosState.value = State.Success(list)
                searchList = list
            }
        }
    }

    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.updateTodo(todo)
            _todoState.postValue(State.Success(true))
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(todo)
            _todoState.postValue(State.Success(true))
        }
    }

    fun addTodo(todoEntity: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.addTodo(todoEntity)
            _todoState.postValue(State.Success(true))
        }
    }

    fun searchTodos(query: String) {
        if (searchList.isEmpty()) {
            return
        }
        searchList.filter {
            it.title.contains(query, true)
        }.also {
            _allTodosState.value = State.Success(it)
        }
    }

    fun sortByDescending() {
        if (_allTodosState.value is State.Success) {
            val list = (_allTodosState.value as State.Success).data
            if (list.isNotEmpty()) {
                _allTodosState.value = State.Success(list.sortedByDescending { it.title })
            }
        }
    }

    fun sortByAscending() {
        if (_allTodosState.value is State.Success) {
            val list = (_allTodosState.value as State.Success).data
            if (list.isNotEmpty()) {
                _allTodosState.value = State.Success(list.sortedBy { it.title })
            }
        }
    }

    sealed class State<out T> {
        data class Success<out T>(val data: T) : State<T>()
        data class Error(val message: String) : State<Nothing>()
        data object Loading : State<Nothing>()
    }

}