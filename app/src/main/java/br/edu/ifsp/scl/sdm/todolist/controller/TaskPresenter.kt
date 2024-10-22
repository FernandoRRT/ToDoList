package br.edu.ifsp.scl.sdm.todolist.controller

import androidx.fragment.app.Fragment
import androidx.room.Room
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import br.edu.ifsp.scl.sdm.todolist.view.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//No modo de presenter a gente precisa criar uma classe abstrata ou interface que vai ser implementada pela main fragment.
//Essa interface será chamada de TaskView. Ela serpa usada para a view chamar o presenter no main fragment
//Eu vou alterar lá, onde recebia um mainController agora vai receber um TaskPresenter
class TaskPresenter (private val taskView: TaskView) {
    //O controller precisa conhecer o model. Esse é o ponto de entrada para o Room
    private val taskDaoImpl = Room.databaseBuilder(
        //Eu chamei o requireContext() da view, mas poderia ser de qualquer fragment
        (taskView as Fragment).requireContext(),
        //qual a classe que implementa o banco de dados
        ToDoListDatabase::class.java,
        //nome do banco de dados
        ToDoListDatabase.TO_DO_LIST_DATABASE
    ).build().getTaskDao()


    //Funcções que serão chamadas pela view para o controler fazer o acesso ao modelo
    fun insertTask(task: Task) {
        //Aqui o controller chama o model para inserir a task
        //Como chamadas ao banco de dados são bloqueantes, é melhor fazer em uma thread separada
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.createTask(task)
        }
    }

    fun getTasks() {
        //Aqui o controller chama o model para recuperar as tasks. Ela dá um retrieveTasks e passa para a view
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = taskDaoImpl.retrieveTasks()
            //Aqui o controller chama a view para atualizar a lista de tasks
            taskView.updateTaskList(tasks)
        }
    }

    fun editTask(task: Task) {
        //Aqui o controller chama o model para atualizar a task
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.updateTask(task)
        }
    }

    fun removeTask(task: Task) {
        //Aqui o controller chama o model para deletar a task
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.deleteTask(task)
        }
    }

    interface TaskView {
        fun updateTaskList(tasks: List<Task>)
    }
}