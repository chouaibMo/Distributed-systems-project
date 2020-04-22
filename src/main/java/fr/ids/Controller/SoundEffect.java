/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

import javafx.scene.media.AudioClip;

/**
 *
 * @author chouaib
 */
public class SoundEffect {
    
    private AudioClip soundEffect;

    public SoundEffect(String filename) {
        this.soundEffect = new AudioClip(getClass().getResource("/fr/ids/Resources/Sounds/"+filename).toExternalForm());
        this.soundEffect.setVolume(0.4);
    }

    public void playClip() {
        soundEffect.play();
    }
}