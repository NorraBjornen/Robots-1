package logic.state;

public interface HasState {
    WindowState getState();
    void setState(WindowState state);
}
