/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotMap;
import frc.robot.subsystems.TeleHook;

public class CableSystem extends CommandBase {
  /**
   * Creates a new HookSystem.
   */
  TeleHook m_Telecables;

  public CableSystem(boolean extend) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotMap.m_TeleHooks);
    if(extend){
      m_Telecables.extend();
    }
    else{
      m_Telecables.retract();
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_Telecables = RobotMap.m_TeleCables;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Telecables.releaseHook();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}