import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class PalyerNOGUI {
    private static DashPlayer dashPlayer;
    //"Bitrate-based", "Buffer-based", "FESTIVE", "D-DASH: MLP2", "MPC", "PANDA"
    private static String choice = "Bitrate-based";
    private static String url = "http://127.0.0.1:8000/media/bbb/bigbuckbunny-simple.mpd";

    public static void main(String[] args){
        // Find needed VLC library
        new NativeDiscovery().discover();
        dashPlayer = new DashPlayer();

        dashPlayer.play(false,choice,url);
    }

}
