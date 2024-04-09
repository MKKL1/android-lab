package com.zielonka.lab.lab1;

import android.view.View;
import android.widget.EditText;

@FunctionalInterface
public interface FormElementValidator<T extends View> {
    void validate(T view);
}
