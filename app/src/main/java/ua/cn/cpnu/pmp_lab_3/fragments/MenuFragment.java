package ua.cn.cpnu.pmp_lab_3.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ua.cn.cpnu.pmp_lab_3.R;
import ua.cn.cpnu.pmp_lab_3.contract.ResponseListener;
import ua.cn.cpnu.pmp_lab_3.model.Options;
import ua.cn.cpnu.pmp_lab_3.model.Questions;

// main menu fragment class
public class MenuFragment extends BaseFragment {

    private static final String KEY_OPTIONS = "OPTIONS";

    private Options options;
    public static Questions[] arrQuestions = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            options = savedInstanceState.getParcelable(KEY_OPTIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_menu,
                container,
                false
        );
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.options_button)
                .setOnClickListener(v -> getAppContract().toOptionsScreen(this, options));
        view.findViewById(R.id.quit_button)
                .setOnClickListener(v -> getAppContract().cancel());

        Button getPlayButton = view
                .findViewById(R.id.start_button);
        getPlayButton.setOnClickListener(v -> getAppContract().toPlayScreen(this, options, arrQuestions));
        updateView();
    }

    // unregister receiver after destroying menu fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_OPTIONS, options);
    }

    private void updateView() {
    }

    private ResponseListener<Options> listener = options -> {
        this.options = options;
    };
}