package cn.homelabs.pdms.hospital.di;

import cn.homelabs.pdms.hospital.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModules {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}
