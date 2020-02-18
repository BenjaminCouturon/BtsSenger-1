package fr.lasalle.btssenger.presentation.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.lasalle.btssenger.ChatsFragment;
import fr.lasalle.btssenger.FriendsFragment;
import fr.lasalle.btssenger.RequestsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String[] TITLES = {"CHATS", "FRIENDS", "REQUESTS"};

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return new ChatsFragment();

            case 1 :
                return new FriendsFragment();

            case 2 :
                return new RequestsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
