package com.soze.r2d;

public abstract class Component {

  private final UiState props = new UiState();
  private final UiState state = new UiState();

  private Runnable stateChangeCallback = () -> {};

  public Component(UiState props) {
    this.props.merge(props);
  }

  public void setState(UiState nextState) {
    state.merge(nextState);
    R.getStateUpdater().accept(() -> stateChangeCallback.run());
  }

  public UiState getProps() {
    return props;
  }

  public UiState getState() {
    return state;
  }

  public Runnable getStateChangeCallback() {
    return stateChangeCallback;
  }

  public void setStateChangeCallback(Runnable stateChangeCallback) {
    this.stateChangeCallback = stateChangeCallback;
  }

  public abstract Element render();

}
