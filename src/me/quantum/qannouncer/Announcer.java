package me.quantum.qannouncer;

public class Announcer implements Runnable {

	private QAnnouncer plugin;
	
	public Announcer(QAnnouncer _plugin) {
		plugin = _plugin;
	}
	
	@Override
	public void run() {
		plugin.broadcast();
	}

}
