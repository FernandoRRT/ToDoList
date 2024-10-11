package br.edu.ifsp.scl.sdm.todolist.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task

// Interface que define as operações de CRUD para a entidade Task
@Dao
interface TaskDao {
    companion object {
        //dei o nome de task para a tabela
        const val TASK_TABLE = "task"
    }
    @Insert
    fun createTask(task: Task)
    @Query("SELECT * FROM $TASK_TABLE")
    fun retrieveTasks(): List<Task>
    @Update
    fun updateTask(task: Task)
    @Delete
    fun deleteTask(task: Task)
}