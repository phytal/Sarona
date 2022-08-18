package com.phytal.sarona.data.network.response

import com.phytal.sarona.data.db.entities.MarkingPeriod

data class OtherMpResponse (
    val otherMps: List<MarkingPeriod>
)