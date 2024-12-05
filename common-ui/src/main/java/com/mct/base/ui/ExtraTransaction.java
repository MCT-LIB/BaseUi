package com.mct.base.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;
import com.mct.base.ui.transition.FragmentTransition;
import com.mct.base.ui.transition.FragmentTransitionFactory;

import java.lang.reflect.Field;

class ExtraTransaction implements IExtraTransaction {

    private static final int FLAG_INCLUSIVE = FragmentManager.POP_BACK_STACK_INCLUSIVE;

    private final int mContainerId;
    private final FragmentManager mFragmentManager;
    private final IKeyboardManager mKeyboardManager;

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
        transaction.commitAllowingStateLoss();
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
        if (getCurrentFragment() != null) {
            transaction.hide(getCurrentFragment());
        }
        transaction.add(mContainerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
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
        transaction.commitAllowingStateLoss();
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
        transaction.commitAllowingStateLoss();
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
        allowStateLoss(() -> {
            if (getBackStackCount() > 0) {
                if (immediate) {
                    mFragmentManager.popBackStackImmediate(null, FLAG_INCLUSIVE);
                } else {
                    mFragmentManager.popBackStack(null, FLAG_INCLUSIVE);
                }
            }
        });
    }

    @Override
    public void popFragment() {
        popFragment(false);
    }

    @Override
    public void popFragment(boolean immediate) {
        performHideSoftInput();
        allowStateLoss(() -> {
            if (immediate) {
                mFragmentManager.popBackStackImmediate();
            } else {
                mFragmentManager.popBackStack();
            }
        });
    }

    @Override
    public void popFragmentToPosition(int position) {
        int count = getBackStackCount();
        if (count == 0 || position >= count) {
            return;
        }
        performHideSoftInput();
        allowStateLoss(() -> {
            int targetIndex = Math.max(0, position);
            int id = mFragmentManager.getBackStackEntryAt(targetIndex).getId();
            mFragmentManager.popBackStack(id, FLAG_INCLUSIVE);
        });
    }

    @Override
    public void popFragmentByAmount(int amount) {
        int count = getBackStackCount();
        if (count == 0 || amount <= 0) {
            return;
        }
        performHideSoftInput();
        allowStateLoss(() -> {
            int targetIndex = Math.max(0, count - amount);
            int targetId = mFragmentManager.getBackStackEntryAt(targetIndex).getId();
            mFragmentManager.popBackStack(targetId, FLAG_INCLUSIVE);
        });
    }

    @Override
    public void popFragmentTo(@NonNull Class<? extends Fragment> targetFragment) {
        popFragmentTo(targetFragment, false);
    }

    @Override
    public void popFragmentTo(@NonNull Class<? extends Fragment> targetFragment, boolean includeTargetFragment) {
        performHideSoftInput();
        allowStateLoss(() -> {
            int flag = includeTargetFragment ? FLAG_INCLUSIVE : 0;
            mFragmentManager.popBackStack(targetFragment.getName(), flag);
        });
    }

    private boolean isCurrentFragmentInBackStack() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment == null) {
            return false;
        }
        int backStackCount = getBackStackCount();
        if (getBackStackCount() > 0) {
            String topTag = mFragmentManager.getBackStackEntryAt(backStackCount - 1).getName();
            String currentTag = currentFragment.getTag();
            return topTag != null && topTag.equals(currentTag);
        }
        return false;
    }

    private void performHideSoftInput() {
        mKeyboardManager.hideSoftInput(200);
    }

    private void allowStateLoss(Runnable runnable) {
        FragmentManager fm = mFragmentManager;
        if (fm.isStateSaved()) {
            try {
                boolean tempStateSaved = FragmentManagerMagician.isStateSaved(fm);
                boolean tempStopped = FragmentManagerMagician.isStopped(fm);
                FragmentManagerMagician.setStateSaved(fm, false);
                FragmentManagerMagician.setStopped(fm, false);

                runnable.run();

                FragmentManagerMagician.setStateSaved(fm, tempStateSaved);
                FragmentManagerMagician.setStopped(fm, tempStopped);
            } catch (IllegalAccessException ignored) {
                runnable.run();
            }
        } else {
            runnable.run();
        }
    }

    private static class FragmentManagerMagician {

        private static Field mStateSavedField;
        private static Field mStoppedField;

        static {
            try {
                mStateSavedField = FragmentManager.class.getDeclaredField("mStateSaved");
                mStateSavedField.setAccessible(true);
                mStoppedField = FragmentManager.class.getDeclaredField("mStopped");
                mStoppedField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                mStateSavedField = null;
                mStoppedField = null;
            }
        }

        static boolean isStateSaved(FragmentManager fm) throws IllegalAccessException {
            if (mStateSavedField == null) {
                return false;
            }
            return (Boolean) mStateSavedField.get(fm);
        }

        static boolean isStopped(FragmentManager fm) throws IllegalAccessException {
            if (mStoppedField == null) {
                return false;
            }
            return (Boolean) mStoppedField.get(fm);
        }

        static void setStateSaved(FragmentManager fm, boolean stateSaved) throws IllegalAccessException {
            if (mStateSavedField != null) {
                mStateSavedField.set(fm, stateSaved);
            }
        }

        static void setStopped(FragmentManager fm, boolean stopped) throws IllegalAccessException {
            if (mStoppedField != null) {
                mStoppedField.set(fm, stopped);
            }
        }
    }

}
