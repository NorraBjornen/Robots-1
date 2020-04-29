package utils;

import java.util.function.Function;

/**
 * Utility class для работы с геометрией робота
 */
public class GeometryUtils {

    /**
     * Рассчитывает угол в полярных координатах от робота до цели
     * и нормализует результат в зависимости от текущего направления робота
     */
    public static double angleTo(double fromX, double fromY, double toX, double toY, double direction)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        double angle = Math.atan2(diffY, diffX);

        if(direction > angle) {
            while (Math.abs(direction - angle) >= Math.PI) {
                angle += 2 * Math.PI;
            }
        } else if (direction < angle) {
            while (Math.abs(direction - angle) >= Math.PI) {
                angle -= 2 * Math.PI;
            }
        }

        return angle;
    }

    /**
     * Нормализует угол
     */
    public static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    /**
     * Возвращает расстояние от точки (x1, y1) до точки (x2, y2)
     */
    public static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Рассчитывает угловую скорость робота в зависимости от угла до цели, направления робота и максимальной угловой скорости
     */
    public static double calculateAngularVelocity(double angleToTarget, double direction, double maxAngularVelocity) {
        double velocity = 0;
        if (angleToTarget > direction) {
            velocity = Math.min((angleToTarget - direction) / 10, maxAngularVelocity);
        } else if (angleToTarget < direction) {
            velocity = Math.max((angleToTarget - direction) / 10, -maxAngularVelocity);
        }

        return velocity;
    }

    /**
     * Рассчитывает новую координату X для робота при атомарной операции движения, т.е. Robot.move
     */
    public static double calculateX(double x, double velocity, double angularVelocity, double duration, double direction) {
        double newX = x + calculateDiff(velocity, angularVelocity, duration, direction, Math::sin);
        if (!Double.isFinite(newX)) {
            newX = x + calculateNonFiniteDiff(velocity, duration, direction, Math::cos);
        }
        return newX;
    }

    /**
     * Рассчитывает новую координату Y для робота при атомарной операции движения, т.е. Robot.move
     */
    public static double calculateY(double y, double velocity, double angularVelocity, double duration, double direction) {
        double newY = y - calculateDiff(velocity, angularVelocity, duration, direction, Math::cos);
        if (!Double.isFinite(newY)) {
            newY = y + calculateNonFiniteDiff(velocity, duration, direction, Math::sin);
        }
        return newY;
    }

    /**
     * Рассчитывает расстояние для атомарной операции движения, т.е. Robot.move
     */
    private static double calculateDiff(double velocity, double angularVelocity, double duration, double direction, Function<Double, Double> function) {
        return velocity / angularVelocity *
                (function.apply(direction + angularVelocity * duration) - function.apply(direction));
    }

    /**
     * Рассчитывает расстояние для атомарной операции движения, т.е. Robot.move, в том случае,
     * если ранее рассчитанная разница не являлась конечной
     */
    private static double calculateNonFiniteDiff(double velocity, double duration, double direction, Function<Double, Double> function) {
        return velocity * duration * function.apply(direction);
    }
}
