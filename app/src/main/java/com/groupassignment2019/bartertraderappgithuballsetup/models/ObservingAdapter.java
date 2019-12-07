package com.groupassignment2019.bartertraderappgithuballsetup.models;

import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.CompleteOnlyWhenNeededObservable;

public interface ObservingAdapter {
    public void update(CompleteOnlyWhenNeededObservable observable, int howObserverWillIdentifyObservable);
}
