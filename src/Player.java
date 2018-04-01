/**
 * @file Player.java
 * @brief Player for DASH protocol
 *
 * @author Iacopo Mandatelli
 * @author Matteo Biasetton
 * @author Luca Piazzon
 *
 * @version 1.0
 * @since 1.0
 *
 * @copyright Copyright (c) 2017-2018
 * @copyright Apache License, Version 2.0
 */


import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;



public class Player extends javax.swing.JFrame {
    //DASHPlayer NOGUI
    private static DashPlayer dashPlayer;

    // Variables declaration
    private EmbeddedMediaPlayer mediaPlayer;

    private boolean videoAlreadyStarted;
    private boolean trainingAlreadyStarted;

    //GUI variables
    private JComboBox<String> jComboBox1;
    private JComboBox<String> jComboBoxTrainingTypes;
    private JLabel jLabel1;
    private JButton messagesButton;
    private JButton playButton;
    private JButton stopButton;
    private JButton startTrainingButton;
    private JButton statsButton;
    private JTextField urlTextfield;
    private Canvas videoCanvas;
    private JPanel videoContainer;

    private static Messages messagesFrame;

    private RenderPlot bufferRender;
    private RenderPlot rewardRender;
    private RenderPlot qualityRender;



    /**
     * Creates new form NewJFrame
     */
    public Player(String name) {
        super(name);
        initComponents();

        // Create hidden Frame for messages
        messagesFrame = new Messages("Messages");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        messagesFrame.setLocation((int) getLocation().getX() + this.getWidth(), this.getY());

    }


    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        //**************************************************//
        // Graphics stuff
        videoContainer = new javax.swing.JPanel();
        videoCanvas = new java.awt.Canvas();
        jLabel1 = new javax.swing.JLabel();
        urlTextfield = new javax.swing.JTextField();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        startTrainingButton = new javax.swing.JButton();
        statsButton = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBoxTrainingTypes = new javax.swing.JComboBox<>();
        messagesButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 585));
        videoContainer.setPreferredSize(new java.awt.Dimension(551, 353));
        videoCanvas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        videoCanvas.setName("videoCanvas");
        javax.swing.GroupLayout videoContainerLayout = new javax.swing.GroupLayout(videoContainer);
        videoContainer.setLayout(videoContainerLayout);
        videoContainerLayout.setHorizontalGroup(
                videoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(videoCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        videoContainerLayout.setVerticalGroup(
                videoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(videoCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLabel1.setText("Media URL:");
        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        messagesButton.setText("Messages");
        messagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messagesButtonActionPerformed(evt);
            }
        });
        startTrainingButton.setText("Start Training");
        startTrainingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTrainingButtonActionPerformed(evt);
            }
        });
        statsButton.setText("Stats");
        statsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(videoContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(urlTextfield))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(messagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(statsButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(startTrainingButton, GroupLayout.DEFAULT_SIZE, 90, GroupLayout.DEFAULT_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jComboBoxTrainingTypes, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(urlTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(videoContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(playButton)
                                        .addComponent(stopButton)
                                        .addComponent(messagesButton)
                                        .addComponent(statsButton))

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBoxTrainingTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(startTrainingButton))
                                .addContainerGap())
        );

        // Set algorithms names to combobox
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Bitrate-based", "Buffer-based", "FESTIVE", "D-DASH: MLP2", "MPC", "PANDA"}));

        // Set type of training to combobox
        jComboBoxTrainingTypes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"server", "local"}));

        // Set default link to the interface
//        TRAINING
//        urlTextfield.setText("http://www.secpoint.it/matteo/dash/video/");
        //urlTextfield.setText("http://localhost/bbb/bigbuckbunny-simple.mpd");

        urlTextfield.setText("http://127.0.0.1:8000/media/bbb/bigbuckbunny-simple.mpd");


        // End graphics stuff
        //**************************************************//

        MediaPlayerFactory mediaPlayerFactory = DashPlayer.getMediaPlayerFactory();

        //FOR MACOS USERS
        dashPlayer = new DashPlayer();

        //FOR WINDOWS USERS
        /*CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(videoCanvas);
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(videoSurface);
        mediaPlayer.addMediaPlayerEventListener(new PlayerEventListener());
        dashPlayer = new DashPlayer(mediaPlayerFactory,mediaPlayer);*/

        pack();

    }



    /**
     * Resize properly the video canvas.
     * This method is called automatically when the form is resized.
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        if (videoAlreadyStarted) {
            videoCanvas.setSize(getRootPane().getWidth(), getRootPane().getHeight() - 120);
        } else {
            videoCanvas.setSize(videoContainer.getWidth(), videoContainer.getHeight() - 120);
        }
    }

    /**
     * Define the behavior of the playButton.
     * This method is called automatically when the playButton is clicked.
     */
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {


//        mediaList.addMedia( "/home/davidetalon/Scrivania/seg1.mp4");
//        mediaList.addMedia( "/home/davidetalon/Scrivania/seg2.mp4");
//        mediaList.addMedia( "/home/davidetalon/Scrivania/seg3.mp4");
//        mediaList.addMedia( "/home/davidetalon/Scrivania/seg4.mp4");
//
//
//        mediaListPlayer.play();



        String algorithmChoice = (String) jComboBox1.getSelectedItem();
        String url = urlTextfield.getText();
        dashPlayer.play(videoAlreadyStarted,algorithmChoice,url);

        if(!videoAlreadyStarted){
            videoAlreadyStarted = true;

            jComboBox1.setEnabled(false);

            bufferRender = new RenderPlot(dashPlayer.getBufferRender(), RenderPlot.BUFFER_PLOT);
            rewardRender = new RenderPlot(dashPlayer.getRewardPlotter(), RenderPlot.REWARD_PLOT);
            qualityRender = new RenderPlot(dashPlayer.getQualityPlotter(), RenderPlot.QUALITY_PLOT);

            addMessage("PLAYER: new DASH algorithm started");
            addLog("PLAYER: new DASH algorithm started");
            playButton.setText("Pause");
        }
        else{
            /*if (mediaPlayer.isPlaying()) {
                playButton.setText("Play");
            } else {
                playButton.setText("Pause");
            }*/

            playButton.setText("Play");
        }
    }

    /**
     * Defines the behavior of the stopButton.
     * Stops reproducing content.
     * This method is called automatically when the stopButton is clicked.
     */
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dashPlayer.stop();
        mediaPlayer.stop();
        playButton.setText("Play");
        videoAlreadyStarted = false;
        jComboBox1.setEnabled(true);
        messagesFrame.clear();
    }

    /**
     * Defines the behavior of the messageButton.
     * Shows log messages.
     * This method is called automatically when the messageButton is clicked.
     */
    private void messagesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (messagesFrame.isVisible()) {
            messagesFrame.setVisible(false);
        } else {
            messagesFrame.setLocation(getLocation().x + getWidth() - 16, getLocation().y);
            messagesFrame.setSize(new Dimension(messagesFrame.getWidth(), this.getHeight()));
            messagesFrame.setVisible(true);
        }

    }

    private void startTrainingButtonActionPerformed(java.awt.event.ActionEvent evt) {

        dashPlayer.startTraining(trainingAlreadyStarted, (String) jComboBoxTrainingTypes.getSelectedItem(), urlTextfield.getText());

        if (trainingAlreadyStarted) {

            startTrainingButton.setText("Start training");
            playButton.setEnabled(true);
            jComboBox1.setEnabled(true);
            jComboBoxTrainingTypes.setEnabled(true);


        } else {

            playButton.setEnabled(false);
            jComboBox1.setEnabled(false);
            jComboBoxTrainingTypes.setEnabled(false);
            trainingAlreadyStarted = true;
            startTrainingButton.setText("Stop training");
            String trainingType = (String) jComboBoxTrainingTypes.getSelectedItem();

            bufferRender = new RenderPlot(dashPlayer.getBufferRender(), RenderPlot.BUFFER_PLOT);
            rewardRender = new RenderPlot(dashPlayer.getRewardPlotter(), RenderPlot.REWARD_PLOT);
            qualityRender = new RenderPlot(dashPlayer.getQualityPlotter(), RenderPlot.QUALITY_PLOT);

            //local training whith exponential scenes and markov channel
            dashPlayer.startTraining(trainingAlreadyStarted,trainingType,urlTextfield.getText());
            if(trainingAlreadyStarted)
                addMessage("TRAINER: new local training started");
            else
                addMessage("TRAINER: new local training started");

        }

    }

    private void statsButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (bufferRender.isVisible() || rewardRender.isVisible() || qualityRender.isVisible()) {

            bufferRender.setVisible(false);
            rewardRender.setVisible(false);
            qualityRender.setVisible(false);

        } else {


            //TODO posizionare statistiche
            bufferRender.setLocation(getLocation().x + getWidth() - 16, getLocation().y);
            bufferRender.setVisible(true);

            rewardRender.setLocation(getLocation().x + getWidth() - 16, getLocation().y);
            rewardRender.setVisible(true);

            qualityRender.setLocation(getLocation().x + getWidth() - 16, getLocation().y);
            qualityRender.setVisible(true);
        }


    }


    /**
     * Append a new message to the messages window.
     *
     * @param message text to append
     */
    public static void addMessage(String message) {
        Messages messagesFrame = Player.messagesFrame;
        messagesFrame.addMessage(message);
    }

    /**
     * Append a new message to the log file.
     *
     * @param message text to append
     */
    public static void addLog(String message) {
        dashPlayer.addLog(message);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Set look of OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Error on setting look and feel");
        }
        // Find needed VLC library
        new NativeDiscovery().discover();

        // Create and display the form
        java.awt.EventQueue.invokeLater(() -> {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Player g = new Player("DASH_Player");

            g.setLocation(dim.width / 2 - g.getSize().width / 2, dim.height / 2 - g.getSize().height / 2);
            g.setVisible(true);

        });
    }


}


