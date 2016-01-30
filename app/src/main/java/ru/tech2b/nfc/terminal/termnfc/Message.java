package ru.tech2b.nfc.terminal.termnfc;

import android.nfc.tech.IsoDep;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by User on 25.01.2016.
 */
public class Message {
    public selectAid(String aid, HashMap<String, String> tags, IsoDep tagcomm) {
        Integer lc = aid.length()/2;
        String strLc;
        if (lc < 10) {strLc = "0"+lc.toString();} else {strLc = lc.toString();}
        String message = "00A40400"+strLc+aid+"00";
        try {
            byte[] res = send(message,tagcomm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return res;
    }

    protected static byte[] send(String hexstr, IsoDep tagcomm) throws IOException {
        String[] hexbytes = new String[hexstr.length()/2]; //= hexstr.split("\\s");
        for (int i = 0; i < hexstr.length()/2;i++){
            hexbytes[i] = hexstr.substring(2*i,2+2*i);
        }
        byte[] bytes = new byte[hexbytes.length];
        for (int i = 0; i < hexbytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexbytes[i], 16);
        }

        byte[] recv = tagcomm.transceive(bytes);

        return recv;
    }

}
