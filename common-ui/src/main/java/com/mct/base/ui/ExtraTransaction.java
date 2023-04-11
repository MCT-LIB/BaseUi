package com.mct.base.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;
import com.mct.base.ui.transition.FragmentTransition;
import com.mct.base.ui.transition.FragmentTransitionFactory;

import java.util.ArrayList;
import java.util.List;

class ExtraTransaction implements IExtraTransaction {

    private final int mContainerId;
    private final FragmentManager mFragmentManager;
    private final IKeyboardManager mKeyboardManager;
    private final List<Integer> mFragmentIds = new ArrayList<>();

    public ExtraTransaction(int mContainerId, FragmentManager mFragmentManager, IKeyboardManager mKeyboardManager) {
        this.mContainerId = mContainerId;
        this.mFragmentManager = mFragmentManager;
        this.mKeyboardManager = mKeyboardManager;
    }

    @Override
    public int getContainerId() {
        return mContainerId;
    }

    @Override
    public FragmentManager getFragmentManager() {
        return mFragmentManager;
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
    public void addFragment(Fragment fragment) {
        addFragment(fragment, FragmentTransitionFactory.createDefaultTransition());
    }

    @Override
    public void addFragment(Fragment fragment, @NonNull FragmentTransition transition) {
        performHideSoftInput();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transition.applyTransition(transaction);
        transaction.add(mContainerId, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void addFragmentToStack(Fragment fragment) {
        addFragmentToStack(fragment, FragmentTransitionFactory.createDefaultTransition());
    }

    @Override
    public void addFragmentToStack(Fragment fragment, @NonNull FragmentTransition transition) {
        performHideSoftInput();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transition.applyTransition(transaction);
        transaction.add(mContainerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        mFragmentIds.add(transaction.commit());
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, FragmentTransitionFactory.createDefaultTransition());
    }

    @Override
    public void replaceFragment(Fragment fragment, @NonNull FragmentTransition transition) {
        if (isCurrentFragmentInBackStack()) {
            popFragment(transition.couldPopImmediate());
            replaceFragmentToStack(fragment, transition);
            return;
        }
        replaceRootFragment(fragment, transition);
    }

    private void replaceRootFragment(Fragment fragment, @NonNull FragmentTransition transition) {
        performHideSoftInput();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transition.applyTransition(transaction);
        transaction.replace(mContainerId, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void replaceFragmentToStack(Fragment fragment) {
        replaceFragmentToStack(fragment, FragmentTransitionFactory.createDefaultTransition());
    }

    @Override
    public void replaceFragmentToStack(Fragment fragment, @NonNull FragmentTransition transition) {
        performHideSoftInput();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transition.applyTransition(transaction);
        transaction.replace(mContainerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        mFragmentIds.add(transaction.commit());
    }

    @Override
    public void replaceAndClearBackStack(Fragment fragment) {
        replaceAndClearBackStack(fragment, FragmentTransitionFactory.createDefaultTransition());
    }

    @Override
    public void replaceAndClearBackStack(Fragment fragment, @NonNull FragmentTransition transition) {
        clearBackStack(transition.couldPopImmediate());
        replaceRootFragment(fragment, transition);
    }

    @Override
    public void clearBackStack() {
        clearBackStack(false);
    }

    @Override
    public void clearBackStack(boolean immediate) {
        performHideSoftInput();
        if (getBackStackCount() > 0) {
            mFragmentIds.clear();
            if (immediate) {
                mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    @Override
    public void popFragment() {
        popFragment(false);
    }

    @Override
    public void popFragment(boolean immediate) {
        performHideSoftInput();
        if (!mFragmentIds.isEmpty()) {
            mFragmentIds.remove(mFragmentIds.size() - 1);
        }
        if (immediate) {
            mFragmentManager.popBackStackImmediate();
        } else {
            mFragmentManager.popBackStack();
        }
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
        mKeyboardManager.hideSoftInput();
    }
}
