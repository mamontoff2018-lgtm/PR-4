package com.example.sqlitelab

data class Student(
    val id: Int = 0,
    var name: String,
    var grade: Int,
    var classLetter: String,
    var averageGrade: Float
)