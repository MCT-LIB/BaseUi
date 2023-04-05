package com.mct.base.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.abs.IExtraTransaction;
import com.mct.base.ui.abs.IKeyboardManager;
import com.mct.base.ui.transition.FragmentTransition;
import com.mct.base.ui.transition.NoneFragmentTransition;

import java.util.ArrayList;
import java.util.List;

class ExtraTransaction implements IExtraTransaction {

    private final int mContainerId;
    private final FragmentManager mFragmentManager;
    private final IKeyboardManager mKeyboardManager;
    private boolean mPendingDisableAutoHideSoftInput;
    private final List<Integer> mFragmentIds = new ArrayList<>();

    public ExtraTransaction(int mContainerId, FragmentManager mFragmentManager, IKeyboardManager mKeyboardManager) {
        this.mContainerId = mContainerId;
        this.mFragmentManager = mFragmentManager;
        this.mKeyboardManager = mKeyboardManager;
    }

    @Override
    public int getBackStackCount() {
        return mFragmentManager.getBackStackEntryCount();
    }

    @Override
    public Fragment getCurrentFragment() {
        return mFragmentManager.findFragmentById(mContainerId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Fragment> T findFragmentByTag(Class<T> targetFragment) {
        try {
            return (T) mFragmentManager.findFragmentByTag(targetFragment.getName());
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, new NoneFragmentTransition());
    }

    @Override
    public void replaceFragment(Fragment fragment, FragmentTransition transition) {
        performHideSoftInput();
        if (isCurrentFragmentInBackStack()) {
            mPendingDisableAutoHideSoftInput = true;
            popFragment();
            mPendingDisableAutoHideSoftInput = true;
            replaceFragmentToStack(fragment, transition);
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        FragmentTransition.apply(transaction, transition);
        transaction.replace(mContainerId, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void replaceFragmentToStack(Fragment fragment) {

        replaceFragmentToStack(fragment, new NoneFragmentTransition());
    }

    @Override
    public void replaceFragmentToStack(Fragment fragment, FragmentTransition transition) {
        performHideSoftInput();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        FragmentTransition.apply(transaction, transition);
        transaction.replace(mContainerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        mFragmentIds.add(transaction.commit());
    }

    @Override
    public void replaceAndClearBackStack(Fragment fragment) {
        replaceAndClearBackStack(fragment, new NoneFragmentTransition());
    }

    @Override
    public void replaceAndClearBackStack(Fragment fragment, FragmentTransition transition) {
        clearBackStack();
        replaceFragment(fragment, transition);
    }

    @Override
    public void clearBackStack() {
        if (getBackStackCount() > 0) {
            mFragmentIds.clear();
            mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void popFragment() {
        performHideSoftInput();
        if (!mFragmentIds.isEmpty()) {
            mFragmentIds.remove(mFragmentIds.size() - 1);
        }
        mFragmentManager.popBackStack();
    }

    @Override
    public void popFragmentToPosition(int position) {
        performHideSoftInput();
        if (mFragmentIds.size() > position) {
            mFragmentIds.subList(position + 1, mFragmentIds.size()).clear();
            mFragmentManager.popBackStack(mFragmentIds.remove(position), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void popFragmentByAmount(int amount) {
        performHideSoftInput();
        int size = mFragmentIds.size();
        if (size - amount < 0) {
            mFragmentIds.clear();
            clearBackStack();
        } else if (size > size - amount) {
            mFragmentIds.subList(size - amount + 1, size).clear();
            mFragmentManager.popBackStack((mFragmentIds.remove(size - amount)), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void popToFragment(@NonNull Class<? extends Fragment> targetFragment) {
        popToFragment(targetFragment, false);
    }

    @Override
    public void popToFragment(@NonNull Class<? extends Fragment> targetFragment, boolean includeTargetFragment) {
        performHideSoftInput();
        int flag = includeTargetFragment ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0;
        boolean isPopped = mFragmentManager.popBackStackImmediate(targetFragment.getName(), flag);
        if (isPopped && mFragmentIds.size() > getBackStackCount()) {
            mFragmentIds.subList(getBackStackCount(), mFragmentIds.size()).clear();
        }
    }

    private boolean isCurrentFragmentInBackStack() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment == null) {
            return false;
        }
        int backStackCount = getBackStackCount();
        if (getBackStackCount() > 0) {
            FragmentManager.BackStackEntry topEntry = mFragmentManager.getBackStackEntryAt(backStackCount - 1);
            String topTag = topEntry.getName();
            String currentTag = currentFragment.getTag();
            return topTag != null && topTag.equals(currentTag);
        }
        return false;
    }

    private void performHideSoftInput() {
        if (mPendingDisableAutoHideSoftInput) {
            mPendingDisableAutoHideSoftInput = false;
            return;
        }
        mKeyboardManager.hideSoftInput();
    }
}
