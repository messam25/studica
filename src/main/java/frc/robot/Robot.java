package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.DriveTrain;

/**
 * Main robot class for the Studica VMX all-terrain mecanum-drive robot.
 *
 * DRIVING MODES
 * ─────────────
 *  • Analog  – left stick moves, right stick rotates (smooth, proportional)
 *  • Digital – D-pad moves at a fixed speed, bumpers rotate (keyboard-friendly)
 *
 * The D-pad and bumpers override the analog sticks whenever they are active,
 * so both input styles can coexist on a single gamepad.  If you map a
 * keyboard to a virtual joystick (vJoy / DS4Windows / etc.), bind WASD to
 * the D-pad (POV hat) and Q/E to LB/RB for full digital control.
 *
 * ENABLE / DISABLE
 * ────────────────
 *  • Recommended: use Control Station Console on your PC (press 'e' to
 *    enable in Teleop, 'd' to disable).
 *  • Alternative: uncomment the MockDS section in robotInit/robotPeriodic
 *    to enable/disable with the physical Start and Back buttons.
 */
public class Robot extends TimedRobot {

    private DriveTrain driveTrain;
    private Joystick   controller;

    // ── Uncomment for MockDS (autonomous-only, no Control Station) ───
    // private com.studica.frc.MockDS mockDS;
    // private boolean mockEnabled = false;

    // ──────────────────────────────────────────────────────────────────
    // Lifecycle
    // ──────────────────────────────────────────────────────────────────

    @Override
    public void robotInit() {
        driveTrain = new DriveTrain();
        controller = new Joystick(Constants.CONTROLLER_PORT);

        // ── MockDS (uncomment if not using Control Station Console) ──
        // mockDS = new com.studica.frc.MockDS();
    }

    @Override
    public void robotPeriodic() {
        // ── MockDS enable/disable via gamepad (uncomment if needed) ──
        // if (controller.getRawButtonPressed(Constants.BUTTON_START) && !mockEnabled) {
        //     mockDS.enable();
        //     mockEnabled = true;
        // }
        // if (controller.getRawButtonPressed(Constants.BUTTON_BACK) && mockEnabled) {
        //     mockDS.disable();
        //     mockEnabled = false;
        // }
    }

    // ──────────────────────────────────────────────────────────────────
    // Teleoperated (requires Control Station Console in Teleop mode)
    // ──────────────────────────────────────────────────────────────────

    @Override
    public void teleopPeriodic() {
        double forward  = 0.0;
        double strafe   = 0.0;
        double rotation = 0.0;

        // ── 1. Analog stick input ────────────────────────────────────
        forward  = deadband(-controller.getRawAxis(Constants.AXIS_LEFT_Y));
        strafe   = deadband( controller.getRawAxis(Constants.AXIS_LEFT_X));
        rotation = deadband( controller.getRawAxis(Constants.AXIS_RIGHT_X));

        // ── 2. D-pad override (digital / keyboard-style) ────────────
        int pov = controller.getPOV();
        if (pov != -1) {
            double ds = Constants.DIGITAL_SPEED;
            double rad = Math.toRadians(pov);
            // POV 0 = up/forward, 90 = right, etc.
            forward = ds * -Math.cos(rad);   // cos(0)=1  → forward
            strafe  = ds *  Math.sin(rad);   // sin(90)=1 → right
        }

        // ── 3. Bumper rotation override ──────────────────────────────
        if (controller.getRawButton(Constants.BUTTON_LEFT_BUMPER)) {
            rotation = -Constants.DIGITAL_SPEED;
        }
        if (controller.getRawButton(Constants.BUTTON_RIGHT_BUMPER)) {
            rotation = Constants.DIGITAL_SPEED;
        }

        driveTrain.drive(forward, strafe, rotation);
    }

    @Override
    public void teleopExit() {
        driveTrain.stop();
    }

    // ──────────────────────────────────────────────────────────────────
    // Autonomous (runs when enabled via MockDS or Control Station 'a')
    // ──────────────────────────────────────────────────────────────────

    private long autoStartTime;

    @Override
    public void autonomousInit() {
        autoStartTime = System.currentTimeMillis();
    }

    @Override
    public void autonomousPeriodic() {
        long elapsed = System.currentTimeMillis() - autoStartTime;
        if (elapsed < 2000) {
            driveTrain.drive(0.3, 0.0, 0.0);   // drive forward 2 s
        } else if (elapsed < 3000) {
            driveTrain.drive(0.0, 0.0, 0.3);   // rotate 1 s
        } else {
            driveTrain.stop();
        }
    }

    @Override
    public void autonomousExit() {
        driveTrain.stop();
    }

    // ──────────────────────────────────────────────────────────────────
    // Disabled
    // ──────────────────────────────────────────────────────────────────

    @Override
    public void disabledInit() {
        driveTrain.stop();
    }

    // ──────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────

    private static double deadband(double value) {
        return Math.abs(value) < Constants.DEADBAND ? 0.0 : value;
    }
}
