package com.mct.base.ui.abs;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mct.base.ui.transition.FragmentTransition;

public interface IExtraTransaction {

    int getBackStackCount();

    Fragment getCurrentFragment();

    <T extends Fragment> T findFragmentByTag(Class<T> targetFragment);

    void replaceFragment(Fragment fragment);

    void replaceFragment(Fragment fragment, FragmentTransition transition);

    void replaceFragmentToStack(Fragment fragment);

    void replaceFragmentToStack(Fragment fragment, FragmentTransition transition);

    void replaceAndClearBackStack(Fragment fragment);

    void replaceAndClearBackStack(Fragment fragment, FragmentTransition transition);

    void clearBackStack();

    void popFragment();

    /**
     * @param position is position of fragment in stack
     *                 position >= 0
     */
    void popFragmentToPosition(int position);

    /**
     * @param amount pop fragments last word by amount
     *               amount > 0
     */
    void popFragmentByAmount(int amount);

    /**
     * @param targetFragment pop to fragment if has
     */
    void popToFragment(@NonNull Class<? extends Fragment> targetFragment);

    /**
     * @param targetFragment pop to fragment if has
     */
    void popToFragment(@NonNull Class<? extends Fragment> targetFragment, boolean includeTargetFragment);

}