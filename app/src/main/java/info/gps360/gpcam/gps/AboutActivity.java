
package info.gps360.gpcam.gps;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import info.gps360.gpcam.R;

import android.util.Log;
import android.widget.TextView;



public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView title = findViewById(R.id.title);
        try {
            title.setText(title.getText() + " " + getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            Log.w(AboutActivity.class.getSimpleName(), e);
        }
    }

}
