package cn.homelabs.pdms.hospital.di;

import javax.inject.Singleton;

import cn.homelabs.pdms.hospital.MVVMApplication;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityModules.class,
        POJOModule.class
})
public interface AppComponents extends AndroidInjector<MVVMApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MVVMApplication> {
    }
}
