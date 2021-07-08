package com.phytal.sarona.data.network.response

import com.phytal.sarona.data.db.entities.MarkingPeriod

data class CurrentMpResponse(
    val currentMp: MarkingPeriod
)