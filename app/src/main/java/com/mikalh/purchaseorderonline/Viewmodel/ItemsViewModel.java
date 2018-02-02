package com.mikalh.purchaseorderonline.Viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.mikalh.purchaseorderonline.Model.Item;

import java.util.ArrayList;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class ItemsViewModel extends ViewModel {
    ArrayList<Item> items;



}
