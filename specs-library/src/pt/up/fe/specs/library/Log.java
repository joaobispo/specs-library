package pt.up.fe.specs.library;

import java.util.logging.Logger;

public class Log {

	public static void info(String message) {
		Logger.getGlobal().info(message);
	}

	public static void lib(String message) {
		Logger.getGlobal().fine(message);
	}

	public static void warn(String message) {
		Logger.getGlobal().warning(message);
	}

	public static void warn(String message, Exception e) {
		Logger.getGlobal().warning(message);
		e.printStackTrace();
	}
}
