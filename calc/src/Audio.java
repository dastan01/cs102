import javafx.scene.media.AudioClip;

public class Audio {
	
	AudioClip Audio ;
	

	public Audio(String loc) {
		Audio = new AudioClip(getClass().getResource(loc).toString());

		Audio.setVolume(0.5f);
	}
	
	public void play() {
		Audio.play();
	}
	
	public void stop() {
		Audio.stop();
	}
	
	public void cycleCountINF() {
		Audio.setCycleCount(AudioClip.INDEFINITE);
	}
	
	public void cycleCount(int x) {
		Audio.setCycleCount(x);
	}
	public boolean isPlaying() {
		return Audio.isPlaying();
	}
}
