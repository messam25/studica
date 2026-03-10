# Studica VMX All-Terrain Mecanum Drive Robot

A WPILib Java project for the Studica VMX robotics controller with four-wheel
mecanum drive, gamepad analog + digital (D-pad / keyboard) control, and
optional MockDS support.

---

## Prerequisites

| Tool | Purpose |
|------|---------|
| **WPILib VS Code** (2026) | IDE + Gradle + WPILib libraries |
| **VMX-PI WPILib extension** | Retargets the build from roboRIO → VMX |
| **Control Station Console** | Enables / disables robot, forwards gamepad |
| **USB gamepad (Xbox-style)** | Drives the robot in Teleoperated mode |

Install WPILib:  
<https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html>

Download Control Station Console:  
<https://docs.dev.studica.com/en/latest/docs/ControlStation/>

---

## One-Time Setup

> The Gradle wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`) is already
> included — copied from your WPILib 2026 installation.

### 1. Install the VMX-PI Extension

1. In VS Code: `Ctrl + Shift + X` → search **VMX** → install **VMX-PI WPILib**
2. `Ctrl + Shift + P` → **Change the deploy target to VMX-Pi (from RoboRIO)**
3. This rewrites `build.gradle` for VMX deployment automatically.
4. `Ctrl + Shift + P` → **Verify the Project's build.gradle file** (should pass)

### 2. Install the Studica Vendor Library

1. `Ctrl + Shift + P` → **WPILib: Manage Vendor Libraries**
2. Select **Install new libraries (online)**
3. Paste the URL:
   ```
   https://dev.studica.com/releases/2026/Studica2026.json
   ```
4. Build & cache when prompted.

> This is needed if you uncomment the MockDS code in `Robot.java`.

---

## Build

```bash
./gradlew build
```

Or: `Ctrl + Shift + P` → **WPILib: Build Robot Code**

Fix any errors before deploying.

---

## Connect to the Robot

### Wi-Fi (Access Point — default)

| Field | Value |
|-------|-------|
| SSID | `VMX-1234` |
| Password | `password` |
| Robot IP | `10.12.34.2` |

1. Power on the VMX and wait ~30 seconds.
2. On your PC, connect to the `VMX-1234` Wi-Fi network.
3. Confirm connectivity:
   ```
   ping 10.12.34.2
   ```
4. (Optional) SSH in to verify:
   ```
   ssh pi@10.12.34.2          # password: raspberry
   ```

### Ethernet (preferred for speed)

Plug in an Ethernet cable — the VMX is always at `172.22.11.2`.

---

## Deploy Code to the Robot

**Important:** This project is set up for **WPILib 2026** and targets **roboRIO** by default.  
The VMX uses different SSH credentials (`pi` / `raspberry`), so **you must switch the deploy target to VMX-Pi once** before `gradlew deploy` will work.

### One-time: Switch to VMX-Pi (required for terminal deploy)

1. Open this project in **WPILib VS Code** (the FRC/WPILib fork of VS Code from [wpilib.org](https://docs.wpilib.org)).
2. Install the **VMX-PI WPILib** extension (`Ctrl + Shift + X` → search **VMX**).
3. Run: `Ctrl + Shift + P` → **Change the deploy target to VMX-Pi (from RoboRIO)**.
4. This rewrites `build.gradle` so deploy uses the VMX (user `pi`, password `raspberry`) instead of roboRIO (`admin`).

After that, you can deploy from the terminal (see below) or from the IDE.

### Deploy from the terminal

With your PC connected to the VMX network (Wi‑Fi `VMX-1234` or Ethernet):

```powershell
cd "d:\my projects\studica"
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk\jdk-17.0.13+11"
.\gradlew.bat deploy
```

Or in WPILib VS Code: `Ctrl + Shift + P` → **WPILib: Deploy Robot Code**.

You should see **BUILD SUCCESSFUL**. The code is then on the robot and will start automatically.

### If you see "No more authentication methods available"

That means the project is still targeting **roboRIO** (user `admin`). The VMX expects user **`pi`** and password **`raspberry`**. Follow the one-time steps above in **WPILib VS Code** to run **Change the deploy target to VMX-Pi**, then try `.\gradlew.bat deploy` again.

---

## Run & Test

### Using Control Station Console (recommended)

1. Plug a USB **Xbox-style gamepad** into your PC.
2. Open **Control Station Console**.
3. Enter the robot IP (`10.12.34.2` or `172.22.11.2`).
4. Wait until **Robot Comms**, **Robot Code**, and **Joysticks** indicators
   are green.
5. Press **`o`** to select Teleoperated mode.
6. Press **`e`** to enable the robot.
7. **Drive!** (see input map below)
8. Press **`d`** to disable at any time.

| Key | Action |
|-----|--------|
| `e` | Enable robot |
| `d` | Disable robot |
| `o` | Teleoperated mode |
| `a` | Autonomous mode |
| `t` | Test mode |
| `q` | Quit console |

### Using MockDS (no Control Station)

Uncomment the MockDS lines in `Robot.java` (robotInit + robotPeriodic),
install the Studica vendor library, rebuild, and redeploy. Then press the
physical **Start** button to enable and **E-Stop** to disable.

> MockDS only enables in *autonomous* mode. Teleoperated driving requires
> Control Station Console.

---

## Controller Input Map

### Analog Control (Joystick Sticks)

| Input | Axis | Robot Action |
|-------|------|--------------|
| Left Stick Y | axis 1 | Forward / Backward |
| Left Stick X | axis 0 | Strafe Left / Right |
| Right Stick X | axis 4 | Rotate Left / Right |

### Digital Control (D-Pad + Bumpers)

When the D-pad is pressed it overrides the analog sticks at a fixed speed.

| Input | Robot Action |
|-------|--------------|
| D-pad Up | Drive forward |
| D-pad Down | Drive backward |
| D-pad Left | Strafe left |
| D-pad Right | Strafe right |
| D-pad diagonals | Combined movement |
| Left Bumper (LB) | Rotate left (counter-clockwise) |
| Right Bumper (RB) | Rotate right (clockwise) |

### Keyboard Mapping (via Virtual Joystick)

If you want to drive with a keyboard instead of a gamepad:

1. Install **vJoy** (<https://sourceforge.net/projects/vjoy/>) to create a
   virtual joystick device.
2. Install **UCR** or **JoyToKey** to map keyboard keys → virtual joystick.
3. Map keys to the **D-pad (POV hat)** and **buttons 5 / 6 (bumpers)**:

| Key | Maps To | Robot Action |
|-----|---------|--------------|
| W | POV 0 (Up) | Forward |
| S | POV 180 (Down) | Backward |
| A | POV 270 (Left) | Strafe left |
| D | POV 90 (Right) | Strafe right |
| Q | Button 5 (LB) | Rotate left |
| E | Button 6 (RB) | Rotate right |

Control Station Console will see the virtual joystick like any real gamepad.

---

## How the Four Wheels Move

Mecanum wheels have rollers at 45° that let the robot translate in any
direction. Here is what each wheel does for each motion:

| Motion | FL | RL | FR | RR |
|--------|----|----|----|-----|
| Forward | + | + | + | + |
| Backward | − | − | − | − |
| Strafe Right | + | − | − | + |
| Strafe Left | − | + | + | − |
| Rotate CW | + | + | − | − |
| Rotate CCW | − | − | + | + |

`+` = forward spin, `−` = reverse spin.  
`MecanumDrive.driveCartesian()` handles all of this automatically.

---

## Customization

| What | Where |
|------|-------|
| Motor PWM port numbers | `Constants.java` → `*_MOTOR_PORT` |
| Controller axis/button indices | `Constants.java` → `AXIS_*` / `BUTTON_*` |
| Speed limit | `Constants.java` → `MAX_SPEED` |
| Digital drive speed | `Constants.java` → `DIGITAL_SPEED` |
| Deadband | `Constants.java` → `DEADBAND` |
| Team number / IP | `Constants.java` → `TEAM_NUMBER`, `VMX_IP_*` |
| Invert a motor | `DriveTrain.java` constructor |
| Autonomous routine | `Robot.java` → `autonomousPeriodic()` |

---

## Project Structure

```
studica/
├── build.gradle                  ← Gradle build (converted by VMX extension)
├── settings.gradle
├── .wpilib/
│   └── wpilib_preferences.json   ← WPILib project metadata
├── vendordeps/                   ← Vendor library JSON files
├── gradle/wrapper/               ← Gradle wrapper (copy from WPILib template)
├── gradlew / gradlew.bat         ← Gradle launcher (copy from WPILib template)
└── src/main/java/frc/robot/
    ├── Main.java                 ← Entry point
    ├── Robot.java                ← Lifecycle + input handling + MockDS
    ├── Constants.java            ← All tuneable values
    └── subsystems/
        └── DriveTrain.java       ← Mecanum drive with four PWM motors
```

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `gradlew` not found | Wrapper is included; run from project root |
| Build fails with plugin error | Run VMX extension: *Change deploy target to VMX-Pi* |
| Cannot ping robot | Check Wi-Fi connection to `VMX-1234` |
| Deploy fails | Ensure you can `ping 10.12.34.2`; check battery |
| "No more authentication methods" | Project still targets roboRIO; run **Change deploy target to VMX-Pi** in WPILib VS Code |
| **"Could not set unknown property 'address'"** | The deploy block has an `address = "10.12.34.2"` (or similar) line. Remove it — the standard RoboRIO target doesn’t support `address`. Keep only `team` and `debug` in the `roborio { }` block. |
| Plugin / Gradle error after VMX conversion | Run **WPILib: Deploy Robot Code** from inside WPILib VS Code instead of the terminal |
| Robot doesn't move | Verify **Robot Code** + **Joysticks** are green in Control Station |
| Wheels spin wrong way | Toggle `setInverted()` in `DriveTrain.java` |
| Battery warning | Replace battery when voltage drops below 11.5 V |
