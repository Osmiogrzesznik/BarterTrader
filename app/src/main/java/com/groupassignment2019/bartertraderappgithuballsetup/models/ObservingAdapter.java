package com.groupassignment2019.bartertraderappgithuballsetup.models;

import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.CompleteOnlyWhenNeededObservable;

public interface ObservingAdapter {
    void update(CompleteOnlyWhenNeededObservable observable, int howObserverWillIdentifyObservable);
}
