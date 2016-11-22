package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class TeleOpHardwarev7 {

    private static final double TRIGGER_LOW_POSITION = 0.3;
    private static final double TRIGGER_HIGH_POSITION = 0.0;
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor forkliftLeft = null;
    public DcMotor forkliftRight = null;
    private DcMotor launcher1 = null;
    private DcMotor launcher2 = null;
    public Servo triggerServo = null;
    public CRServo sweeperLeft = null;
    public CRServo sweeperRight = null;

    private ElapsedTime period = new ElapsedTime();
    private boolean doesLauncherExist;

    public String init(HardwareMap hardwareMap) {
        initDriveMotors(hardwareMap);
        String initMessageLauncher = initLauncherMotors(hardwareMap);
        initSweeper(hardwareMap);
        initLauncher(hardwareMap);
        initForkLift(hardwareMap);
        return initMessageLauncher;
    }

    private void initForkLift(HardwareMap hardwareMap) {
        forkliftLeft = hardwareMap.dcMotor.get("forkliftLeft");
        forkliftRight = hardwareMap.dcMotor.get("forkliftRight");
        forkliftLeft.setDirection(DcMotor.Direction.REVERSE);
        forkliftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        forkliftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        forkliftLeft.setPower(0);
        forkliftRight.setPower(0);
    }

    private void initLauncher(HardwareMap hardwareMap) {
        triggerServo = hardwareMap.servo.get("triggerServo");
        setTriggerLow();
        launcherOff();
    }

    private void initSweeper(HardwareMap hardwareMap) {
        sweeperRight = hardwareMap.crservo.get("servo_right");
        sweeperLeft = hardwareMap.crservo.get("servo_left");
        sweeperLeft.setPower(0);
        sweeperRight.setPower(0);
    }

    private String initLauncherMotors(HardwareMap hardwareMap) {
        launcher1 = hardwareMap.dcMotor.get("launcher_1");
        launcher2 = hardwareMap.dcMotor.get("launcher_2");
        if (launcher1 != null && launcher2 != null) {
            doesLauncherExist = true;
            launcher1.setPower(0);
            launcher2.setPower(0);
            launcher2.setDirection(DcMotorSimple.Direction.REVERSE);
            launcher1.setDirection(DcMotorSimple.Direction.FORWARD);
            launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            launcher2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            launcher1.setMaxSpeed(0);
            launcher2.setMaxSpeed(0);
            return "Launcher initialized";
        } else {
            return "launcher motors not found! Kunal, put it back :)";
        }
    }

    private void initDriveMotors(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setPower(0);
        rightMotor.setPower(0);

    }

    public void waitForTick(long periodMs) throws InterruptedException {
        long remaining = periodMs - (long) period.milliseconds();
        if (remaining > 0) {
            Thread.sleep(remaining);
        }
        period.reset();
    }

    private void setLauncherMaxSpeed(int speed) {
        if (!doesLauncherExist) return;
        launcher1.setMaxSpeed(speed);
        launcher2.setMaxSpeed(speed);
        launcher1.setPower(1);
        launcher2.setPower(1);
    }

    public int getLauncherMaxSpeed() {
        return doesLauncherExist ? launcher1.getMaxSpeed() : 0;
    }

    public void changeLauncherMaxSpeedBy(int changeBy) {
        int newSpeed = getLauncherMaxSpeed() + changeBy;
        if (newSpeed < 0) {
            newSpeed = 0;
        }
        setLauncherMaxSpeed(newSpeed);
    }

    public void setSweeperPower(double power) {
        sweeperLeft.setPower(power);
        sweeperRight.setPower(-1 * power);
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