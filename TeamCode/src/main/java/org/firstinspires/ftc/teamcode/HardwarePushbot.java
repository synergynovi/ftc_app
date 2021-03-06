package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwarePushbot
{
    /* Public OpMode members. */
    public DcMotor  leftMotor   = null;
    public DcMotor  rightMotor  = null;
    private DcMotor launcher1 = null;
    private DcMotor launcher2 = null;
    public Servo triggerServo = null;

   //public static final double MID_SERVO       =  0.5 ;
    //public static final double ARM_UP_POWER    =  0.45 ;
    //public static final double ARM_DOWN_POWER  = -0.45 ;
   private static final double TRIGGER_LOW_POSITION = 0.3;
    private static final double TRIGGER_HIGH_POSITION = 0.0;


    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();
    private boolean doesLauncherExist;
    /* Constructor */
    public HardwarePushbot(){

    }

    private void initLauncher(HardwareMap hardwareMap) {
        triggerServo = hardwareMap.servo.get("triggerServo");
        setTriggerLow();
        launcherOff();
    }
    private void setLauncherMaxSpeed(int speed) {
        if (!doesLauncherExist) return;
        launcher1.setPower(speed);
        launcher2.setPower(speed);
        launcher1.setPower(1);
        launcher2.setPower(1);
    }
    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        initLauncher(hwMap);

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get("left_drive");
        rightMotor  = hwMap.dcMotor.get("right_drive");
        //armMotor    = hwMap.dcMotor.get("left_arm");
        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        //armMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        //leftClaw = hwMap.servo.get("left_hand");
        //rightClaw = hwMap.servo.get("right_hand");
        //leftClaw.setPosition(MID_SERVO);
        //rightClaw.setPosition(MID_SERVO);
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
    public void launcherOn() {
        setLauncherMaxSpeed(300);
    }

    public void launcherOff() {
        setLauncherMaxSpeed(0);
    }

    public void setTriggerLow() {
        triggerServo.setPosition(TRIGGER_LOW_POSITION);
    }

    public void setTriggerHigh() {
        triggerServo.setPosition(TRIGGER_HIGH_POSITION);
    }

    public double getTriggerPosition() {
        return triggerServo.getPosition();
    }

    public void launchTheBall() throws InterruptedException {
        launcherOn();
        waitForTick(1500);
        setTriggerHigh();
    }

    public void stopLaunchingBall() {
        launcherOff();
        setTriggerLow();
    }
}
