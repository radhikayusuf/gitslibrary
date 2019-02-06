package id.gits.gitsdesigncomponent.view.gitsvalidatorlayout;

import java.util.List;

public class GitsValidatorResultModel {
    private boolean result;
    private List<String> message;

    public GitsValidatorResultModel(boolean result, List<String> message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
