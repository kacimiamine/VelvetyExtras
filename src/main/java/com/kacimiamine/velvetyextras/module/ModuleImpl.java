package com.kacimiamine.velvetyextras.module;

import com.kacimiamine.velvetyextras.VelvetyExtras;

public abstract class ModuleImpl implements Module {

    // CONFIG VALUES
    protected boolean enabled = true;

    protected abstract String name();

    @Override
    public void enable() {
        if (!isEnabled()) return;
        VelvetyExtras.logger().info("{} module is enabled!", this.name());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
