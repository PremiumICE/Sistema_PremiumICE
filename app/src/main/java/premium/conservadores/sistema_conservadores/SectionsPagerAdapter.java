package premium.conservadores.sistema_conservadores;

import static java.util.Objects.*;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class SectionsPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {
        Fragment fra = new Fragment();
        if(position == 0) {
            fra = InicioFragment.newInstance(1);
        }
        else
            fra = ConfiguracionFragment.newInstance(1);
        return fra;
    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}