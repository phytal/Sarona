package com.phytal.sarona.data.network.response

import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.db.entities.CurrentCourse

data class CourseResponse(
    val currentAssignmentList: List<CurrentCourse>,
    val message: String,
    val oldAssignmentList: List<Course>,
    val reportCardList1: List<Course>,
    val reportCardList2: List<Course>,
    val reportCardList3: List<Course>,
    val reportCardList4: List<Course>
)