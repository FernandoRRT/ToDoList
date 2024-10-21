package br.edu.ifsp.scl.sdm.todolist.controller

import androidx.room.Room
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import br.edu.ifsp.scl.sdm.todolist.view.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//O MVC tem muito nível de acooplamento, então o controller precisa conhecer o model e a view
class MainController(private val mainFragment: MainFragment) {
    //O controller precisa conhecer o model. Esse é o ponto de entrada para o Room
    private val taskDaoImpl = Room.databaseBuilder(
        //o primeiro argumento é o contexto
        mainFragment.requireContext(),
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
            mainFragment.updateTaskList(tasks)
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

}