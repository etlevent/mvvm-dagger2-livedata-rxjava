package cn.homelabs.pdms.hospital.di;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import cn.homelabs.pdms.hospital.MVVMApplication;
import cn.homelabs.pdms.hospital.R;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModules {
    @Singleton
    @Binds
    public abstract Context context(MVVMApplication application);

    @Named("appName")
    @Singleton
    @Provides
    public static String providerAppName(Context context) {
        return context.getString(R.string.app_name);
    }
}
