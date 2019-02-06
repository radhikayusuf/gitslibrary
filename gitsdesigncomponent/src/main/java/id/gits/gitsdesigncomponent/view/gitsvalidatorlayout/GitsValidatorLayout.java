package id.gits.gitsdesigncomponent.view.gitsvalidatorlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitsValidatorLayout extends FrameLayout {

    private List<GitsValidatorModel> mViewList = new ArrayList<>();

    public static final String VALIDATION_EMPTY_OR_NULL = "validationEmptyOrNull";
    public static final String VALIDATION_EMAIL_REGEX = "validationEmailRegex";
    public static final String VALIDATION_PASSWORD_WITH_NUMBER = "validationPasswordWithNumber";
    public static final String VALIDATION_PASSWORD_WITH_SPECIAL_CHAR = "validationPasswordWithSpecialChar";
    public static final String VALIDATION_PASSWORD_FULL_SECURE = "validationPasswordFullSecure";
    public static final String VALIDATION_RADIO_TYPE = "validationRadioGroup";
    public static final String VALIDATION_SPINNER = "validationSpinner";


    public static final int MAXIMUM_CHAR_LENGTH = 255;
    public static final int MINIMUM_PASSWORD_LENGTH = 8;
    public static final int MAXIMUM_PASSWORD_LENGTH = 12;


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^([A-Z|a-z|0-9](\\.|_){0,1})+[A-Z|a-z|0-9]\\@([A-Z|a-z|0-9])+((\\.){0,1}[A-Z|a-z|0-9]){4}\\.[a-z]{2,3}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_WITH_NUMBER = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,12}$");
    public static final Pattern VALID_PASSWORD_WITH_SPECIAL_CHAR = Pattern.compile("^(?=)(?=.*[a-z])(?=.*[@#$%!])(?=.*[a-zA-Z]).{8,12}$");
    public static final Pattern VALID_PASSWORD_WITH_FULL_SECURE = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[@#$%!])(?=.*[a-zA-Z]).{8,12}$");


    public GitsValidatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public GitsValidatorLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GitsValidatorLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialComponent() {
        getAllChildFromViewGroup(this);
        validateAllValue();
    }

    private void getAllChildFromViewGroup(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            final View viewChild = viewGroup.getChildAt(i);
            if (viewChild instanceof ViewGroup) {
                if (viewChild instanceof RadioGroup && isValidationAvailable(viewChild.getTag())) {
                    mViewList.add(new GitsValidatorModel(viewChild, getViewName(viewChild.getTag()), getViewValidationType(viewChild.getTag()), GitsViewType.TYPE_RADIOGROUP));
                } else if (viewChild instanceof Spinner && isValidationAvailable(viewChild.getTag())) {
                    mViewList.add(new GitsValidatorModel(viewChild, getViewName(viewChild.getTag()), getViewValidationType(viewChild.getTag()), GitsViewType.TYPE_SPINNER));
                } else {
                    getAllChildFromViewGroup((ViewGroup) viewChild);
                }
            } else {
                if (isValidationAvailable(viewChild.getTag())) {
                    if (viewChild instanceof EditText) {
                        mViewList.add(new GitsValidatorModel(viewChild, getViewName(viewChild.getTag()), getViewValidationType(viewChild.getTag()), GitsViewType.TYPE_EDITTEXT));
                    } else if (viewChild instanceof TextView) {
                        mViewList.add(new GitsValidatorModel(viewChild, getViewName(viewChild.getTag()), getViewValidationType(viewChild.getTag()), GitsViewType.TYPE_TEXTVIEW));
                    }
                }
            }
        }
    }


    private String getViewName(@Nullable Object value) {
        if (value != null) {
            String tag = value.toString();
            if (tag.contains("|")) {
                String[] arr = tag.split("\\|");
                if (arr.length > 1) {
                    return arr[1];
                } else {
                    return "-";
                }
            } else {
                return "-";
            }
        } else return "-";
    }

    private String getViewValidationType(@Nullable Object value) {
        if (value != null) {
            String tag = value.toString();
            if (tag.contains("|")) {
                return tag.split("\\|")[0];
            } else {
                return tag;
            }
        } else return "-";
    }


    private boolean isValidationAvailable(@Nullable Object tag) {
        if (tag != null) {
            String value = tag.toString();
            if (!value.isEmpty()) {
                value = getViewValidationType(tag);
                return value.equalsIgnoreCase(VALIDATION_EMPTY_OR_NULL)
                        || value.equalsIgnoreCase(VALIDATION_EMAIL_REGEX)
                        || value.equalsIgnoreCase(VALIDATION_PASSWORD_WITH_NUMBER)
                        || value.equalsIgnoreCase(VALIDATION_PASSWORD_WITH_SPECIAL_CHAR)
                        || value.equalsIgnoreCase(VALIDATION_PASSWORD_FULL_SECURE)
                        || value.equalsIgnoreCase(VALIDATION_RADIO_TYPE)
                        || value.equalsIgnoreCase(VALIDATION_SPINNER);
            } else return false;
        } else return false;
    }

    public GitsValidatorResultModel validateAllValue() {
        List<String> validationFailedMessage = new ArrayList<>();
        boolean result = true;

        for (GitsValidatorModel validatorModel : mViewList) {
            boolean validateResult = false;
            if (validatorModel.getView() instanceof EditText) {
                validateResult = validateEditText((EditText) validatorModel.getView(), validatorModel.getValidationType());
                if (!validateResult) validationFailedMessage.add(getErrorMessage(validatorModel));
                result = result && validateResult;
            } else if (validatorModel.getView() instanceof TextView) {
                validateResult = validateTextView((EditText) validatorModel.getView());
                if (!validateResult) validationFailedMessage.add(getErrorMessage(validatorModel));
                result = result && validateResult;
            } else if (validatorModel.getView() instanceof RadioGroup) {
                validateResult = validateRadioGroup((RadioGroup) validatorModel.getView());
                if (!validateResult) validationFailedMessage.add(getErrorMessage(validatorModel));
                result = result && validateResult;
            } else if (validatorModel.getView() instanceof Spinner) {
                validateResult = validateSpinner((Spinner) validatorModel.getView());
                if (!validateResult) validationFailedMessage.add(getErrorMessage(validatorModel));
                result = result && validateResult;
            }
        }

        return new GitsValidatorResultModel(result, validationFailedMessage);
    }

    private boolean validateSpinner(Spinner view) {
        return view.getSelectedItemPosition() != 0;
    }

    private boolean validateRadioGroup(RadioGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            if (view.getChildAt(i) instanceof RadioButton) {
                if (((RadioButton) view.getChildAt(i)).isChecked()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean validateEditText(EditText view, String validationType) {
        String textContent = view.getText().toString().trim();
        Matcher matcher;
        switch (validationType) {
            case VALIDATION_EMAIL_REGEX:
                matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(textContent);
                break;
            case VALIDATION_PASSWORD_WITH_NUMBER:
                matcher = VALID_PASSWORD_WITH_NUMBER.matcher(textContent);
                break;
            case VALIDATION_PASSWORD_WITH_SPECIAL_CHAR:
                matcher = VALID_PASSWORD_WITH_SPECIAL_CHAR.matcher(textContent);
                break;
            case VALIDATION_PASSWORD_FULL_SECURE:
                matcher = VALID_PASSWORD_WITH_FULL_SECURE.matcher(textContent);
                break;
            default:
                return !textContent.isEmpty();
        }
        if (matcher != null) {
            return matcher.find();
        } else {
            return false;
        }
    }

    private String getErrorMessage(GitsValidatorModel model) {
        String message = "";
        switch (model.getValidationType()) {
            case VALIDATION_EMAIL_REGEX:
                message += "Field " + model.getName() + " doesn't valid email format.";
                break;
            case VALIDATION_PASSWORD_WITH_NUMBER:
                message += "Field " + model.getName() + " required number.";
                break;
            case VALIDATION_PASSWORD_WITH_SPECIAL_CHAR:
                message += "Field " + model.getName() + " required special character.";
                break;
            case VALIDATION_PASSWORD_FULL_SECURE:
                message += "Field " + model.getName() + " required number & special character.";
                break;
            case VALIDATION_SPINNER:
                message += "Please choose one of " + model.getName() + "Options.";
                break;
            case VALIDATION_RADIO_TYPE:
                message += "Please choose one of " + model.getName() + "Options.";
                break;
            default:
                message += "Field " + model.getName() + " cannot be empty.";
        }

        return message;
    }

    private boolean validateTextView(TextView view) {
        return !view.getText().toString().trim().isEmpty();
    }
}
