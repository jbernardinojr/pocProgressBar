package br.com.bernardino.pocmyprogressbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AsyncListener {

    private ProgressBar pbar;
    private TextView textViewResponse;

    private String URL = "https://6352ac93a9f3f34c37463419.mockapi.io/api/v1/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResponse = findViewById(R.id.tv_response);
        pbar = findViewById(R.id.pbProcessing);
        Button button = findViewById(R.id.btn_send);

        button.setOnClickListener(v -> {
            HttpGetRequestTst getRequest = new HttpGetRequestTst(this);
            //Perform the doInBackground method, passing in our url
            getRequest.execute(URL);
        });
    }

    @Override
    public void onAsyncFinished(String resp) {
        textViewResponse.setVisibility(View.VISIBLE);
        textViewResponse.setText(resp);
    }

    @Override
    public void onUpdateProgress(Integer progressValue) {
        pbar.setProgress(progressValue);
    }
}
