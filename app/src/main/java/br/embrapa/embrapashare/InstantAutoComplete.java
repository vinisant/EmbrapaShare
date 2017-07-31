package br.embrapa.embrapashare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;


public class InstantAutoComplete extends AppCompatAutoCompleteTextView {

    private Context context;

    public InstantAutoComplete(Context context) {
        super(context);
        this.context = context;
        setTouchListener();
    }

    public InstantAutoComplete(Context context, AttributeSet arg1) {
        super(context, arg1);
        this.context = context;
        setTouchListener();
    }

    public InstantAutoComplete(Context context, AttributeSet arg1, int arg2) {
        super(context, arg1, arg2);
        this.context = context;
        setTouchListener();
    }

    public void setTouchListener(){
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView)v).showDropDown();
                performFiltering("", 0);
                return false;
            }
        });
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if ((focused && getAdapter() != null) && getText().length() == 0) {
            performFiltering(getText(), 0);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 1  && isPopupShowing()) {
            super.onKeyPreIme(keyCode, event);

            View view = ((Activity)context).getCurrentFocus(); //TODO cast
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}

/* inexplicable warnings
E/SpannableStringBuilder: SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length
finishComposingText on inactive InputConnection AutoCompleteTextView
InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
 */
