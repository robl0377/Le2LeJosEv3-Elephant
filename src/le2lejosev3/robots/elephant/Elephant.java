/**
 * 
 */
package le2lejosev3.robots.elephant;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.BrickButtons;
import le2lejosev3.pblocks.ColorSensor;
import le2lejosev3.pblocks.LargeMotor;
import le2lejosev3.pblocks.MediumMotor;
import le2lejosev3.pblocks.Sound;
import le2lejosev3.pblocks.Timer;
import le2lejosev3.pblocks.TouchSensor;
import le2lejosev3.pblocks.Wait;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Elephant
 * 
 * @author Roland Blochberger
 */
public class Elephant {

	private static Class<?> clazz = Elephant.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortA = MotorPort.A; // Large Motor
	static final Port motorPortB = MotorPort.B; // Large Motor
	static final Port motorPortD = MotorPort.D; // Medium Motor
	static final Port touchPort1 = SensorPort.S1; // Touch Sensor
	static final Port colorPort1 = SensorPort.S4; // Color Sensor

	// the motors:
	private static final LargeMotor motA = new LargeMotor(motorPortA);
	private static final LargeMotor motB = new LargeMotor(motorPortB);
	private static final MediumMotor motD = new MediumMotor(motorPortD);
	// the sensors
	private static final TouchSensor touch = new TouchSensor(touchPort1);
	private static final ColorSensor color = new ColorSensor(colorPort1);

	// the variables
	// cf
	private static int cf = 0;

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		// setup logging to file
		// Setup.log2File(clazz);
		log.info("Starting ...");

		// call Reset Block
		reset();

		// Outer Loop 01
		float rotations = 0F;
		while (Button.ESCAPE.isUp()) {
			// set variable cf to 0
			cf = 0;
			// Reset Timer 1
			Timer.reset(1);
			// Inner Loop 01
			while (Button.ESCAPE.isUp()) {
				// react on Brick buttons
				switch (BrickButtons.measure()) {

				case BrickButtons.BB_UP:
					log.info("Up Button");
					// Play tone with 600 Hz for 0.1 seconds with volume 100 and wait until done
					// (playtype 0)
					Sound.playTone(600, 0.1F, 100, Sound.WAIT);
					// Reset Timer 1
					Timer.reset(1);
					// Increment cf
					cf++;
					// wait until brick button UP (4) is released
					while (Button.ESCAPE.isUp()) {
						if (BrickButtons.measure() == BrickButtons.BB_NONE) {
							break;
						}
					}
					break;

				case BrickButtons.BB_DOWN:
					log.info("Down Button");
					// Play tone with 1200 Hz for 0.1 seconds with volume 100 and wait until done
					// (playtype 0)
					Sound.playTone(1200, 0.1F, 100, Sound.WAIT);
					// Reset Timer 1
					Timer.reset(1);
					// Decrement cf
					cf--;
					// wait until brick button DOWN (5) is released
					while (Button.ESCAPE.isUp()) {
						if (BrickButtons.measure() == BrickButtons.BB_NONE) {
							break;
						}
					}
					break;

				case BrickButtons.BB_LEFT:
					log.info("Left Button");
					// call Lift and roar Block
					liftAndRoar();
					break;

				case BrickButtons.BB_RIGHT:
					log.info("Right Button");
					// call Grab Block
					grab();
					break;

				case BrickButtons.BB_CENTER:
					log.info("Center Button");
					// Play sound file "Elephant call" with volume 100 once in the background
					// (playtype 1)
					Sound.playFile("Elephant call", 100, Sound.ONCE);
					break;

				case BrickButtons.BB_NONE:
				default:
					log.info("No Button");
					break;
				}
				// exit loop if timer value > 1 second
				if (Timer.measure(1) > 1F) {
					break;
				}
			} // end Inner Loop 01

			// check whether cf is not 0
			if (cf != 0) {
				// calculate the rotations
				rotations = cf * -2.5F;
				// Large motor A on with power 100 for rotations and brake at end
				// NOTE: it seems that the LEGO Programming block also accepts a negative number
				// of rotations for backward movement!
				motA.motorOnForRotations(100, rotations, true);
			}
		} // end Outer Loop 01

		log.info("The End");
	}

	/**
	 * Grab Block
	 */
	private static void grab() {
		log.info("");
		// call Reset Block
		reset();
		// Medium motor D on with Power 100 for 350 degrees and brake at end
		motD.motorOnForDegrees(100, 350, true);
		// Large motor B on with Power -100 for 300 degrees and brake at end
		motB.motorOnForDegrees(-100, 300, true);
		// Medium motor D on with Power -50 for 350 degrees and brake at end
		motD.motorOnForDegrees(-50, 350, true);
		// Medium motor D on with Power -10 for 1 second and brake at end
		motD.motorOnForSeconds(-10, 1F, true);
		// Large motor B on with Power 70 for 500 degrees and brake at end
		motB.motorOnForDegrees(70, 500, true);

		// run in parallel:
		// Large motor grab thread
		Thread lmth = new GrabLargeMotorThread();
		// Medium motor grab thread
		Thread mmth = new GrabMediumMotorThread();
		// start both threads
		lmth.start();
		mmth.start();
		// wait until both threads are done
		try {
			lmth.join();
			mmth.join();
		} catch (InterruptedException e) {
			// ignore
		}
		log.info("end");
	}

	/**
	 * Large motor parallel execution thread for Grab Block
	 */
	static class GrabLargeMotorThread extends Thread {

		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// log.info("");
			// Large motor B on with power 30 for 300 degrees and brake at end
			motB.motorOnForDegrees(30, 300, true);
			// log.info("end");
		}
	}

	/**
	 * Medium motor parallel execution thread for Grab Block
	 */
	static class GrabMediumMotorThread extends Thread {

		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// log.info("");
			// Medium motor D on with power 30 for 400 degrees and brake at end
			motD.motorOnForDegrees(30, 400, true);
			// log.info("end");
		}
	}

	/**
	 * Lift and roar Block
	 */
	private static void liftAndRoar() {
		log.info("");
		// Large Motor B on with power -30
		motB.motorOn(-30);
		// wait until touch sensor finds state pressed (1)
		touch.waitCompareState(TouchSensor.PRESSED);
		// Large motor B on with power 10 for 30 degrees and brake at end
		motB.motorOnForDegrees(10, 30, true);
		// call Reset Block
		reset();
		log.info("end");
	}

	/**
	 * Reset Block
	 */
	private static void reset() {
		log.info("");
		// Medium motor D on with power 50
		motD.motorOn(50);
		// wait until color sensor finds color red (5)
		while (Button.ESCAPE.isUp()) {
			if (color.measureColor() == ColorSensor.COLOR_RED) {
				break;
			}
			Wait.time(0.002F);
		}
		// Medium motor D off with brake
		motD.motorOff(true);

		// Large motor B on with power -60
		motB.motorOn(-60);
		// wait until touch sensor finds state pressed (1)
		touch.waitCompareState(TouchSensor.PRESSED);
		// Large motor B off with brake
		motB.motorOff(true);

		// Play sound file "Elephant call" with volume 100 once in the background
		// (playtype 1)
		Sound.playFile("Elephant call", 100, Sound.ONCE);
		// wait 1 second
		Wait.time(1F);

		// run in parallel:
		// Large motor reset thread
		Thread lmth = new ResetLargeMotorThread();
		// Medium motor reset thread
		Thread mmth = new ResetMediumMotorThread();
		// start both threads
		lmth.start();
		mmth.start();
		// wait until both threads are done
		try {
			lmth.join();
			mmth.join();
		} catch (InterruptedException e) {
			// ignore
		}
		log.info("end");
	}

	/**
	 * Large motor parallel execution thread for Reset Block
	 */
	static class ResetLargeMotorThread extends Thread {

		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// log.info("");
			// Large motor B on with power 90 for 770 degrees and brake at end
			motB.motorOnForDegrees(90, 770, true);
			// Wait 0.1 seconds
			Wait.time(0.1F);
			// Rotation reset at Large motor B
			motB.rotationReset();
			// log.info("end");
		}
	}

	/**
	 * Medium motor parallel execution thread for Reset Block
	 */
	static class ResetMediumMotorThread extends Thread {

		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// log.info("");
			// Medium motor D on with power -40 for 770 degrees and brake at end
			motD.motorOnForDegrees(-40, 770, true);
			// Wait 0.1 seconds
			Wait.time(0.1F);
			// Rotation reset at Medium motor D
			motD.rotationReset();
			// log.info("end");
		}
	}
}
