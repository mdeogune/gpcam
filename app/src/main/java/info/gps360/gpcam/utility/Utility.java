package info.gps360.gpcam.utility;


public class Utility {
    public void reBoot(){
        try {
            Process proc = Runtime.getRuntime()
                    .exec(new String[]{ "su", "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void shutDown(){
        try {
            Process proc = Runtime.getRuntime()
                    .exec(new String[]{ "su", "-c", "reboot -p" });
            proc.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
