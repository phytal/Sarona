package com.phytal.sarona.data.network.response

import com.phytal.sarona.data.db.entities.MarkingPeriod

data class PastMpResponse (
    val pastMps: List<MarkingPeriod>
)