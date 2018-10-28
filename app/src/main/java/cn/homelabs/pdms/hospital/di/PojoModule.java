package cn.homelabs.pdms.hospital.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PojoModule {

    @Singleton
    @Provides
    public String provideName() {
        return "Jack";
    }
}
