package com.zxyapp.taobaounion.ui.adatper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.zxyapp.taobaounion.model.domain.Categories;
import com.zxyapp.taobaounion.ui.fragment.HomePagerFragment;
import com.zxyapp.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataDTO> mCategoriesList=new ArrayList<> ();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super (fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoriesList.get (position).getTitle ();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        LogUtils.d (this,"getItem------->"+position);
        Categories.DataDTO dataDTO=mCategoriesList.get (position);
        HomePagerFragment homePagerAdapter=HomePagerFragment.newInstance (dataDTO);
        return homePagerAdapter;
    }

    @Override
    public int getCount() {
        return mCategoriesList.size ();
    }

    public void setCategories(Categories categories){
        mCategoriesList.clear ();
        List<Categories.DataDTO> data = categories.getData ();
        mCategoriesList.addAll(data);
        notifyDataSetChanged ();
    }
}