package logic.state;

public interface StateSaveable {
    WindowState saveState();
    void restoreState(WindowState state);
}
