package com.example.sqlitelab

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "school.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "students"

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_GRADE = "grade"
        const val COLUMN_CLASS_LETTER = "class_letter"
        const val COLUMN_AVERAGE_GRADE = "average_grade"
    }

    private val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_GRADE INTEGER NOT NULL,
            $COLUMN_CLASS_LETTER TEXT NOT NULL,
            $COLUMN_AVERAGE_GRADE REAL
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Добавление ученика
    fun addStudent(name: String, grade: Int, classLetter: String, averageGrade: Float): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_GRADE, grade)
            put(COLUMN_CLASS_LETTER, classLetter)
            put(COLUMN_AVERAGE_GRADE, averageGrade)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    // Получение всех учеников
    fun getAllStudents(): ArrayList<Student> {
        val list = ArrayList<Student>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val grade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE))
                val classLetter = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_LETTER))
                val avgGrade = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AVERAGE_GRADE))

                list.add(Student(id, name, grade, classLetter, avgGrade))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    // Удаление ученика
    fun deleteStudent(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}