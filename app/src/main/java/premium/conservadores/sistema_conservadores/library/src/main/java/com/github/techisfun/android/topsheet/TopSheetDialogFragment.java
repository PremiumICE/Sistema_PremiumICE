package premium.conservadores.sistema_conservadores.library.src.main.java.com.github.techisfun.android.topsheet;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import premium.conservadores.sistema_conservadores.library.src.main.java.com.github.techisfun.android.topsheet.TopSheetDialog;

/**
 * Created by andrea on 23/08/16.
 */
public class TopSheetDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TopSheetDialog(getContext(), getTheme());
    }
}
