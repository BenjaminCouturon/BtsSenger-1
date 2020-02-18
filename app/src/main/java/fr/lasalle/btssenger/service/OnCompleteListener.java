package fr.lasalle.btssenger.service;

public interface OnCompleteListener<T> {
    void onSuccess(T result);
    void onError();
    void onLoad(boolean loading);
}
