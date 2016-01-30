package ru.tech2b.nfc.terminal.termnfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Tag tag;
    private IsoDep tagcomm;
    public byte[] callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolveIntent(getIntent());
    }

    void resolveIntent(Intent intent) {
        HashMap <String,String> tags = new HashMap<>();
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            String idTag = byteArrayToHexString(intent.getExtras().getByteArray("android.nfc.extra.ID"));
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(this, idTag,Toast.LENGTH_LONG).show();

            tagcomm = IsoDep.get(tag);

            if (tagcomm == null) {
                Toast.makeText(this, "Tagcomm",Toast.LENGTH_LONG).show();
                return;
            }
            try {
                tagcomm.connect();
            } catch (IOException e) {
                Toast.makeText(this, "Tagcomm"+ (e.getMessage() != null ? e.getMessage() : "-"),Toast.LENGTH_LONG).show();

                return;
            }

            try {
                //select PSE
                callBack = Message.send("00A404000E325041592E5359532E444446303100",tagcomm);
                String recieve = byteArrayToHexString(callBack);
                tags.putAll(BerTLV.getBertlv(recieve));
                //select AID
                //callBack = send(Message.selectAID(tags.get("4F")));
                recieve = byteArrayToHexString(callBack);
                tags.putAll(BerTLV.getBertlv(recieve));
                //get proc opt with empty PDOL (processing data object list)
                //callBack = send("80 A8 00 00 02 83 00 00");

            } catch (IOException e) {
                e.printStackTrace();
            }


            Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        }
    }

    public static String byteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }


}
