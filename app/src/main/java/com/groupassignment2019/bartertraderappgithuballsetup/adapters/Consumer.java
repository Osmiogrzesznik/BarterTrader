package com.groupassignment2019.bartertraderappgithuballsetup.adapters;

public interface Consumer<T> {
   void consume(String key, T element, int identifier);
   void noObject(String key,int identifier);
}
