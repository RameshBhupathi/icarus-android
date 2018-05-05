package com.github.mr5.icarus.button;

import android.widget.TextView;

import com.github.mr5.icarus.Icarus;

public class ColorButton extends TextViewButton {
    public String getName() {
        return NAME_COLOR;
    }

    public ColorButton(TextView textView, Icarus icarus) {
        super(textView, icarus);
    }

  /*  public void command() {
        icarus.jsExec(
                String.format(
                        "javascript: editor.toolbar.execCommand('%s', '%s')",
                        Button.NAME_COLOR, "#428bca"));
    }*/

    public void command() {
        if (getPopover() != null) {
            getPopover().show(Button.NAME_COLOR, "");
        }
    }

}
