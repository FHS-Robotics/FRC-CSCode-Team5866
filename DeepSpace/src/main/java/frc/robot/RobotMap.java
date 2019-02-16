

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PWMSpeedController;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import java.sql.Driver;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.subsystems.UltrasonicSensor;
import frc.robot.vision.VisionManager;
import frc.robot.subsystems.BoschMotor;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.LEDInterface;
import frc.robot.subsystems.LiftSystem;

/**
 * This class contains all of the objects for the robot and a map of where they go
 */
public class RobotMap {
    //#region DriveBase
    public static DifferentialDrive driveBase;

    //left motors
    private static PWMVictorSPX m_frontLeft;
    private static PWMVictorSPX m_rearLeft;
    private static SpeedControllerGroup m_left;

    //right motors
    private static PWMVictorSPX m_frontRight;
    private static PWMVictorSPX m_rearRight;
    private static SpeedControllerGroup m_right;
    //#endregion


    //lift system
    public static PWMVictorSPX liftMotor1;
    public static PWMVictorSPX liftMotor2;
    public static LiftSystem liftSystem;

    //Claw and Pneumatics
    public static Compressor mainC;
    public static DoubleSolenoid clawPistons;
    public static DoubleSolenoid wristPiston;
    public static Claw m_claw;

    //#endregion
    

    //sensors
    public static AHRS navX;
    public static UltrasonicSensor ultraSonicFront;

    public static LEDInterface ledStrip;


    public static void init()
    {
        m_rearLeft = new PWMVictorSPX(3);
        m_frontLeft = new PWMVictorSPX(4);
        m_left = new SpeedControllerGroup(m_frontLeft, m_rearLeft);
    
        m_rearRight = new PWMVictorSPX(1);
        m_frontRight = new PWMVictorSPX(2);
        m_right = new SpeedControllerGroup(m_frontRight, m_rearRight);

        driveBase = new DifferentialDrive(m_left, m_right); //create differential drive using the two speed controller groups

        liftMotor1 = new PWMVictorSPX(0);
        liftMotor2 = new PWMVictorSPX(5);
        liftSystem = new LiftSystem(liftMotor1, liftMotor2,  4, 5);

        mainC = new Compressor(1);

        try {
        mainC.start();
        } catch(Exception e) {
            System.out.println("Error: Could not start compressor");
            System.out.print(e);
        }
        clawPistons = new DoubleSolenoid(0, 1);
        wristPiston = new DoubleSolenoid(2, 3);
        m_claw = new Claw(clawPistons, wristPiston);

        navX = new AHRS(SPI.Port.kMXP); //establish NavX sensor on the MXP port (12 pins on the roborio)
        ultraSonicFront = new UltrasonicSensor(0); //pass in analog pin for the sensor

        ledStrip = new LEDInterface(0, 1, 2, 3); //arduino pins are 0, 1, 3

        //ledStrip = new LEDStrip(4, 5, 6);
    }
}
