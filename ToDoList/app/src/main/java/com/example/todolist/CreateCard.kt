package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.todolist.databinding.ActivityCreateCardBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.DelicateCoroutinesApi

class CreateCard : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCardBinding
    private lateinit var database: Database
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = Room.databaseBuilder(applicationContext, Database::class.java, "to_do_db").build()

        binding.saveButton.setOnClickListener {
            if (binding.createTitle.text.toString().trim { it <= ' ' }.isNotEmpty()
                && binding.createPriority.text.toString().trim { it <= ' ' }.isNotEmpty()
            ) {
                val title = binding.createTitle.text.toString()
                val priority = binding.createPriority.text.toString()
                DataObject.setData(title, priority)
                GlobalScope.launch {
                    database.dao().insertTask(Entity(0, title, priority))
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}