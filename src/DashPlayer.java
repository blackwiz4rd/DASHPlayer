import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.*;

/**
 * DashPlayer is a NO gui player. However it depends on MediaPlayerFactory as a paramenter to work.
 * */
public class DashPlayer {
    // Variables declaration

    private MediaListPlayer mediaListPlayer;

    private DashAlgorithm dash;
    private String tempFolderPath;
    private MediaList mediaList;
    private FileHandler fh;
    private static Logger logger;
    private static Logger MPDLog;

    private Trainer trainer;

    private Plotter bufferPlotter;
    private Plotter rewardPlotter;
    private Plotter qualityPlotter;

    //If not specified default settings are applied
    public DashPlayer(){
        this(DashPlayer.getMediaPlayerFactory());
    }

    public DashPlayer(MediaPlayerFactory mediaPlayerFactory) {

        // Creates temp and subfolders if not present
        tempFolderPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
        File downloadFolder = new File(tempFolderPath);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }
        downloadFolder = new File(tempFolderPath + "init" + File.separator);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }
        downloadFolder = new File(tempFolderPath + "seg" + File.separator);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }

        // Creates the logger
        logger = Logger.getLogger("MyLog");
        MPDLog = Logger.getLogger("MDPLog");

        String logFolderPath = System.getProperty("user.dir") + File.separator + "logs" + File.separator;
        File logFolder = new File(logFolderPath);
        if (!logFolder.exists()) {
            logFolder.mkdir();
        }

        boolean exists = false;
        String logFilePath = "";
        for (int i = 0; !exists; i++) {
            logFilePath = "logs/" + "MPD" + i + "log";
            File log = new File(logFilePath);
            if (!log.exists()) {
                exists = true;
            }
        }

        System.out.println(logFilePath);
        FileHandler fh = null;
        try {
            fh = new FileHandler(logFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleFormatter formatter = new SimpleFormatter();


        fh.setFormatter(formatter);
        MPDLog.addHandler(fh);
        MPDLog.setUseParentHandlers(false);

        mediaList = mediaPlayerFactory.newMediaList();
        mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();

        mediaListPlayer.setMediaList(mediaList);
    }

    /*FOR WINDOWS ONLY (TO DISPLAY VIDEO)*/
    public DashPlayer(MediaPlayerFactory mediaPlayerFactory,EmbeddedMediaPlayer mediaPlayer){
        this(mediaPlayerFactory);
        mediaListPlayer.setMediaPlayer(mediaPlayer);
    }

    /**
     * Starts the selected algorithm as a thread.
     * @param videoAlreadyStarted set as false if video has not been started.
     * @param algorithmChoice choose the algorithm to run
     * @param url set the url where the MPD file is located
     */
    public void play(boolean videoAlreadyStarted, String algorithmChoice, String url){
        if (!videoAlreadyStarted) {
            // This block configures the logger with handler and formatter
            try {
                fh = new FileHandler(tempFolderPath + File.separator + "DashAlgorithmLog.log");
                logger.addHandler(fh);
                fh.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        Calendar cal = new GregorianCalendar();
                        cal.setTimeInMillis(record.getMillis());
                        return " " + logTime.format(cal.getTime())
                                + " || "
                                + record.getMessage() + "\n";
                    }
                });
                logger.setUseParentHandlers(false);
            } catch (SecurityException | IOException e) {
                System.err.println(e.getMessage());
            }
            switch (algorithmChoice) {
                case "Bitrate-based":
                    dash = new BitRateBasedDashAlgorithm(mediaListPlayer, tempFolderPath, url);
                    break;
                case "Buffer-based":
                    dash = new BufferBasedDashAlgorithm(mediaListPlayer, tempFolderPath, url);
                    break;
                case "FESTIVE":
                    dash = new FestiveDashAlgorithm(mediaListPlayer, tempFolderPath, url);
                    break;
                case "D-DASH: MLP2":
                    dash = new MLP2(mediaListPlayer, tempFolderPath, url);
                    break;
                case "MPC":
                    dash = new MPCDashAlgorithm(mediaListPlayer, tempFolderPath, url);
                    break;
                case "PANDA":
                    dash = new PANDADashAlgorithm(mediaListPlayer, tempFolderPath, url);
            }

            bufferPlotter = new Plotter();
            rewardPlotter = new Plotter();
            qualityPlotter = new Plotter();

            dash.setPlotters(bufferPlotter, qualityPlotter, rewardPlotter);

            dash.start();

        } else {
            mediaListPlayer.pause();
        }
    }

    public void stop(){
        mediaListPlayer.stop();
        dash.closeMDPSession();
        mediaList.clear();
        PlayerEventListener.segIndex = 1;
        fh.close();
    }

    public Plotter getQualityPlotter() {
        return qualityPlotter;
    }

    public Plotter getRewardPlotter() {
        return rewardPlotter;
    }

    public Plotter getBufferRender() {
        return bufferPlotter;
    }

    public void addLog(String message) {
        logger.info(message);
    }

    public void startTraining(boolean trainingAlreadyStarted, String trainingType, String url){
        if(trainingAlreadyStarted){
            trainer.forceInterrupt();
        }
        else{
            bufferPlotter = new Plotter();
            rewardPlotter = new Plotter();
            qualityPlotter = new Plotter();


            if (trainingType.equals("local")) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            trainer = new Trainer(1000, 540, 8, 400, false, false);
                            trainer.setPlotters(bufferPlotter, rewardPlotter, qualityPlotter);
                            trainer.start();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });

                addLog("TRAINER: new local training started");

                //training using fake MPDs downloaded from server
            } else {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            trainer = new Trainer(1000, 540, 8, 400, false, true);
                            trainer.setPlotters(bufferPlotter, rewardPlotter, qualityPlotter);
                            trainer.setSourceUrl(url);
                            trainer.setTempFolderPath(tempFolderPath);
                            trainer.start();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
                addLog("TRAINER: new local training started");

            }
        }
    }

    public static MediaPlayerFactory getMediaPlayerFactory(){
        List<String> vlcArgs = new ArrayList<String>();
//        vlcArgs.add("-vvv");
//        vlcArgs.add("--no-plugins-cache");
//        vlcArgs.add("--avcodec-fast");
        vlcArgs.add("--avcodec-skiploopfilter=4");
        vlcArgs.add("--file-caching=70");
        vlcArgs.add("--avcodec-hw=any");
//        vlcArgs.add("--avcodec-codec=H264");
        vlcArgs.add("--avcodec-threads=1");
//        vlcArgs.add("--no-avcodec-hurry-up");
//        vlcArgs.add(" --no-skip-frames");

        // MediaPlayer
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(vlcArgs);
        return mediaPlayerFactory;
    }
}
