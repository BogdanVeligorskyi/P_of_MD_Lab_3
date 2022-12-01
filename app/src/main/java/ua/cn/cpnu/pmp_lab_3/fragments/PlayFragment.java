package ua.cn.cpnu.pmp_lab_3.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ua.cn.cpnu.pmp_lab_3.R;
import ua.cn.cpnu.pmp_lab_3.model.Options;
import ua.cn.cpnu.pmp_lab_3.model.Questions;

// play screen fragment class
public class PlayFragment extends BaseFragment {

    // special constant for reading options
    public static final String EXTRA_OPTIONS = "EXTRA_OPTIONS";

    // all fields used in the process of game
    private Options options;
    private int current_question_num;
    private boolean is_first_question_answered;
    private boolean is_clicked_checkbox;
    private boolean is_recently_checked_checkbox;
    private static Questions[] arrQuestions = new Questions[10];

    // new instance of this fragment
    public static PlayFragment newInstance(Options options, Questions[] questions) {
        arrQuestions = questions;
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_OPTIONS, options);
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // if device was rotated - retrieve all pre-saved values
        if (savedInstanceState != null) {
            options = savedInstanceState.getParcelable("EXTRA_OPTIONS");
            current_question_num = savedInstanceState.getInt("CURRENT_QUESTION_NUM");
            is_first_question_answered = savedInstanceState.getBoolean("IS_FIRST_QUESTION_ANSWERED");
            is_clicked_checkbox = savedInstanceState.getBoolean("IS_CLICKED_CHECKBOX");
            is_recently_checked_checkbox = savedInstanceState.getBoolean("IS_RECENTLY_CHECKED_CHECKBOX");
            arrQuestions = (Questions[]) savedInstanceState.getParcelableArray("QUESTIONS");
            // if you pressed "Start" in main menu - read options first
            // and shuffle all questions
        } else {
            options = getOptions();
            current_question_num = 1;
            initializeQuestions();
        }
        startSetup(view);

        // opening 'Take the winnings' dialog
        view.findViewById(R.id.quit_game)
                .setOnClickListener(button -> showQuitDialog(getContext()));

        // if you pressed button_1 - check it
        view.findViewById(R.id.button_1).setOnClickListener(button -> checkAnswer(view,0));

        // if you pressed button_2 - check it
        view.findViewById(R.id.button_2).setOnClickListener(button -> checkAnswer(view,1));

        // if you pressed button_3 - check it
        view.findViewById(R.id.button_3).setOnClickListener(button -> checkAnswer(view,2));

        // if you pressed button_4 - check it
        view.findViewById(R.id.button_4).setOnClickListener(button -> checkAnswer(view,3));

    }

    // dialog which is opened after clicking to 'Exit | Take the winnings' button
    private void showQuitDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setIcon(R.mipmap.ic_launcher_round);
        dialog.setTitle("Game over");
        if (!is_first_question_answered) {
            dialog.setMessage("You`ve decided to quit without choosing answer to 1st question, thus you didn`t win anything.");
        } else {
            int won_value = (current_question_num - 1) * 100000;
            dialog.setMessage("Congratulations, you have won $" + won_value + "!");
        }
        dialog.setPositiveButton("Ok", (dialogInterface, i) -> getAppContract().cancel());
        dialog.create();
        dialog.show();
    }

    // dialog which is opened after choosing incorrect answer
    private void showLoseDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setIcon(R.mipmap.ic_launcher_round);
        dialog.setTitle("Game over");
        dialog.setMessage("Unfortunately, you`ve chosen wrong answer and lost the game. Good luck in next game!");
        dialog.setPositiveButton("Ok", (dialogInterface, i) -> getAppContract().cancel());
        dialog.create();
        dialog.show();
    }

    // setup of all UI components after clicking on 'start' in main menu
    private void startSetup(View view) {
        setupCurrentQuestion(view);
        setupCheckBox(view);
        setupButtons(view);
        setupQuestionsNum(view);
        setupQuestionPrice(view);

        // if you rotated device after pressing 'Hint' and before
        // choosing possible answer (to current question) removing incorrect variants is needed
        if (is_recently_checked_checkbox) {
            CheckBox cb = view.findViewById(R.id.is_hint);
            removeIncorrectAnswers(view, cb);
            is_recently_checked_checkbox = false;
        }
    }

    // setup buttons as possible answers to the question
    private void setupButtons(View view) {
        Button but1 = view.findViewById(R.id.button_1);
        Button but2 = view.findViewById(R.id.button_2);
        Button but3 = view.findViewById(R.id.button_3);
        Button but4 = view.findViewById(R.id.button_4);
        but1.setText(arrQuestions[current_question_num-1].variants_arr[0]);
        but2.setText(arrQuestions[current_question_num-1].variants_arr[1]);
        but3.setText(arrQuestions[current_question_num-1].variants_arr[2]);
        but4.setText(arrQuestions[current_question_num-1].variants_arr[3]);
        but1.setEnabled(true);
        but2.setEnabled(true);
        but3.setEnabled(true);
        but4.setEnabled(true);
    }

    // setup text of current question and shuffling possible answers
    private void setupCurrentQuestion(View view) {
        TextView tvQuestionText = view.findViewById(R.id.text_of_question);
        tvQuestionText.setText(arrQuestions[current_question_num-1].text_of_question);
        List<String> list = Arrays.asList(arrQuestions[current_question_num-1].variants_arr);
        Collections.shuffle(list);
    }

    // setup price of question (in $)
    private void setupQuestionPrice(View view) {
        TextView tvPrice  = view.findViewById(R.id.price);
        int question_price = (current_question_num - 1) * 100000;
        String text_price = "$" + question_price;
        tvPrice.setText(text_price);
    }

    // setup current number of question and max number of questions
    private void setupQuestionsNum(View view) {
        TextView tvQuestions = view.findViewById(R.id.question_number);
        String text_question_num = "Question " + current_question_num + " of " + options.getNumber_of_questions();
        tvQuestions.setText(text_question_num);
    }

    // setup checkbox with a possibility to turn on one-time hint during the game
    private void setupCheckBox(View view) {
        CheckBox cb = view.findViewById(R.id.is_hint);
        cb.setEnabled(options.getIs_hint_available() && !is_clicked_checkbox);
        cb.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                removeIncorrectAnswers(view, cb);
                is_recently_checked_checkbox = true;
            } else
                cb.setEnabled(true);
        });
    }

    // initializing all questions, variants, answers, and shuffling of questions
    private void initializeQuestions() {
        List<Questions> list = Arrays.asList(arrQuestions);
        Collections.shuffle(list);
    }


    // check chosen answer
    private void checkAnswer(View view, int index) {
        is_first_question_answered = true;
        // if answer was correct - move to next question (for questions from 1 to (n-1))
        if (arrQuestions[current_question_num-1].variants_arr[index].equalsIgnoreCase(arrQuestions[current_question_num-1].answer) && current_question_num < options.getNumber_of_questions()) {
            current_question_num += 1;
            is_recently_checked_checkbox = false;
            setupQuestionsNum(view);
            setupQuestionPrice(view);
            setupCurrentQuestion(view);
            setupButtons(view);
            // if answer was correct - show winning dialog (for last question)
        } else if (current_question_num >= options.getNumber_of_questions() && (arrQuestions[current_question_num-1].variants_arr[index].equalsIgnoreCase(arrQuestions[current_question_num-1].answer))) {
            showQuitDialog(getContext());
            // else answer is incorrect
        } else {
            showLoseDialog(getContext());
        }
    }

    // remove 2 incorrect answers if 'Hint' was clicked
    private void removeIncorrectAnswers(View view, CheckBox cb) {
        is_clicked_checkbox = true;
        cb.setEnabled(false);
        int counter_incorrect_answers = 0;
        while (true) {
            Button but = view.findViewById(R.id.button_1);
            if (!but.getText().toString().equalsIgnoreCase(arrQuestions[current_question_num - 1].answer)) {
                Log.i("TAG: ", "answer=" + arrQuestions[current_question_num - 1].answer);
                but.setEnabled(false);
                counter_incorrect_answers += 1;
            }
            but = view.findViewById(R.id.button_2);
            if (!but.getText().toString().equalsIgnoreCase(arrQuestions[current_question_num - 1].answer)) {
                but.setEnabled(false);
                counter_incorrect_answers += 1;
                if (counter_incorrect_answers >= 2) {
                    break;
                }
            }
            but = view.findViewById(R.id.button_3);
            if (!but.getText().toString().equalsIgnoreCase(arrQuestions[current_question_num - 1].answer)) {
                but.setEnabled(false);
                counter_incorrect_answers += 1;
                if (counter_incorrect_answers >= 2) {
                    break;
                }
            }
            but = view.findViewById(R.id.button_4);
            if (!but.getText().toString().equalsIgnoreCase(arrQuestions[current_question_num - 1].answer)) {
                but.setEnabled(false);
                counter_incorrect_answers += 1;
                if (counter_incorrect_answers >= 2) {
                    break;
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("ERR", String.valueOf(options.getNumber_of_questions()));
        outState.putParcelable(EXTRA_OPTIONS, options);
        outState.putInt("CURRENT_QUESTION_NUM", current_question_num);
        outState.putBoolean("IS_FIRST_QUESTION_ANSWERED", is_first_question_answered);
        outState.putBoolean("IS_CLICKED_CHECKBOX", is_clicked_checkbox);
        outState.putBoolean("IS_RECENTLY_CHECKED_CHECKBOX", is_recently_checked_checkbox);
        outState.putParcelableArray("QUESTIONS", arrQuestions);
    }

    // read options from OptionsFragment
    private Options getOptions() {
        assert getArguments() != null;
        return getArguments().getParcelable(EXTRA_OPTIONS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
