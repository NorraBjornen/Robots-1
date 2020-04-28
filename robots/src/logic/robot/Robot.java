package logic.robot;

import gui.RobotObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static utils.GeometryUtils.*;

/**
 * Модель данных, описывающая поведение робота
 */
public class Robot {

    /**
     * Изначальные координаты робота
     */
    private volatile double x = 100;
    private volatile double y = 100;
    private volatile double direction = 0;

    /**
     * Поля, значения которых ограничивают скорость движения робота
     */
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    /**
     * Список подписчиков RobotObserver на изменение состояния модели
     */
    private List<RobotObserver> observers = new ArrayList<>();

    /**
     * Таймер, вызывающий событие движения робота к цели
     */
    private Timer timer;

    /**
     * Метод, начинающий движение робота
     * Метод планирует событие движения робота
     *
     * @param targetX координата x цели
     * @param targetY координата y цели
     */
    public void startMoving(int targetX, int targetY) {
        if (timer != null)
            timer.cancel();
        timer = new Timer("move generator", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                move(targetX, targetY);
            }
        }, 0, 10);
    }

    /**
     * Метод, меняющий координаты и направление робота в зависимости от текущего положения
     * самого робота и цели
     *
     * @param targetX координата x цели
     * @param targetY координата y цели
     */
    private void move(int targetX, int targetY) {
        double distance = distance(targetX, targetY, x, y);
        if (distance < 0.5) {
            timer.cancel();
            return;
        }
        double angleToTarget = angleTo(x, y, targetX, targetY, direction);
        double angularVelocity = calculateAngularVelocity(angleToTarget, direction, maxAngularVelocity);

        double velocity = maxVelocity;
        double duration = 10;

        synchronized (this) {
            x = calculateX(x, velocity, angularVelocity, duration, direction);
            y = calculateY(y, velocity, angularVelocity, duration, direction);
            direction = asNormalizedRadians(direction + angularVelocity * duration);
        }

        for (RobotObserver observer : observers) {
            observer.update(x, y, direction);
        }
    }

    /**
     * Метод, позволяющий подписаться на изменение состояния модели
     *
     * @param observer подписчик, желающий отслеживать изменение состояния модели
     */
    public void subscribe(RobotObserver observer) {
        observers.add(observer);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDirection() {
        return direction;
    }
}
