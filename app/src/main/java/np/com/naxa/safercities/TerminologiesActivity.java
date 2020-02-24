package np.com.naxa.safercities;

import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.safercities.utils.sectionmultiitemUtils.DataServer;

public class TerminologiesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_general)
    Toolbar toolbarGeneral;
    @BindView(R.id.tvTerminologies)
    TextView tvTerminologies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminologies);
        ButterKnife.bind(this);


        setupToolBar();

        setTvTerminologies();

    }

    private void setupToolBar() {
        setSupportActionBar(toolbarGeneral);

            getSupportActionBar().setTitle("Terminologies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void setTvTerminologies (){
        DataServer dataServer = new DataServer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTerminologies.setText(Html.fromHtml(dataServer.getTerminologies(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvTerminologies.setText(Html.fromHtml(dataServer.getTerminologies()));
        }
    }
}
