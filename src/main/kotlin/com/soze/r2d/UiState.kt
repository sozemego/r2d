package com.soze.lifegame.uibuilder

data class UiState(
	val state: MutableMap<String, Any?> = HashMap()
) {

	constructor(vararg nextState: Pair<String, Any?>) : this() {
		nextState.forEach {
			state[it.first] = it.second
		}
	}

	operator fun get(key: String): Any? {
		return state[key]
	}

	operator fun set(key: String, value: Any?) {
		state[key] = value
	}

	private fun entries(): Set<Map.Entry<String, Any?>> {
		return state.entries
	}

	fun merge(uiState: UiState) {
		uiState.entries().forEach {
			set(it.key, it.value)
		}
	}

	fun merge(vararg nextState: Pair<String, Any?>) {
		nextState.forEach {
			state[it.first] = it.second
		}
	}

	fun clear() {
		state.clear()
	}

}