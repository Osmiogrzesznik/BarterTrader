package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import com.groupassignment2019.bartertraderappgithuballsetup.models.ObservingAdapter;

public interface CompleteOnlyWhenNeededObservable {
    boolean isComplete();
    void completeYourselfAndNotifyObserver(ObservingAdapter observer, int howObserverWillIdentifyObservable);
}
