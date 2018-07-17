import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class PalyerNOGUI {
    private static DashPlayer dashPlayer;
    //{"Bitrate-based", "Buffer-based", "FESTIVE", "D-DASH: MLP2", "MPC", "PANDA", "Scheleton"}
    private static String choice = "Bitrate-based";
    //private static String url = "http://127.0.0.1:8000/media/bbb/bigbuckbunny-simple.mpd";
    private static String url = "http://192.168.0.176:8000/media/bbb/bigbuckbunny-simple.mpd";

    public static void main(String[] args){
        // Find needed VLC library
        new NativeDiscovery().discover();
        dashPlayer = new DashPlayer();

        // If first parameter is specified use the selected algorithm else Bitrate-based runs.
        try{
            choice = agrs[0];
        }
        expect(Exception e){
        }

        dashPlayer.play(false,choice,url);
    }

}
