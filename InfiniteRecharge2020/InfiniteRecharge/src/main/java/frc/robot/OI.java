package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.SwitchDriveMode;

/**
 * OI
 */
public class OI {

    public static Joystick m_driverControl = new Joystick(0);
    public static Joystick m_gunnerControl = new Joystick(1);
    
    public static JoystickButton switchDrive;

    public OI() {
        switchDrive = new JoystickButton(m_driverControl, 0);
        switchDrive.whenPressed(new SwitchDriveMode()); //switch drive mode when this button is pressed
    }
}