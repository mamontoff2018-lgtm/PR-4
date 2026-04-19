package com.example.sqlitelab

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLHelper
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = SQLHelper(this)
        container = findViewById(R.id.container)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            showAddDialog()
        }

        loadAllStudents() // загрузка списка при запуске
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etGrade = dialogView.findViewById<EditText>(R.id.etGrade)
        val etLetter = dialogView.findViewById<EditText>(R.id.etLetter)
        val etAvgGrade = dialogView.findViewById<EditText>(R.id.etAvgGrade)

        AlertDialog.Builder(this)
            .setTitle("Добавить ученика")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = etName.text.toString().trim()
                val grade = etGrade.text.toString().toIntOrNull() ?: 0
                val letter = etLetter.text.toString().trim()
                val avg = etAvgGrade.text.toString().toFloatOrNull() ?: 0f

                if (name.isNotEmpty()) {
                    dbHelper.addStudent(name, grade, letter, avg)
                    Toast.makeText(this, "Ученик добавлен", Toast.LENGTH_SHORT).show()
                    loadAllStudents()
                } else {
                    Toast.makeText(this, "Введите ФИО ученика", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun loadAllStudents() {
        val students = dbHelper.getAllStudents()
        container.removeAllViews()

        if (students.isEmpty()) {
            val tv = TextView(this)
            tv.text = "Список учеников пуст"
            tv.textSize = 16f
            container.addView(tv)
            return
        }

        for (student in students) {
            val tv = TextView(this)
            tv.text = "${student.id}. ${student.name} — ${student.grade}${student.classLetter} класс, средний балл ${student.averageGrade}"
            tv.textSize = 16f
            tv.setPadding(8, 12, 8, 12)

            // Удаление по долгому нажатию
            tv.setOnLongClickListener {
                dbHelper.deleteStudent(student.id)
                Toast.makeText(this, "Ученик удалён", Toast.LENGTH_SHORT).show()
                loadAllStudents()
                true
            }
            container.addView(tv)
        }
    }
}