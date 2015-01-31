package pt.up.fe.specs.library;

import java.util.logging.Logger;

public class Log {

	public static void warn(String message) {
		Logger.getGlobal().warning("Could not get InputStream.");
	}

	public static void warn(String message, Exception e) {
		Logger.getGlobal().warning("Could not get InputStream.");
	}
}
