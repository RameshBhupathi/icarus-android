package com.github.mr5.icarus.popover;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mr5.icarus.Icarus;
import com.github.mr5.icarus.R;

public class ColorPickerPopoverImpl implements Popover, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    BottomSheetBehavior mBehavior;
    View colorView;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    AppCompatTextView redToolTip, greenToolTip, blueToolTip;
    AppCompatButton buttonSelector;
    ClipboardManager clipBoard;
    ClipData clip;
    Window window;
    Display display;
    int red, green, blue, seekBarLeft;
    Rect thumbRect;
    protected TextView textView;
    private Context context;
    protected Icarus icarus;
    private Dialog dialog;
    private String name;

    public ColorPickerPopoverImpl(TextView textView, Icarus icarus, String name) {
        this.textView = textView;
        this.icarus = icarus;
        context = textView.getContext();
        this.name = name;
        //mainLopperHandler = new Handler(Looper.getMainLooper());
        initDialog();
    }


    public void initDialog() {
        dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.form_color_picker, null);
        //Set Margins  to with Layout Params Programatically
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.
                LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(60, 200, 60, 20);
        view.setLayoutParams(params);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        SharedPreferences settings = context.getSharedPreferences("COLOR_SETTINGS", 0);
        red = settings.getInt("RED_COLOR", 0);
        green = settings.getInt("GREEN_COLOR", 0);
        blue = settings.getInt("BLUE_COLOR", 0);

        clipBoard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        colorView = view.findViewById(R.id.colorView);
        window = ((Activity) context).getWindow();

        redSeekBar = (SeekBar) view.findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) view.findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) view.findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (AppCompatTextView) view.findViewById(R.id.redToolTip);
        greenToolTip = (AppCompatTextView) view.findViewById(R.id.greenToolTip);
        blueToolTip = (AppCompatTextView) view.findViewById(R.id.blueToolTip);

        buttonSelector = (AppCompatButton) view.findViewById(R.id.buttonSelector);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        //Setting View, Status bar & button color & hex codes

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() != Surface.ROTATION_90 && display.getRotation() != Surface.ROTATION_270)
                window.setStatusBarColor(Color.rgb(red, green, blue));

        }

        buttonSelector.setOnClickListener(this);


        //Calculate device width and height
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);


    }

    @Override
    public void show(String params, String callbackName) {

        dialog.show();

    }

    @Override
    public void hide() {
        dialog.cancel();

    }

    private void closeBottomSheet() {
        //Close Bottom Sheet
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setHideable(true);
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
            thumbRect = seekBar.getThumb().getBounds();

            redToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress < 10)
                redToolTip.setText("  " + red);
            else if (progress < 100)
                redToolTip.setText(" " + red);
            else
                redToolTip.setText(red + "");
        } else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = seekBar.getThumb().getBounds();

            greenToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
            if (progress < 10)
                greenToolTip.setText("  " + green);
            else if (progress < 100)
                greenToolTip.setText(" " + green);
            else
                greenToolTip.setText(green + "");

        } else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = seekBar.getThumb().getBounds();

            blueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress < 10)
                blueToolTip.setText("  " + blue);
            else if (progress < 100)
                blueToolTip.setText(" " + blue);
            else
                blueToolTip.setText(blue + "");

        }

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() == Surface.ROTATION_0)
                window.setStatusBarColor(Color.rgb(red, green, blue));

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void colorSelect() {
        //Copies color to Clipboard
        //Setting the button hex color
        String selectedColorInHexFormat = String.format("#%02x%02x%02x", red, green, blue);
        icarus.jsExec(
                String.format(
                        "javascript: editor.toolbar.execCommand('%s', '%s')", name
                        , selectedColorInHexFormat));
        clip = ClipData.newPlainText("clip", selectedColorInHexFormat);
        clipBoard.setPrimaryClip(clip);
        hide();
    }

    @Override
    public void onClick(View view) {
        if ((view.getId() == R.id.buttonSelector)) {
            colorSelect();
        }
    }
}
