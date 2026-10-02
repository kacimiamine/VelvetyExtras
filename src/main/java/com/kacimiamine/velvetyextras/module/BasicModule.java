package com.kacimiamine.velvetyextras.module;

import com.kacimiamine.velvetyextras.VelvetyExtras;

public class BasicModule implements Module {

    @Override
    public void enable() {
        VelvetyExtras.logger().info("BasicModule enabled!");
    }
}
