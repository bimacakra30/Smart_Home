package com.karebet.smarthome.model

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RelayViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance().reference

    private val _relayStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val relayStates = _relayStates.asStateFlow()

    private val _scheduleEnabled = MutableStateFlow(false)
    val scheduleEnabled = _scheduleEnabled.asStateFlow()

    private val _onTime = MutableStateFlow("")
    val onTime = _onTime.asStateFlow()

    private val _offTime = MutableStateFlow("")
    val offTime = _offTime.asStateFlow()

    private val _scheduleRelays = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val scheduleRelays = _scheduleRelays.asStateFlow()

    init {
        db.child("relay").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val states = snapshot.children.associate {
                    it.key!! to (it.getValue(Boolean::class.java) ?: false)
                }
                _relayStates.value = states
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        db.child("schedule").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _scheduleEnabled.value = snapshot.child("enabled").getValue(Boolean::class.java) ?: false
                _onTime.value = snapshot.child("onTime").getValue(String::class.java) ?: ""
                _offTime.value = snapshot.child("offTime").getValue(String::class.java) ?: ""

                val scheduledRelays = snapshot.children
                    .filter { it.key?.startsWith("relay") == true }
                    .associate { it.key!! to (it.getValue(Boolean::class.java) ?: false) }
                _scheduleRelays.value = scheduledRelays
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun toggleRelay(key: String, value: Boolean) {
        db.child("relay").child(key).setValue(value)
    }

    fun toggleSchedule(enabled: Boolean) {
        db.child("schedule/enabled").setValue(enabled)
    }

    fun toggleAllRelay(turnOn: Boolean) {
        relayStates.value.keys.forEach { key ->
            db.child("relay/$key").setValue(turnOn)
        }
    }

    fun setOnTime(time: String) {
        db.child("schedule/onTime").setValue(time)
    }

    fun setOffTime(time: String) {
        db.child("schedule/offTime").setValue(time)
    }

    fun setRelayScheduled(key: String, enabled: Boolean) {
        db.child("schedule/$key").setValue(enabled)
    }
}