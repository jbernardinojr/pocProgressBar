package br.com.bernardino.pocmyprogressbar;

public interface AsyncListener {
    void onAsyncFinished(String resp);
    void onUpdateProgress(Integer progressValue);
}
