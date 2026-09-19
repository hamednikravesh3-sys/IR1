package com.pte.flashcards;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

public class MainActivity extends Activity {
    private WebView webView;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript(
                    "(function(){var b=document.getElementById('speakBtn');" +
                    "if(b&&window.AndroidTTS){b.onclick=function(e){e.stopPropagation();" +
                    "var c=CARDS[filtered[pos]];AndroidTTS.speak(c.en);};}})();",
                    null
                );
            }
        });

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                tts.setSpeechRate(0.85f);
            }
        });
        webView.addJavascriptInterface(new SpeechBridge(), "AndroidTTS");

        try {
            StringBuilder encoded = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                String name = String.format(Locale.US, "data%02d.txt", i);
                try (InputStream in = getAssets().open(name);
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[8192];
                    int n;
                    while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                    encoded.append(out.toString("UTF-8"));
                }
            }
            byte[] gzipBytes = Base64.decode(encoded.toString(), Base64.DEFAULT);
            String html;
            try (GZIPInputStream gz = new GZIPInputStream(new ByteArrayInputStream(gzipBytes));
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192];
                int n;
                while ((n = gz.read(buffer)) != -1) out.write(buffer, 0, n);
                html = out.toString("UTF-8");
            }
            webView.loadDataWithBaseURL("https://app.local/", html, "text/html", "UTF-8", null);
        } catch (Exception e) {
            webView.loadData(
                "<html><body dir='rtl' style='font-family:sans-serif;padding:24px'>" +
                "<h2>خطا در بارگذاری فلش‌کارت‌ها</h2><pre>" + e.toString() + "</pre></body></html>",
                "text/html",
                "UTF-8"
            );
        }
    }

    public class SpeechBridge {
        @JavascriptInterface
        public void speak(String text) {
            runOnUiThread(() -> {
                if (tts != null) {
                    tts.stop();
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "pte-word");
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
