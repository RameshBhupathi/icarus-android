package com.github.mr5.icarus.button;

import android.widget.TextView;

import com.github.mr5.icarus.Icarus;

public class BackgroundColorButton extends TextViewButton {
    public String getName() {
        return NAME_BACK_COLOR;
    }

    public BackgroundColorButton(TextView textView, Icarus icarus) {
        super(textView, icarus);
    }

  /*  public void command() {
            icarus.jsExec(
                    String.format(
                            "javascript: editor.toolbar.execCommand('%s', '%s')",
                            Button.NAME_BACK_COLOR, "#333333"));
    }*/

    public void command() {
        if (getPopover() != null) {
            getPopover().show(Button.NAME_BACK_COLOR, "");
        }
    }
}

