package br.edu.ifsp.scl.sdm.todolist.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
//Estou declarando que meu data class é uma entidade do Room
@Entity
data class Task (
    //Estou declarando que o atributo time é a chave primária da entidade
    @PrimaryKey
    var time: Long = INVALID_TIME, //Chave primária tem o valor padrão de -1
    var name: String = "",
    var done: Int = TASK_DONE_FALSE
    //como esses serão enviados para outras activities, preciso que sejam Parcelable
): Parcelable {
    companion object {
        const val INVALID_TIME = -1L
        const val TASK_DONE_TRUE = 1
        const val TASK_DONE_FALSE = 0
    }
}