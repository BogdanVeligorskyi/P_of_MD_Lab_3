package ua.cn.cpnu.pmp_lab_3.contract;

// one more interface for fragments
public interface ResponseListener<T> {
    void onResults(T results);
}
