package presntation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.RequestState
import domain.ToDoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias MutableTasks = MutableState<RequestState<List<ToDoTask>>>
typealias Tasks = State<RequestState<List<ToDoTask>>>

class HomeViewModel(
    private val mongoDB: MongoDB
) : ScreenModel {

    private var _activeTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val activeTasks: Tasks = _activeTasks

    private var _completeTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val completeTask: Tasks = _completeTasks


    init {
        _activeTasks.value = RequestState.Loading
        _completeTasks.value = RequestState.Loading

        screenModelScope.launch(Dispatchers.Main) {
            delay(500L)
            mongoDB.readActiveTasks().collectLatest {
                _activeTasks.value = it
            }
        }

        screenModelScope.launch(Dispatchers.Main) {
            delay(500L)
            mongoDB.readCompleteTasks().collectLatest {
                _completeTasks.value = it
            }
        }
    }
}