package com.kozmotronik.example.duzeygostergesi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;


public class DuzeyGostergesiActivity extends AppCompatActivity {
    private static final String ETIKET = DuzeyGostergesiActivity.class.getSimpleName();

    View gosterge;
    TextView yuzde;
    SeekBar ayar;
    EditText editTextMiktar;
    Button doldur, bosalt;

    // Tuşlarla yapılacak artırma ve azaltma için miktar. EditText ile alınacak. Varsayılan 5.
    int miktar = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duzey_gostergesi);

        // UI denetim öğelerini ilkleyelim
        gosterge = findViewById(R.id.view_gosterge);
        yuzde = findViewById(R.id.textView_yuzde);
        ayar = findViewById(R.id.seekBar_ayar);
        editTextMiktar = findViewById(R.id.editText_miktar);
        doldur = findViewById(R.id.button_doldur);
        bosalt = findViewById(R.id.button_bosalt);

        /*
        SeekBar'ın ilerleme (progress) değişimini dinleyip pil göstergemiz üzerinde gereken
        güncellemeyi yapacağız.
         */
        ayar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /**
                 * Bir {@link Drawable} nesnesinin düzeyi (level)
                 * 0 ile 10000 arasında bir değere kurulabilir. Fakat {@link SeekBar} aracının
                 * maksimum düzeyini okunabilirlik açısından 100 yaptık. Bu yüzden gelen değeri
                 * 10000 değerine ölçeklemek için 10000 / 100 = 100 ile çarpacağız.
                 */
                int duzey = progress * 100;
                String sYuzde = "%" + progress;
                gosterge.getBackground().setLevel(duzey);
                yuzde.setText(sYuzde);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*
        Burada artırma ve azaltma miktarını EditText yoluyla alacağız. Bunun için yazı değişimini
        bir TextWatcher sınıfı ile dinlememiz gerekir.
         */
        editTextMiktar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                Integer sınıfının statik yordamı parseInt kullanarak String sayı girdisini int değere dönüştürüyoruz.
                Bu yordam geçersiz bir sayı stringi durumunda NumberFormatException hatası atabilir.
                Bu yüzden dönüştürme işlemini try-catch bloğu içinde yapacağız.
                 */
                try {
                    miktar = Integer.parseInt(s.toString(), 10); // Decimal radixte string girdiyi sayıya dönüştür
                } catch (NumberFormatException numberFormatException) {
                    // Girilen string verisinde sayı olarak değerlendirilecek bir girdi yok, uyarı ver
                    Snackbar.make(editTextMiktar, s.toString()+" geçerli bir sayı değil!", 1500).show();
                    miktar = 5; // hata durumunda miktarı varsayılan değere kur
                    numberFormatException.printStackTrace(); // Hatayı loga yazdır
                }
                Log.d(ETIKET,"miktar: "+miktar);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Tuşların görevlerini kuralım
        bosalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Önce zaten boş olmadığından emin olmalıyız
                int duzey = ayar.getProgress() - miktar;
                if(duzey < 0) duzey = 0; // Sıfırın altına düştüyse sıfırda tut.
                ayar.setProgress(duzey); // Seekbar progress değerini kurunca pil düzeyi seekbar onProgressChanged içinde güncellenir
            }
        });

        doldur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Değerin 100 ü aşmadığına emin olmalıyız
                int duzey = ayar.getProgress() + miktar;
                if(duzey > 100) duzey = 100;
                ayar.setProgress(duzey);
            }
        });

    }
}