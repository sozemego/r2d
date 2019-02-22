package com.soze.r2d;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UiState {

  private final Map<String, Object> state = new HashMap<>();

  public UiState() {

  }

  public UiState(Map<String, Object> state) {
    this.state.clear();
    this.state.putAll(state);
  }

  public Object get(String key) {
    return state.get(key);
  }

  public <T> T get(String key, T defaultValue) {
    return (T) state.getOrDefault(key, defaultValue);
  }

  public void set(String key, Object value) {
    state.put(key, value);
  }

  public void merge(UiState uiState) {
    for (Map.Entry<String, Object> entry : uiState.entries()) {
      this.state.put(entry.getKey(), entry.getValue());
    }
  }

  private Set<Map.Entry<String, Object>> entries() {
    return state.entrySet();
  }

  public void clear() {
    this.state.clear();
  }

}
