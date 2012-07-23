package org.game.energizar;


import java.io.InputStream;

import javax.microedition.media.Player;

import net.rim.device.api.system.Alert;
import net.rim.device.api.ui.component.Dialog;

// Sound engine
class _SND {

	Player _musicPlayer; // Java media player

	_SND() {
	}

	// Play a midi file for background music
	void playMusic(String passMusic) {
		try {
			// Set InputStream to a midi file included as resource, as specified
			// by
			// passMusic
			InputStream in = getClass().getResourceAsStream("/" + passMusic);

			// Create a media player with mime type of audio/midi using our
			// inputstream
			_musicPlayer = javax.microedition.media.Manager.createPlayer(in,
					"audio/midi");

			// Ready the data and start playing it. To loop indefinitely, we set
			// loopcount
			// to -1.
			_musicPlayer.realize();
			_musicPlayer.prefetch();
			_musicPlayer.setLoopCount(-1);
			_musicPlayer.start();

		} catch (Exception e) {
			Dialog.alert("Error playing music");
		}
	}

	// Stop playing music
	void stopMusic() {
		try {
			// Tell player to stop playing
			_musicPlayer.stop();

		} catch (Exception e) {
			Dialog.alert("Error stopping music");
		}

		// Then release the data and close out the player
		_musicPlayer.deallocate();
		_musicPlayer.close();
	}

	// The Playsound method plays a simple combinations of tones to simulate a
	// firing
	// noise. This was necessary, as due to a bug or limitation of the
	// BlackBerry 8830
	// (the phone I do my testing on), playing a WAV file stopped the midi
	// player and
	// any other sound effects. Player doesn't appear to mix properly (if at
	// all). However,
	// a midi file can be played while using the Alert objects startAudio method
	// which
	// can play a sequence of tones, so this is what we've done for now.
	void playSound() {
		// A sequence of frequencies and durations (eg 1400hz for 15ms, 1350hz
		// for 15ms, etc)
		short[] fire = { 1400, 15, 1350, 15, 1320, 20, 1300, 20, 1250, 25,
				1200, 35 };

		try {
			Alert.startAudio(fire, 100);

		} catch (Exception e) {
			Dialog.alert("Error playing sound effect.");
		}

	}

	// Activates the phone's vibration functionality for a specific number of ms
	void vibrate(int passMilli) {
		Alert.startVibrate(passMilli);
	}
}
