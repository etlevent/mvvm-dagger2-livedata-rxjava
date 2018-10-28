package cn.homelabs.pdms.hospital;

import cn.homelabs.pdms.hospital.di.DaggerAppComponents;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MVVMApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponents.builder().create(this);
    }
}
