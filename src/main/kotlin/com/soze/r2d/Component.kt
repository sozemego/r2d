package com.soze.lifegame.uibuilder

abstract class Component(
	val props: UiState = UiState()
) {

	val state: UiState = UiState()
	var stateChangeCallback: () -> Unit = fun() {}

	fun setState(nextState: UiState) {
		state.merge(nextState)
		stateChangeCallback()
	}

	fun setState(vararg nextState: Pair<String, Any?>) {
		val nextUiState = UiState()
		nextState.forEach {
			nextUiState[it.first] = it.second
		}
		setState(nextUiState)
	}


	abstract fun render(): Element

}