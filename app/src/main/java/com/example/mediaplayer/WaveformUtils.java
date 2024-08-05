package com.example.mediaplayer;

import java.util.ArrayList;
import java.util.List;

public class WaveformUtils {

    // Mock method to generate dummy waveform data
    public static List<Float> generateDummyWaveform() {
        List<Float> waveformPoints = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            // Generate some dummy waveform points between 0 and 1
            waveformPoints.add((float) Math.random());
        }
        return waveformPoints;
    }
}