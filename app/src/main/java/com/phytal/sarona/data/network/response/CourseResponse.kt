package com.phytal.sarona.data.network.response

import com.phytal.sarona.data.db.entities.Course

data class CourseResponse(
    val courses: List<List<Course>>
//    ,
//    val message: String,
//    val oldAssignmentList: List<Course>,
//    val reportCardList1: List<Course>,
//    val reportCardList2: List<Course>,
//    val reportCardList3: List<Course>,
//    val reportCardList4: List<Course>
)