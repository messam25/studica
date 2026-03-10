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

### Using Cursor (no WPILib VS Code)

If you use **Cursor** instead of WPILib VS Code:

1. **Option A — Try the VMX extension in Cursor**  
   Cursor can run many VS Code extensions. Install **VMX-PI WPILib** from the Extensions view (`Ctrl+Shift+X` / `Cmd+Shift+X` → search **VMX**). If it installs, run **Change the deploy target to VMX-Pi (from RoboRIO)** from the Command Palette (`Ctrl+Shift+P` / `Cmd+Shift+P`). Then you can use `./gradlew deploy` from the terminal.

2. **Option B — One-time switch in WPILib VS Code, then use Cursor**  
   If the VMX extension doesn’t work in Cursor:
   - Install [WPILib VS Code](https://docs.wpilib.org) and open this project in it once.
   - Install the **VMX-PI WPILib** extension and run **Change the deploy target to VMX-Pi (from RoboRIO)**.
   - Save and close WPILib VS Code. The `build.gradle` changes are saved.
   - From then on, use **Cursor** for editing and run **`./gradlew deploy`** from the project folder in a terminal (see below). No need to open WPILib VS Code again unless you create a new project or reset the deploy target.

### One-time: Switch to VMX-Pi (required for terminal deploy)

If you use WPILib VS Code:

1. Open this project in **WPILib VS Code** (the FRC/WPILib fork of VS Code from [wpilib.org](https://docs.wpilib.org)).
2. Install the **VMX-PI WPILib** extension (`Ctrl + Shift + X` → search **VMX**).
3. Run: `Ctrl + Shift + P` → **Change the deploy target to VMX-Pi (from RoboRIO)**.
4. This rewrites `build.gradle` so deploy uses the VMX (user `pi`, password `raspberry`) instead of roboRIO (`admin`).

After that, you can deploy from the terminal (see below) or from the IDE.

### Deploy from the terminal

With your PC connected to the VMX network (Wi‑Fi `VMX-1234` or Ethernet):

**macOS / Linux:**
```bash
cd /path/to/studica
# If you use WPILib's JDK (optional): export JAVA_HOME="/path/to/wpilib/2026/jdk/..."
./gradlew deploy
```

**Windows (PowerShell):**
```powershell
cd "d:\my projects\studica"
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk\jdk-17.0.13+11"
.\gradlew.bat deploy
```

In WPILib VS Code you can also use: `Ctrl + Shift + P` → **WPILib: Deploy Robot Code**.

You should see **BUILD SUCCESSFUL**. The code is then on the robot and will start automatically.

### If you see "No more authentication methods available"

That means the project is still targeting **roboRIO** (user `admin`). The VMX expects user **`pi`** and password **`raspberry`**. Do one of the following:

- **Preferred:** Do the one-time **Change the deploy target to VMX-Pi** step (in Cursor if the VMX extension works, or in WPILib VS Code), then run `./gradlew deploy` again.
- **Workaround from Cursor (no extension):** Build and copy the JAR to the VMX with user `pi`:
  ```bash
  ./gradlew build deployVmx
  ```
  When prompted, use password **`raspberry`**. Override VMX IP if needed: `./gradlew deployVmx -PVMX_IP=172.22.11.2`.  
  **Note:** The JAR built with the default (RoboRIO) target may not run on the VMX Pi; for a VMX-compatible build you still need to run **Change the deploy target to VMX-Pi** once in WPILib VS Code, then use Cursor for editing and either `./gradlew deploy` or `./gradlew deployVmx`.

Alternatively on macOS/Linux you can run `chmod +x deploy-vmx.sh` then `./deploy-vmx.sh` (same password when prompted).

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
| **UnsupportedClassVersionError (61.0 vs 55.0)** | VMX needs **Java 17**. Pi has no internet when hosting Wi‑Fi → see **Java 17 on VMX (no internet)** below. |
| **IOException: wpiutiljni could not be loaded** | JAR was built for **roboRIO**, not VMX. Build a VMX JAR: open project in **WPILib VS Code**, run **Change the deploy target to VMX-Pi (from RoboRIO)**, then **WPILib: Build Robot Code**. Redeploy that JAR to the VMX. |
| Wheels spin wrong way | Toggle `setInverted()` in `DriveTrain.java` |
| Battery warning | Replace battery when voltage drops below 11.5 V |

### Java 17 on VMX (no internet)

When the VMX is the Wi‑Fi AP (`VMX-1234`), it often has no internet, so `apt install openjdk-17-jre-headless` fails. Install Java 17 manually:

1. **On your Mac:** Download a Java 17 JRE for **Linux ARM 32-bit**:
   - Go to [Adoptium Temurin 17](https://adoptium.net/temurin/releases/?version=17&os=linux&arch=arm&package=jre), choose **JRE**, **Linux**, **ARM** (32-bit), and download the **.tar.gz**.
   - Or from [GitHub temurin17-binaries](https://github.com/adoptium/temurin17-binaries/releases) pick the asset like `OpenJDK17U-jre_*_aarch32_linux_*.tar.gz` (or `arm_linux_*` for 32-bit).

2. **Copy to the VMX** (Mac connected to `VMX-1234`, or use your SSH key):
   ```bash
   scp OpenJDK17U-jre_*.tar.gz pi@10.12.34.2:/home/pi/
   ```

3. **On the VMX** (SSH `pi@10.12.34.2`):
   ```bash
   cd /home/pi
   tar -xzf OpenJDK17U-jre_*.tar.gz
   ```
   That creates a folder like `jdk-17.0.x+7-jre`. Run the robot with it:
   ```bash
   /home/pi/jdk-17.0.*-jre/bin/java -jar /home/pi/deploy/frc.jar
   ```
   Or use the full path, e.g. `/home/pi/jdk-17.0.13+7-jre/bin/java -jar /home/pi/deploy/frc.jar`.
