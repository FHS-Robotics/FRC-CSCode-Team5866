/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.commands.TeleOpDrive;
import frc.robot.commands.boschmotor.BoschMotorContinuous;
import frc.robot.commands.cargodelivery.FindTargetsPeriodic;
import frc.robot.commands.TeleOpLift;
import frc.robot.commands.SetLEDModeAuto;
import frc.robot.commands.ultrasonic.*;
import frc.robot.vision.VisionManager;
import frc.robot.RobotMap;

import java.text.DecimalFormat;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.*;

/**
 * This class is at the highest level and initializes all of our subsystems, RobotMap, commands, etc
 */
public class Robot extends TimedRobot {

  Timer timer; //for ultrasonic sensor
  double refreshRate = .5; //for ultrasonic

  OI m_oi;

  public static UsbCamera main;

  @Override
  public void robotInit() 
  {
    RobotMap.init();
    m_oi = new OI();
    VisionManager.init();

    timer = new Timer(); //initialize timer for the ultrasonic reading
    timer.start();

    //make sure the navX sensor is reset
    RobotMap.navX.reset();
    RobotMap.navX.resetDisplacement();

    main = CameraServer.getInstance().startAutomaticCapture(0); //start camera server
    main.setResolution(310, 240); //set resolution of camera

    //set our led color
    Command setAutoLED = new SetLEDModeAuto();
    setAutoLED.start();
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() 
  {
    SmartDashboard.putNumber("Joystick Sensitivity", OI.sensitivity); //display current joystick sensitivity to the dashboard

    //start lift command
    Command lift = new TeleOpLift();
    lift.start();

    //set our led color
    Command setAutoLED = new SetLEDModeAuto();
    setAutoLED.start();

    //start finding targets
    Command findTargets = new FindTargetsPeriodic();
    findTargets.start();
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopPeriodic() 
  {
    //get range of ultrasonic sensor every x seconds
    if(timer.get() > refreshRate)
    {
      Command range = new GetRange(RobotMap.ultraSonicFront);
      
      //put the current position of the navX sensor to the dashboard
      AHRS navX = RobotMap.navX;
      DecimalFormat df = new DecimalFormat("###.###"); //set format layout
      SmartDashboard.putString("Robot X Pos: ", df.format(navX.getDisplacementX()));
      SmartDashboard.putString("Robot Y Pos: ", df.format(navX.getDisplacementY()));
      
      SmartDashboard.putNumber("Robot Yaw: ", navX.getYaw());
      range.start();
      timer.reset();
    }
    Command teleOp = new TeleOpDrive();
    teleOp.start();

    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousPeriodic(){
    teleopPeriodic(); //just do the same stuff as teleopPeriodic
  }

}
