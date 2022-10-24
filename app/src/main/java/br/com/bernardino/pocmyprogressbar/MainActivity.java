package br.com.bernardino.pocmyprogressbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements AsyncListener {

    private ProgressBar progressBar;
    private TextView textViewResponse;
    private Request request;
    private Button button;

    private String URL = "https://6352ac93a9f3f34c37463419.mockapi.io/api/v1/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResponse = findViewById(R.id.tv_response);
        progressBar = findViewById(R.id.pbProcessing);
        button = findViewById(R.id.btn_send);
        setupRequest();

        button.setOnClickListener(v -> {
            v.setEnabled(false);
            callRequest();
        });
    }

    private void setupRequest() {
        JSONObject parameters = new JSONObject();

        request = new Request.Builder()
                .url(URL)
                .post(new ProgressRequestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toString()),
                        (bytesWritten, totalBytes) -> {
                            final float uploadProgressPercent = ((float) bytesWritten) / ((float) totalBytes);
                            progressBar.setProgress((int) uploadProgressPercent * 100);
                        })
                ).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkingHelpers.cancelPendingRequests();
    }

    public void callRequest() {
        NetworkingHelpers.getApiClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                String responseString = Objects.requireNonNull(response.body()).string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    if (response.code() >= 200 || response.code() <= 299) {
                        runOnUiThread(() -> {
                            textViewResponse.setVisibility(View.VISIBLE);
                            textViewResponse.setText(responseJSON.toString());
                            button.setEnabled(true);
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to parse JSON result.");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("FaceTecSDKSampleApp", "Exception raised while attempting HTTPS call.");
            }
        });
    }


    @Override
    public void onAsyncFinished(String resp) {
        textViewResponse.setVisibility(View.VISIBLE);
        textViewResponse.setText(resp);
    }

    @Override
    public void onUpdateProgress(Integer progressValue) {
        progressBar.setProgress(progressValue);
    }
}
