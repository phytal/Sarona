package com.phytal.sarona.models

object Model {
    data class Result(val currentAssignmentList	: List<Course>)
    //data class List(val courseList: CourseList)
}

class Course(val courseId: String, val courseName: String, val courseGrade: Double)
