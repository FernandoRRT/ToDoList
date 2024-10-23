package br.edu.ifsp.scl.sdm.todolist.controller

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import br.edu.ifsp.scl.sdm.todolist.view.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//O que eu preciso no MVVP é um contexto para acessar o banco de dados, por isso eu preciso de um
//parametro application para dar acesso à área de armazenamento do aplicativo
class TaskViewModel (application: Application): ViewModel() {
    //O controller precisa conhecer o model. Esse é o ponto de entrada para o Room
    private val taskDaoImpl = Room.databaseBuilder(
        //o primeiro argumento é o contexto
        application.applicationContext,
        //qual a classe que implementa o banco de dados
        ToDoListDatabase::class.java,
        //nome do banco de dados
        ToDoListDatabase.TO_DO_LIST_DATABASE
    ).build().getTaskDao()

    //Precisamos de um objeto observavel pelo mainFragment para atualizar a lista de tasks
    //Ele vai armazenar dentro dele uma list de tasks e toda vez que ele for alterado
    // ele vai notificar todos os observadores
    val tasksMld = MutableLiveData<List<Task>>()


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
            //Aqui atualiza a lista de tasks
            tasksMld.postValue(tasks)
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
    //para que view e viewmodel tenham ciclos de vida diferentes é necessário que eu utilize um Factory
    //Ao fazer um companion object eu defini um bloco de código estático, ou seja, membros que podem
    // ser acessados diretamente a partir da classe sem precisar instanciar um objeto.
    companion object {
//Aqui eu declaro uma constante que contém uma instância anônima de ViewModelProvider.Factory,  A interface Factory
// é usada para criar instâncias de ViewModel de maneira personalizada, ou seja, para que o ViewModel possa receber
// parâmetros no construtor.
        val TaskViewModelFactory = object: ViewModelProvider.Factory {
//Agora eu consigo sobrescrever o métdo create e criar uma instância de TaskViewModel. O método create recebe um modelClass e um extras
//modelClass: A classe do ViewModel que está sendo solicitada.
//extras: Um objeto CreationExtras, que contém informações adicionais, como o contexto da aplicação, que são necessárias para instanciar o ViewModel.
            override fun <T: ViewModel> create (modelClass: Class<T>, extras: CreationExtras): T =
//essa checagem vai garantir que o modelClass seja do tipo TaskViewModel (através do casting as T) e que o extras não seja nulo
                TaskViewModel(checkNotNull (extras [ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as T
            }
        }
}
