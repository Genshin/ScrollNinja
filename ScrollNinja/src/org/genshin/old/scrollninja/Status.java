package org.genshin.old.scrollninja;

public class Status {
	public static enum RunModes { ERROR, INIT, MAIN_MENU, SETTINGS, GAME_INIT, GAME_RUN, GAME_FINISH, SHUTDOWN };

	private static int gameMode;
	private static Boolean running;
	
	public static int getGameMode() {
		return Status.gameMode;
	}
	
	public static void setGameMode(int gameMode) {
		Status.gameMode = gameMode;
	}
	
	public static Boolean running() {
		return Status.running();
	}
	
	public static void running(Boolean running) {
		Status.running = running;
	}
}
