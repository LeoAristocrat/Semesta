package com.leoaristocrat.semesta.core.datastore

import com.leoaristocrat.semesta.feature_expenses.domain.StudentFee
import org.json.JSONArray
import org.json.JSONObject

/** Shared by DataStore and local/cloud backups. Unknown/malformed entries are skipped. */
object StudentFeesJson {
    fun encode(fees: List<StudentFee>): JSONArray = JSONArray().apply {
        fees.forEach { fee -> put(JSONObject().put("id", fee.id).put("name", fee.name)
            .put("amount", fee.amount).put("paid", fee.paid).put("dueEpochDay", fee.dueEpochDay)) }
    }
    fun decode(raw: String?): List<StudentFee> = runCatching {
        val array = JSONArray(raw ?: "[]")
        (0 until array.length()).mapNotNull { index ->
            runCatching {
                val value = array.getJSONObject(index)
                StudentFee(value.getString("id"), value.getString("name"), value.getInt("amount"),
                    value.getInt("paid"), value.getLong("dueEpochDay")).takeIf { it.valid }
            }.getOrNull()
        }.distinctBy { it.id }
    }.getOrDefault(emptyList())
}
