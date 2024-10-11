package br.edu.ifsp.scl.sdm.todolist.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.ifsp.scl.sdm.todolist.model.dao.TaskDao
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task

// Classe que representa o banco de dados da aplicação
@Database(entities = [Task::class], version = 1)
abstract class ToDoListDatabase: RoomDatabase() {
    companion object {
        const val TO_DO_LIST_DATABASE = "toDoListDatabase"
    }
    abstract fun getTaskDao(): TaskDao
}