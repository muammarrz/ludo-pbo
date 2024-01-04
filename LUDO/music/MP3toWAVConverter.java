/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package music;

/**
 *
 * @author Azeren
 */
import javax.sound.sampled.*;


import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;

import java.io.File;
import java.io.IOException;

public class MP3toWAVConverter {

    public static void main(String[] args) {
        String mp3FilePath = "C:\\Users\\Azeren\\OneDrive\\Documents\\NetBeansProjects\\LUDO\\src\\music\\1.mp3";
        String wavFilePath = "C:\\Users\\Azeren\\OneDrive\\Documents\\NetBeansProjects\\LUDO\\src\\music\\2.wav";

        try {
            convertMP3toWAV(mp3FilePath, wavFilePath);
            System.out.println("Conversion successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertMP3toWAV(String mp3FilePath, String wavFilePath) throws IOException, JavaLayerException {
        Converter converter = new Converter();
        converter.convert(mp3FilePath, wavFilePath, null);
    }
}
