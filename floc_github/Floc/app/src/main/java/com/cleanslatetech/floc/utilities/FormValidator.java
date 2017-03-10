package com.cleanslatetech.floc.utilities;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.cleanslatetech.floc.interfaces.isEmpty;
import com.cleanslatetech.floc.interfaces.isValidEmail;
import com.cleanslatetech.floc.interfaces.isValidPhone;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by pimpu on 3/8/2017.
 */

public class FormValidator {

    public boolean validateField(Object objectToValidate, Context context) throws IllegalAccessException {
        /**
         *  Get all the fields of the class
         */
        Field[] declaredFields = objectToValidate.getClass().getDeclaredFields();

        /**
         *  Iterate over each field to check if that field
         *  has annotation declared for it or not
         */
        for(Field field : declaredFields) {

            Annotation emptyAnnotation = field.getAnnotation(isEmpty.class);
            Annotation phoneAnnotation = field.getAnnotation(isValidPhone.class);
            Annotation emailAnnotation = field.getAnnotation(isValidEmail.class);

            if (emptyAnnotation != null) {

                isEmpty empty = (isEmpty) emptyAnnotation;

                /**
                 *  Check if it says this field is required
                 */
                if (empty.value()) {
                    /**
                     *  Now we make sure we can access the private
                     *  fields also, so we need to call this method also
                     *  other wise we would get a {@link java.lang.IllegalAccessException}
                     */
                    field.setAccessible(true);

                    if( field.get(objectToValidate) != null) {
                        if( field.get(objectToValidate).toString().isEmpty() ) {
                            showMessage(context, empty.errorMsg(), empty.errorDisplayType());
                            return false;
                        }
                    }
                    else {
                        showMessage(context, empty.errorMsg(), empty.errorDisplayType());
                        return false;
                    }
                }

            } else if(phoneAnnotation != null) {

                isValidPhone phone = (isValidPhone) phoneAnnotation;

                if (phone.value()) {

                    field.setAccessible(true);

                    if( field.get(objectToValidate) != null) {

                        if( field.get(objectToValidate).toString().isEmpty() ||
                                !Patterns.PHONE.matcher(field.get(objectToValidate).toString()).matches() ) {
                            showMessage(context, phone.errorMsg(), phone.errorDisplayType() );
                            return false;
                        }
                    }
                    else {
                        showMessage(context, phone.errorMsg(), phone.errorDisplayType());
                        return false;
                    }
                }

            } else if(emailAnnotation != null) {
                isValidEmail email = (isValidEmail) emailAnnotation;

                if (email.value()) {

                    field.setAccessible(true);

                    if( field.get(objectToValidate) != null) {
                        if( field.get(objectToValidate).toString().isEmpty() ||
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(field.get(objectToValidate).toString()).matches() ) {
                            showMessage(context, email.errorMsg(), email.errorDisplayType() );
                            return false;
                        }
                    }
                    else {
                        showMessage(context, email.errorMsg(), email.errorDisplayType());
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void showMessage(Context context, String msg, DisplayType displayType) {
        switch (displayType) {
            case Toast:
                CommonUtilities.customToast(context, msg);
                break;

            case Materialized:
                break;

            case Toggle:
                break;
        }
    }

    public enum DisplayType {
        Toast,
        Materialized,
        Toggle
    }
}