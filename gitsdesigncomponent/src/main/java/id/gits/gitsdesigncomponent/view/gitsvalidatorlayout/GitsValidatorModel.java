package id.gits.gitsdesigncomponent.view.gitsvalidatorlayout;

import android.view.View;

public class GitsValidatorModel {

    private View mView;
    private String name;
    private String validationType;
    private GitsViewType viewType;

    public GitsValidatorModel(View mView, String name, String validationType, GitsViewType viewType) {
        this.mView = mView;
        this.name = name;
        this.validationType = validationType;
        this.viewType = viewType;
    }

    public View getView() {
        return mView;
    }

    public void setView(View mView) {
        this.mView = mView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public GitsViewType getViewType() {
        return viewType;
    }

    public void setViewType(GitsViewType viewType) {
        this.viewType = viewType;
    }
}
