package ua.cn.cpnu.pmp_lab_3.contract;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ua.cn.cpnu.pmp_lab_3.model.Options;
import ua.cn.cpnu.pmp_lab_3.model.Questions;

// interface for launching fragments
public interface AppContract {
    /**
     * Launch options screen
     * @param target fragment that launches options screen
     * @param options data about the settings of game
     */
    void toOptionsScreen(Fragment target,
                         @Nullable Options options);
    /**
     * Launch results screen
     * @param target fragment that launches results screen
     * @param options data used for game duration
     */
    void toPlayScreen(Fragment target, Options options, Questions[] questions);
    /**
     * Exit from the current screen
     */
    void cancel();
    /**
     * Publish results to the target screen
     */
    <T> void publish(T data);
    /**
     * Listen for results from other screens
     */
    <T> void registerListener(Fragment fragment, Class<T> clazz,
                              ResponseListener<T> listener);
    /**
     * Stop listening for results from other screens
     */
    void unregisterListeners(Fragment fragment);
}