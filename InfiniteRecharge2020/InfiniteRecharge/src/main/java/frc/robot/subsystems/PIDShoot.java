/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.RobotMap;
/**
 * Class to calculate distance from the limelight and shoot that distance
 */
public class PIDShoot extends PIDSubsystem {

  double h1 = 0; //height of limelight lense from the ground
  double h2 = 0; //height of vision target
  double h3 = 0; //height of inner port
  double a1 = 0; //angle of limelight lense from the horizontal
  double a2; //angle of target from the center line of the limelight lense;
  double d; //distance from the vision target on the outer port;
  double d2 = 0; //distance of inner port wall from the outer port

  public PIDShoot() {
    super(
        // The PIDController used by the subsystem
        new PIDController(0, 0, 0));
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return 0;
  }

  public double getDistance() {
    a2 = RobotMap.limeLight.getY(); //set a2 to the angle of the target in the y
    d = (h2 - h1) / Math.tan(a1 + a2);
    d += d2;
    return d;
  }
}
