package frc.robot;

public final class Constants {

    private Constants() {}

    // ── Motor PWM Ports ──────────────────────────────────────────────
    // Adjust these to match your physical wiring on the VMX.
    public static final int FRONT_LEFT_MOTOR_PORT  = 0;
    public static final int REAR_LEFT_MOTOR_PORT   = 1;
    public static final int FRONT_RIGHT_MOTOR_PORT = 2;
    public static final int REAR_RIGHT_MOTOR_PORT  = 3;

    // ── Gamepad / Joystick ───────────────────────────────────────────
    public static final int CONTROLLER_PORT = 0;

    // Xbox-style axis indices
    public static final int AXIS_LEFT_X  = 0;
    public static final int AXIS_LEFT_Y  = 1;
    public static final int AXIS_RIGHT_X = 4;

    // Xbox-style button indices
    public static final int BUTTON_A             = 1;
    public static final int BUTTON_B             = 2;
    public static final int BUTTON_X             = 3;
    public static final int BUTTON_Y             = 4;
    public static final int BUTTON_LEFT_BUMPER   = 5;
    public static final int BUTTON_RIGHT_BUMPER  = 6;
    public static final int BUTTON_BACK          = 7;
    public static final int BUTTON_START         = 8;

    // ── Drive Tuning ─────────────────────────────────────────────────
    /** Inputs below this magnitude are treated as zero. */
    public static final double DEADBAND = 0.05;

    /** Global speed cap applied to all mecanum outputs (0.0 – 1.0). */
    public static final double MAX_SPEED = 0.80;

    /** Fixed speed used for D-pad / bumper digital control. */
    public static final double DIGITAL_SPEED = 0.45;

    // ── Networking ───────────────────────────────────────────────────
    public static final int TEAM_NUMBER = 1234;
    public static final String VMX_IP_WIFI     = "10.12.34.2";
    public static final String VMX_IP_ETHERNET = "172.22.11.2";
}
