/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargodelivery;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.robot.RobotMap;
import frc.robot.commands.TeleOpLift;
import frc.robot.subsystems.LiftSystem;
import frc.robot.subsystems.UltrasonicSensor;
import frc.robot.vision.VisionManager;

/**
 * Designed to use the vision processing received by the VisionManager over
 * the network tables from the computer-side to find the vision tape on the rocket,
 * move until the ultrasonic sensor reads a specified distance, and then finishes
 * <br/>
 * <br/>
 * (Was not used during the 2019 season due to it being nonfunctional somehow)
 */
public class MoveToTarget extends Command implements PIDOutput{

  public double period = 2000; //relock every 3 seconds
  private Timer timer;

  private double finalDistance = 20;

  //#region Navigation
  private AHRS navX;
  private PIDController turnController;
  private double rotateToAngleRate;
  private double targetHeading; //the angle in degrees we need to add to our original heading
  private double actualHeading; //our actual heading
  private double originalHeading; //the original rotation we were on

   /* The following PID Controller coefficients will need to be tuned */
  /* to match the dynamics of your drive system.  Note that the      */
  /* SmartDashboard in Test mode has support for helping you tune    */
  /* controllers by displaying a form where you can enter new P, I,  */
  /* and D constants and test the mechanism.                         */
  
  static final double kP = 0.03;
  static final double kI = 0.00;
  static final double kD = 0.00;
  static final double kF = 0.00;

  /* This tuning parameter indicates how close to "on target" the    */
  /* PID Controller will attempt to get.                             */

  static final double kToleranceDegrees = 2.0f;
  //#endregion

  private UltrasonicSensor ultrasonicFront;

  public MoveToTarget() {
    requires(RobotMap.ultraSonicFront);
    navX = RobotMap.navX;
    ultrasonicFront = RobotMap.ultraSonicFront;

    timer = new Timer();

    turnController = new PIDController(kP, kI, kD, kF, navX, this);
    turnController.setInputRange(-180.0f,  180.0f);
    turnController.setOutputRange(-1.0, 1.0);
    turnController.setAbsoluteTolerance(kToleranceDegrees);
    turnController.setContinuous(true);

    /* Add the PID Controller to the Test-mode dashboard, allowing manual  */
      /* tuning of the Turn Controller's P, I and D coefficients.            */
      /* Typically, only the P value needs to be modified.                   */
      //LiveWindow.addActuator("DriveSystem", "RotateController", turnController);
  }

  @Override
  /* This function is invoked periodically by the PID Controller, */
  /* based upon navX-MXP yaw angle input and PID Coefficients.    */
  public void pidWrite(double output) {
    rotateToAngleRate = output;
  }

  @Override
  protected void initialize() {
    timer.start();
    originalHeading = navX.getYaw();
    VisionManager.FindTargets();
    //targetHeading = VisionManager.FindHeading();
    targetHeading = -90; //testing this out
    turnController.setSetpoint(originalHeading + targetHeading);

    System.out.println("Moving to target");
  }

  @Override
  protected void execute() {

    if(timer.hasPeriodPassed(period))
    {
      originalHeading = navX.getYaw();
      VisionManager.FindTargets();
      targetHeading = VisionManager.FindHeading();
      turnController.setSetpoint(originalHeading + targetHeading); //the rotation we want to get to is based on our original rotation
      timer.reset();

      System.out.println("Relocating targets...");
    }

    turnController.enable();
    try {

      RobotMap.driveBase.arcadeDrive(1, rotateToAngleRate); //drive based on the rotation rate from the pid controler
      System.out.println("Rotating...");
      System.out.println("navx heading: " + navX.getYaw());
    } catch( RuntimeException ex ) {

      DriverStation.reportError("Error communicating with drive system:  " + ex.getMessage(), true);
      System.out.println("It didn't work :/");
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    System.out.println("Finished!");
    return false; //(ultrasonicFront.GetRangeInCM() <= finalDistance);
  }
}
