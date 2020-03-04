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
  
  TeleHook m_leftTeleCable;
  TeleHook m_rightTeleCable;

  boolean extend;

  /**
   * Creates a new HookSystem.
   */
  public CableSystem(boolean _extend) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotMap.m_leftTeleHook);
    addRequirements(RobotMap.m_rightTeleHook);
    
    m_leftTeleCable = RobotMap.m_leftTeleHook;
    m_leftTeleCable = RobotMap.m_rightTeleHook;
    
    extend = _extend;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(extend){
      m_leftTeleCable.extend();
      m_rightTeleCable.extend();
    }
    else{
      m_leftTeleCable.retract();
      m_rightTeleCable.retract();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_leftTeleCable.releaseHook();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}