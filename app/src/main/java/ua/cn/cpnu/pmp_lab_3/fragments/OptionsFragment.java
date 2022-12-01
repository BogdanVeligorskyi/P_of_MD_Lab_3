package ua.cn.cpnu.pmp_lab_3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ua.cn.cpnu.pmp_lab_3.R;
import ua.cn.cpnu.pmp_lab_3.model.Options;

// options fragment class
public class OptionsFragment extends BaseFragment {

    // keys and fields
    private static final String ARG_OPTIONS = "OPTIONS";
    private static final String KEY_QUESTIONS = "QUESTIONS";
    private static final String KEY_HINT = "HINT";
    private CheckBox hintAvailability;
    private Spinner numberOfQuestionsSpinner;

    public static OptionsFragment newInstance(
            @Nullable Options options) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_OPTIONS, options);
        OptionsFragment fragment = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_options,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numberOfQuestionsSpinner = view
                .findViewById(R.id.questions_spinner);
        hintAvailability = view
                .findViewById(R.id.hint_availability);
        setupButtons(view);
        int selectedNum = 0;
        //boolean isHint = false;

        if (savedInstanceState != null) {
            // so need to restore only Spinner data
            selectedNum = savedInstanceState
                    .getInt(KEY_QUESTIONS);
            savedInstanceState.getBoolean(KEY_HINT);
        } else {
            Options options = getOptionsArg();
            if (options != null) {
                hintAvailability.setChecked(options.getIs_hint_available());
                selectedNum = options.getNumber_of_questions();
            }
        }
        setupQuestionsNumSpinner(String.valueOf(selectedNum));
    }

    // setup "Cancel" and "Ok" buttons in OptionsFragment
    private void setupButtons(View view) {
        view.findViewById(R.id.cancel)
                .setOnClickListener(v -> getAppContract().cancel());
        view.findViewById(R.id.ok)
                .setOnClickListener(v -> {
                    Options options = new Options(
                            Integer.parseInt(numberOfQuestionsSpinner.getSelectedItem().toString()),
                            hintAvailability.isChecked()
                    );
                    getAppContract().publish(options);
                    getAppContract().cancel();
                });
    }

    // setup spinner according to chosen number of questions
    private void setupQuestionsNumSpinner(
            @Nullable String selectedNum) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_group,
                Options.QUESTIONS_NUM
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_dropdown_item_1line);
        numberOfQuestionsSpinner.setAdapter(adapter);
        if (selectedNum != null) {
            int index = Options.QUESTIONS_NUM.indexOf(selectedNum);
            if (index != -1) numberOfQuestionsSpinner.setSelection(index);
        }
    }

    // get pre-defined options
    private Options getOptionsArg() {
        assert getArguments() != null;
        return getArguments().getParcelable(ARG_OPTIONS);
    }

}
