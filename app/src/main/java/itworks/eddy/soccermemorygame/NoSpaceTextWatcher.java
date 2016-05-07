package itworks.eddy.soccermemorygame;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by medve on 02/05/2016.
 */

/**
 * NoSpaceTextWatcher - this class implements TextWatcher interface in order to handle spaces in different EditText views
 */
public class NoSpaceTextWatcher implements TextWatcher {

    private EditText textBox; //current EditText

    public NoSpaceTextWatcher(View textBox) {
        this.textBox = (EditText) textBox;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //for each text modification: checks if the text has spaces and removes such if found
        String result = s.toString().replaceAll(" ", "");
        if (!s.toString().equals(result)) {
            Log.d("Space-->", "replaced");
            textBox.setText(result);
            textBox.setSelection(result.length());
        }
    }
}
