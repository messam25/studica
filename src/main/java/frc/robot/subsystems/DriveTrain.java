package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.Constants;

/**
 * Four-wheel mecanum drive subsystem.
 *
 * Wheel layout (top-down view):
 *
 *   FL ╲  ╱ FR        PWM 0   PWM 2
 *        ╳
 *   RL ╱  ╲ RR        PWM 1   PWM 3
 *
 * Right-side motors are inverted so that positive values always mean
 * "forward" for every wheel.
 */
public class DriveTrain {

    private final PWMSparkMax frontLeft;
    private final PWMSparkMax rearLeft;
    private final PWMSparkMax frontRight;
    private final PWMSparkMax rearRight;
    private final MecanumDrive mecanumDrive;

    public DriveTrain() {
        frontLeft  = new PWMSparkMax(Constants.FRONT_LEFT_MOTOR_PORT);
        rearLeft   = new PWMSparkMax(Constants.REAR_LEFT_MOTOR_PORT);
        frontRight = new PWMSparkMax(Constants.FRONT_RIGHT_MOTOR_PORT);
        rearRight  = new PWMSparkMax(Constants.REAR_RIGHT_MOTOR_PORT);

        frontRight.setInverted(true);
        rearRight.setInverted(true);

        mecanumDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
        mecanumDrive.setDeadband(Constants.DEADBAND);
    }

    /**
     * Cartesian mecanum drive.
     *
     * @param xSpeed   forward / backward  [-1, 1]  (positive = forward)
     * @param ySpeed   strafe left / right  [-1, 1]  (positive = right)
     * @param rotation clockwise rotation   [-1, 1]  (positive = clockwise)
     */
    public void drive(double xSpeed, double ySpeed, double rotation) {
        double cap = Constants.MAX_SPEED;
        xSpeed   = clamp(xSpeed   * cap);
        ySpeed   = clamp(ySpeed   * cap);
        rotation = clamp(rotation * cap);
        mecanumDrive.driveCartesian(xSpeed, ySpeed, rotation);
    }

    /** Set each wheel individually (values in [-1, 1]). */
    public void driveRaw(double fl, double rl, double fr, double rr) {
        frontLeft.set(clamp(fl));
        rearLeft.set(clamp(rl));
        frontRight.set(clamp(fr));
        rearRight.set(clamp(rr));
    }

    public void stop() {
        mecanumDrive.stopMotor();
    }

    private static double clamp(double v) {
        return Math.max(-1.0, Math.min(1.0, v));
    }
}
